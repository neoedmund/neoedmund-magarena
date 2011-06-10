package magic.ai;

import java.util.*;

import magic.model.MagicGame;
import magic.model.phase.MagicPhase;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.MagicRandom;

/*
UCT algorithm from Kocsis and Sezepesvari 2006

function playOneSeq(root)
    nodes = [root]
    while (nodes.last is not leaf) do
      nodes append descendByUCB1(node.last)
    //assume value of leaf nodes are known
    //node.init is all elements except the last one
    updateValue(nodes.init, -nodes.last.value) 

function descendByUCB1(node)
    nb = sum of nb in node's children
    for each node n in node's children
        if n.nb = 0
          v[n] = infinity
        else
          v[n] = 1 - n.value/n.nb + sqrt(2 * log(nb) / n.nb)
    return n that maximizes v[n]

function updateValue(nodes, value)
    for each node n in nodes
        n.value += value
        n.nb += 1
        value = 1 - value

Modified UCT for MoGO in Wang and Gelly 2007

function playOneGame(state)
   create node root from current game state
   init tree to empty tree
   while there is time and memory
     //build the game tree one node at a time
     playOneSeqMC(root, tree)
   return descendByUCB1(root)

function playOneSeqMC(root, tree)
   nodes = [root]
   while (nodes.last is not in the tree)
     nodes append descendByUCB1(node.last)
   tree add nodes.last
   nodes.last.value = getValueByMC(nodes.last)
   updateValue(nodes.init, -nodes.last.value)

function getValueByMC(node) 
   play one random game starting from node
   return 1 if player 1 (max) wins, 0 if player 2 wins (min)
*/

//AI using Monte Carlo Tree Search
public class MCTSAI implements MagicAI {

    private final List<Integer> simLengths = new LinkedList<Integer>();
    private final boolean LOGGING;
	private final boolean CHEAT;
    private int MAXTIME;
    private long STARTTIME;
    private boolean USE_CACHE = true;
    private static final int MAXEVENTS = 1000;
    //higher C -> more exploration less exploitation
    static final double C = 1.0;

    //store the top 10000 most used nodes
    private final CacheNode cache = null;// = new CacheNode(10000);

    public MCTSAI() {
        this(false, true);
    }

    public MCTSAI(boolean printLog, boolean cheat) {
        LOGGING = printLog;
        CHEAT = cheat;
    }

    private void log(final String message) {
        if (LOGGING) {
            System.err.println(message);
        }
    }
    
    private void logi(final int num) {
        if (LOGGING) {
            System.err.print(num);
        }
    }
    
    private void logc(final char message) {
        if (LOGGING) {
            System.err.print(message);
        }
    }


    private void addNode(final long gid, final MCTSGameTree node) {
        if (USE_CACHE) {
            cache.put(gid, node);
        }
    }

    private MCTSGameTree getNode(long gid) {
        if (USE_CACHE && cache.containsKey(gid)) {
            log("root is a cache hit");
            return cache.get(gid);
        } else {
            return new MCTSGameTree(-1, -1);
        }
    }
    
    private MCTSGameTree getNode(final long gid, final int choice, final int evalScore) {
        final MCTSGameTree node = getNode(gid);
        if (node.getEvalScore() == evalScore && node.getChoice() == choice) {
            return node;
        } else {
            return new MCTSGameTree(choice, evalScore);
        }
    }
    
    private double UCT(final boolean isMax, final MCTSGameTree parent, final MCTSGameTree child) {
        return (isMax ? 1.0 : -1.0) * child.getV() + C * Math.sqrt(Math.log(parent.getNumSim()) / child.getNumSim());
    }

