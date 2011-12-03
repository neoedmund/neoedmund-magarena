package magic.test;

import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicDuel;
import magic.model.phase.MagicMainPhase;

class TestMCTSCrash extends TestGameBuilder {    
    public MagicGame getGame() {
		final MagicDuel duel=new MagicDuel();
		duel.setDifficulty(6);
		
		final MagicPlayerProfile profile=new MagicPlayerProfile("bgruw");
		final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
		final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
		duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
		duel.setStartPlayer(0);
        duel.setAIs(new MagicAI[]{null, MagicAIImpl.MCTS.getAI()});
		
		final MagicGame game=duel.nextGame(true);
		game.setPhase(MagicMainPhase.getFirstInstance());
		final MagicPlayer player=game.getPlayer(0);
		final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
		createPermanent(game,P,"Rupture Spire",false,8);
		createPermanent(game,P,"Sword of Feast and Famine",false,2);
		createPermanent(game,P,"Marisi's Twinclaws",false, 2);
       

        P = opponent;
		
        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        addToHand(P, "Plains", 10);
		createPermanent(game,P,"Rupture Spire",false,8);
		createPermanent(game,P,"Mogg Fanatic", false, 1);
		createPermanent(game,P,"Knight of Meadowgrain", false, 2);
		
		return game;
    }
}
