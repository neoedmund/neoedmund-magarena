package magic.test;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicDuel;
import magic.model.phase.MagicMainPhase;

class TestSelfSac extends TestGameBuilder {    
    public MagicGame getGame() {
        final MagicDuel duel=new MagicDuel();
        duel.setDifficulty(6);
        
        final MagicPlayerProfile profile=new MagicPlayerProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
        duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        duel.setStartPlayer(0);
        //duel.setAIs(new MagicAI[]{null, new MCTSAI(true, true)});
        
        final MagicGame game=duel.nextGame(true);
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = opponent;

        P.setLife(2);
        addToLibrary(P,"Swamp",10);
        createPermanent(game,P,"Swamp",false,4);
        createPermanent(game,P,"Fallen Angel",false,1);

        P = player;
        
        P.setLife(5);
        addToLibrary(P,"Mountain",10);
        createPermanent(game,P,"Mountain",false,4);
        addToHand(P,"Act of Treason",1);
        
        return game;
    }
}