    public synchronized Object[] findNextEventChoiceResults(
            final MagicGame game, 
            final MagicPlayer scorePlayer) {
        //ArtificialLevel = number of seconds to run MCTSAI
        MAXTIME = 1000 * game.getArtificialLevel();
        STARTTIME = System.currentTimeMillis();
   
        final String pinfo = "MCTS " + scorePlayer.getIndex() + " (" + scorePlayer.getLife() + ")";
        final List<Object[]> choices = getCR(game, scorePlayer);
        final int size = choices.size();
        
        // No choice results
        if (size == 0) {
            log(pinfo);
            System.err.println("ERROR! MCTS: no choice found at start");
            return null;
        }
    
        // Single choice result
        if (size == 1) {
            log(pinfo + " SINGLE CHOICE");
            return game.map(choices.get(0));
        }
        
        // repeat a number of simulations
        // each simulation does the following
        //   selects a path down the game tree and create a new leaf
        //   score the leaf by doing a random play to the end of the game
        //   update the score of all the ancestors of the leaf
        // return the "best" choice
       
        //root represents the start state
        final MCTSGameTree root = new MCTSGameTree(-1, -1);
        simLengths.clear();
        for (; System.currentTimeMillis() - STARTTIME < MAXTIME; ) {
            //create a new MagicGame for simulation
            final MagicGame start = new MagicGame(game, scorePlayer);
            if (!CHEAT) {
                start.setKnownCards();
            }
            
            //pass in a clone of the state, genNewTreeNode grows the tree by one node
            //and returns the path from the root to the new node
            final List<MCTSGameTree> path = genNewTreeNode(root, start);
            
            // play a simulated game to get score
            // update all nodes along the path from root to new node 
            final double score = randomPlay(start);
            //logc((score > 0.0) ? '.' : 'X');
            for (MCTSGameTree node : path) {
                node.updateScore(score);
            }
        }
        //logc('\n');

        if (root.size() == 0) {
            System.err.println("ERROR! MCTS: root has no children but there are " + size + " choices");
            System.exit(1);
        }

        //select the best choice (child that has the highest secure score)
        final MCTSGameTree first = root.first();
        int maxV = first.getNumSim();
        int bestC = first.getChoice();
        final List<ArtificialChoiceResults> achoices = getACR(choices);
        for (MCTSGameTree node : root) {
            achoices.get(node.getChoice()).worker = (int)(node.getV() * 100);
            achoices.get(node.getChoice()).gameCount = node.getNumSim();
            final int V = node.getNumSim();
            final int C = node.getChoice();
            if (V > maxV) { 
                maxV = V;
                bestC = C;
            }
        }
        
        final long duration = System.currentTimeMillis() - STARTTIME;
        log("MCTS:  time: " + duration + "  sims:  " + root.getNumSim());

        if (LOGGING) {
            int minL = 1000000;
            int maxL = -1;
            int sumL = 0;
            for (int len : simLengths) {
                sumL += len;
                if (len > maxL) maxL = len;
                if (len < minL) minL = len;
            }
            log("min: " + minL + "  max: " + maxL + "  avg: " + (sumL / simLengths.size()));
        }
        
        if (LOGGING) {
            log(pinfo);
            final ArtificialChoiceResults selected = (bestC >= 0) ? achoices.get(bestC) : null;
            for (final ArtificialChoiceResults achoice : achoices) {
                log((achoice == selected ? "* ":"  ") + achoice);
            }
        }

        return game.map(choices.get(bestC));
    }

    private List<Object[]> getCR(final MagicGame game, final MagicPlayer player) {
        final MagicGame choiceGame = new MagicGame(game, player);
        final MagicEvent event = choiceGame.getNextEvent();
        return event.getArtificialChoiceResults(choiceGame);
    }

    private List<ArtificialChoiceResults> getACR(final List<Object[]> choices) {
        final List<ArtificialChoiceResults> aiChoiceResultsList = 
            new ArrayList<ArtificialChoiceResults>();
        for (final Object choiceResults[] : choices) {
            aiChoiceResultsList.add(new ArtificialChoiceResults(choiceResults));
        }
        return aiChoiceResultsList;
    }

    // p is parent of n
    // n.nb is how many times the node n is simulated
    // sum of nb in all children of parent of n (same as p.nb)
    // select node n (child of node) that maximize v[n]
    // where v[n] = 1 - n.value/n.nb + C * sqrt(log(nb) / n.nb)
    // find a path from root to an unexplored node
    private List<MCTSGameTree> genNewTreeNode(final MCTSGameTree root, final MagicGame game) {
        final List<MCTSGameTree> path = new LinkedList<MCTSGameTree>();
        boolean cached = false;
        MCTSGameTree curr = root;
        path.add(curr);

        for (List<Object[]> choices = getNextMultiChoiceEvent(game, curr != root, false);
             choices != null;
             choices = getNextMultiChoiceEvent(game, curr != root, false)) {
          
            final MagicEvent event = game.getNextEvent();
            final boolean isMax = game.getScorePlayer() == event.getPlayer();

            //cache the first non root node along the path that belongs to AI
            /*
            if (curr != root && isMax && !cached) {
                addNode(game.getGameId(), curr);
                cached = true;
            }
            */
            
            if (curr.size() < choices.size()) {
                //there are unexplored children of node
                //assume we explore children of a node in increasing order of the choices
                game.executeNextEvent(choices.get(curr.size()));
                final MCTSGameTree child = new MCTSGameTree(curr.size(), game.getScore());
                curr.addChild(child);
                path.add(child);
                return path;
            } else {
                while (curr.size() > choices.size()) {
                    System.err.println("ERROR! MCTS: invalid node removed");
                    curr.removeLast();
                }

                //curr.size() == choices.size()
                if (curr.size() == 0) {
                    System.err.println("ERROR! MCTS: curr has size 0, choices has size " + choices.size());
                    System.exit(1);
                }

                final List<MCTSGameTree> invalid = new LinkedList<MCTSGameTree>();
                
                MCTSGameTree child = curr.first();
                double bestV = UCT(isMax, curr, child); 
                for (MCTSGameTree node : curr) {
                    if (node.getChoice() >= choices.size()) {
                        invalid.add(node);
                        continue;
                    }
                    final double v = UCT(isMax, curr, node);
                    if (v > bestV) {
                        bestV = v;
                        child = node;
                    }
                }

                //move down the tree
                curr = child;
                
                game.executeNextEvent(choices.get(curr.getChoice()));
                path.add(curr);
            }
        } 
       
        return path;
    }

