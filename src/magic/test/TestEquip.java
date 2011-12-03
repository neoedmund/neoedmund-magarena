package magic.test;

import magic.ai.MCTSAI;
import magic.ai.MagicAI;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicDuel;
import magic.model.phase.MagicMainPhase;

// demonstrates MiniMax AI also moves equipment between creatures until all mana is spend.
// interesting is how "Strider Harness" shows the bug, but "Barbed Battlegear" does not.
class TestEquip extends TestGameBuilder {    
    public MagicGame getGame() {
		final MagicDuel duel=new MagicDuel();
		duel.setDifficulty(6);
		
		final MagicPlayerProfile profile=new MagicPlayerProfile("bgruw");
		final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
		final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
		duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
		duel.setStartPlayer(1);
		
		// MCTS AI doesn't use the equipment
		//duel.setAIs(new MagicAI[]{null, new MCTSAI(true, false)});
		
		final MagicGame game=duel.nextGame(true);
		game.setPhase(MagicMainPhase.getFirstInstance());
		final MagicPlayer player=game.getPlayer(0);
		final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(20);
        addToLibrary(P, "Forest", 10);
        createPermanent(game,P,"Forest",false,8);
		createPermanent(game,P,"Cylian Elf",false,2);
	    addToHand(P,"Cylian Elf",1); 
	    

        P = opponent;
		
        P.setLife(20);
        addToLibrary(P, "Forest", 10);
        createPermanent(game,P,"Forest",false,8);
		createPermanent(game,P,"Cylian Elf",false,2);
		createPermanent(game,P,"Strider Harness",false,1);
		//createPermanent(game,P,"Barbed Battlegear",false,1);
	    addToHand(P,"Cylian Elf",1); 
		
		return game;
    }
}
