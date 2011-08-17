package magic.ai;

import magic.model.MagicGame;
import magic.model.MagicPlayer;

public class MiniMaxAlphaBetaAI implements MagicAI {
	
    private final boolean CHEAT;
    
    public MiniMaxAlphaBetaAI() {
        this(false);
    }

    public MiniMaxAlphaBetaAI(final boolean cheat) {
        CHEAT = cheat;
    }

    public Object[] findNextEventChoiceResults(final MagicGame game,final MagicPlayer player) {
    	final ArtificialWorkerPool aiWorkerPool = new ArtificialWorkerPool(game, player);
        aiWorkerPool.setCheat(CHEAT);
        return aiWorkerPool.findNextEventChoiceResults();
    }
}
