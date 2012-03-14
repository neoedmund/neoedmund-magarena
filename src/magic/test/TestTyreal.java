package magic.test;

import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicDuel;
import magic.model.action.MagicChangeCountersAction;
import magic.model.phase.MagicMainPhase;

class TestTyreal extends TestGameBuilder {
    public MagicGame getGame() {
	final MagicDuel duel = new MagicDuel();
	duel.setDifficulty(6);

	final MagicPlayerProfile profile = new MagicPlayerProfile("bgruw");
	final MagicPlayerDefinition player1 = new MagicPlayerDefinition("Player", false, profile, 15);
	final MagicPlayerDefinition player2 = new MagicPlayerDefinition("Computer", true, profile, 14);
	duel.setPlayers(new MagicPlayerDefinition[] { player1, player2 });
	duel.setStartPlayer(0);
	duel.setAIs(new MagicAI[]{null, MagicAIImpl.MCTS.getAI()});

	final MagicGame game = duel.nextGame(true);
	game.setPhase(MagicMainPhase.getFirstInstance());
	final MagicPlayer player = game.getPlayer(0);
	final MagicPlayer opponent = game.getPlayer(1);

	MagicPlayer P = player;

	P.setLife(20);
	addToLibrary(P, "Plains", 10);
	createPermanent(game, P, "Rupture Spire", false, 10);
	//createPermanent(game, P, "Goblin Bombardment", false, 1);
	//createPermanent(game, P, "Jayemdae Tome", false, 1);
	//createPermanent(game, P, "Mad Auntie", false, 1);
	//createPermanent(game, P, "Ib Halfheart, Goblin Tactician", false, 1);
	//createPermanent(game, P, "Boggart Shenanigans", false, 1);
	//createPermanent(game, P, "Knucklebone Witch", false, 1);
	//createPermanent(game, P, "Strangleroot Geist", false, 1);
	//createPermanent(game, P, "Vorapede", false, 1);
	addToHand(P, "Dungeon Geists", 1);
	addToHand(P, "Torch Fiend", 1);
	addToHand(P, "Disenchant", 1);

	P = opponent;

	P.setLife(1);
	addToLibrary(P, "Plains", 10);
	createPermanent(game, P, "Rupture Spire", false, 10);
	createPermanent(game, P, "Mad Auntie", false, 1);
	MagicPermanent la = createPermanent(game, P, "Legacy's Allure", false, 1);
	game.doAction(new MagicChangeCountersAction(la, MagicCounterType.Charge, 1, true));
	//game.doAction(new MagicChangeCountersAction(la, MagicCounterType.Charge, 1, true));
	//createPermanent(game, P, "Mad Auntie", false, 1);
	// createPermanent(game,P,"Jayemdae Tome",false,1);

	return game;
    }
}