    private double randomPlay(final MagicGame game) {
        final List<Object[]> elist = getNextMultiChoiceEvent(game, true, true);
        final double events = (Integer)(elist.get(0)[0]);
      
        if (game.getLosingPlayer() == null) {
            return 0;
        } else if (game.getLosingPlayer() == game.getScorePlayer()) {
            return  -(1.0 - events/MAXEVENTS);
        } else {
            return  1.0 - events/MAXEVENTS;
        }
    }
    
    private List<Object[]> getNextMultiChoiceEvent(MagicGame game, boolean fastChoices, boolean sim) {
        game.setFastChoices(fastChoices);
        
        int events = 0;
        
        // simulate game until it is finished or simulated 300 events
        while (!game.isFinished() && events < MAXEVENTS) {
            if (!game.hasNextEvent()) {
                game.getPhase().executePhase(game);
                continue;
            }

            //game has next event
            final MagicEvent event = game.getNextEvent();

            if (!event.hasChoice()) {
                game.executeNextEvent(MagicEvent.NO_CHOICE_RESULTS);
                continue;
            }
            
            //event has choice

            if (sim) {
                //get simulation choice and execute
                Object[] choice = null;
                try {
                    choice = event.getSimulationChoiceResult(game);
                } catch (OutOfMemoryError err) {
                    System.err.println(err.getMessage());
                    err.printStackTrace();
                    System.exit(1);
                }
                if (choice == null) {
                    System.err.println("ERROR! MCTS: no choice found during sim");
                    game.executeNextEvent(null);
                } else {
                    game.executeNextEvent(choice);
                    events++;
                }
            } else {
                //get list of possible AI choices
                final List<Object[]> choices = event.getArtificialChoiceResults(game);
                final int size = choices.size();
                if (size == 0) {
                    //invalid game state
                    System.err.println("ERROR! MCTS: no choice found");
                    game.executeNextEvent(null);
                } else if (size == 1) {
                    game.executeNextEvent(choices.get(0));
                } else {
                    //multiple choice
                    return choices;
                }
            }
        }
        
        if (LOGGING) {
            simLengths.add(events);
        }

        //game is finished
        if (sim) {
            return Collections.<Object[]>singletonList(new Object[]{events}); 
        } else {
            return null;
        }
    }
}

//only store one copy of MagicGame
//each tree node stores the choice from the parent that leads to this node
//so we only need one copy of MagicGame for MCTSAI
class MCTSGameTree implements Iterable<MCTSGameTree> {
    
    private final int choice;
    private final LinkedList<MCTSGameTree> children = new LinkedList<MCTSGameTree>();
    private int numSim = 0;
    private double score = 0;
    private int evalScore = 0;

    public MCTSGameTree(int choice, int evalScore) {
        this.evalScore = evalScore;
        this.choice = choice;
    }
    
    public void updateScore(final double score) {
        this.score += score;
        numSim += 1;
    }

    public int getChoice() {
        return choice;
    }

    public int getEvalScore() {
        return evalScore;
    }

    public double getScore() {
        return score;
    }
    
    public int getNumSim() {
        return numSim;
    }

    public double getV() {
        return score / numSim;
    }
    
    public double getSecureScore() {
        return getV() + 1.0/Math.sqrt(getNumSim());
    }

    public void addChild(MCTSGameTree child) {
        children.add(child);
    }
    
    public void removeLast() {
        children.removeLast();
    }
    
    public MCTSGameTree first() {
        return children.get(0);
    }
    
    public Iterator<MCTSGameTree> iterator() {
        return children.iterator();
    }

    public int size() {
        return children.size();
    }
}


class CacheNode extends LinkedHashMap<Long, MCTSGameTree> {
	private static final long serialVersionUID = 1L;
    private final int capacity;
    public CacheNode(int capacity) {
        super(capacity + 1, 1.1f, true);
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > capacity;
    }
}
