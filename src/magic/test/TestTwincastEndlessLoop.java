package magic.test;

import magic.model.*;
import magic.model.phase.MagicMainPhase;

// shows the AI can cast Twincast on a Twincast, creating an endless loop.
// repro:
// 1. cast Lightning Bolt, targeting the AI's Diregraf Ghoul
// 2. cast Twincast, targeting the Lightning Bolt effect on the stack
// 3. pass priority
class TestTwincastEndlessLoop extends TestGameBuilder {    
    
    public MagicGame getGame() {
		final MagicDuel duel=new MagicDuel();
		duel.setDifficulty(6);
		
		final MagicPlayerProfile profile=new MagicPlayerProfile("bgruw");
		final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
		final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
		duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
		duel.setStartPlayer(0);
		
		final MagicGame game=duel.nextGame(true);
		game.setPhase(MagicMainPhase.getFirstInstance());
		final MagicPlayer player=game.getPlayer(0);
		final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(6);
        addToLibrary(P, "Plains", 15);
        createPermanent(game,P,"Rupture Spire",false,6);
		createPermanent(game,P,"Wall of Diffusion",false,1);
		createPermanent(game,P,"Eager Cadet",false,1);
		addToHand(P,"Twincast",1);
		addToHand(P,"Benalish Lancer",1);
	    addToHand(P,"Accorder Paladin",1);
	    addToHand(P,"Eager Cadet",1);
	    addToHand(P,"Lightning Bolt",2);
	    addToHand(P,"Swords to Plowshares",1);
       

        P = opponent;
		
        P.setLife(12);
        addToLibrary(P, "Swamp", 15);
		createPermanent(game,P,"Rupture Spire",false,4);
		createPermanent(game,P,"Eager Cadet",false,1);
		createPermanent(game,P,"Diregraf Ghoul",false,1);
	    addToHand(P,"Twincast",1);
		
		return game;
    }
}
