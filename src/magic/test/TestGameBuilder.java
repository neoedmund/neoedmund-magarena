package magic.test;

import java.util.concurrent.atomic.AtomicInteger;

import magic.data.CardDefinitions;
import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.model.MagicTournament;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicPutIntoPlayAction;
import magic.model.phase.MagicMainPhase;

public class TestGameBuilder {
	
	private static final AtomicInteger currentId=new AtomicInteger(1);
	
	public static void addToLibrary(final MagicPlayer player,final String name,final int count) {

		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		for (int c=count;c>0;c--) {
			
			player.getLibrary().addToTop(new MagicCard(cardDefinition,player,currentId.getAndIncrement()));
		}
	}
	
	public static void addToGraveyard(final MagicPlayer player,final String name,final int count) {
		
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		for (int c=count;c>0;c--) {
			
			player.getGraveyard().addToTop(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
		}
	}

	public static void addToExile(final MagicPlayer player,final String name,final int count) {
		
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		for (int c=count;c>0;c--) {
			
			player.getExile().addToTop(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
		}
	}
	
	public static void addToHand(final MagicPlayer player,final String name,final int count) {
		
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		for (int c=count;c>0;c--) {
			
			player.addCardToHand(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
		}
	}

	public static void createAllTokens(final MagicGame game,final MagicPlayer player) {
		
		for (final MagicCardDefinition cardDefinition : TokenCardDefinitions.TOKEN_CARDS) {
			
			game.doAction(new MagicPlayTokenAction(player,cardDefinition));
		}
	}
	
	public static MagicPermanent createPermanent(final MagicGame game,final MagicPlayer player,final String name,final boolean tapped,final int count) {

		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		MagicPermanent lastPermanent=null;
		for (int c=count;c>0;c--) {
			
			final MagicCard card=new MagicCard(cardDefinition,player,currentId.getAndIncrement());
			final MagicPermanent permanent=new MagicPermanent(currentId.getAndIncrement(),card,player);
			lastPermanent=permanent;
			
			game.doAction(new MagicPutIntoPlayAction() {
				
				@Override
				protected MagicPermanent createPermanent(final MagicGame game) {

					return permanent;
				}
			});

			game.getEvents().clear();
			permanent.clearState(MagicPermanentState.Summoned);
			if (tapped) {
				permanent.setState(MagicPermanentState.Tapped);
			} 
		}
		return lastPermanent;
	}
	
	public static MagicGame buildGame() {

		final MagicTournament tournament=new MagicTournament();
		tournament.setDifficulty(6);
		
		final MagicPlayerProfile profile=new MagicPlayerProfile("bw");
		final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile,15);
		final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile,14);
		tournament.setPlayers(new MagicPlayerDefinition[]{player1,player2});
		tournament.setStartPlayer(0);
		
		final MagicGame game=tournament.nextGame();
		game.setPhase(MagicMainPhase.getFirstInstance());
		final MagicPlayer player=game.getPlayer(0);
		final MagicPlayer opponent=game.getPlayer(1);

		addToLibrary(player,"Mountain",10);
		addToLibrary(opponent,"Forest",3);
		addToLibrary(opponent,"Mountain",1);
		addToLibrary(opponent,"Plains",1);
		addToLibrary(opponent,"Island",1);
		addToLibrary(opponent,"Swamp",1);		
		addToLibrary(opponent,"Forest",1);				
		addToGraveyard(player,"Mogg Fanatic",1);
		addToExile(player,"Stormfront Pegasus",1);
		addToHand(player,"Vivid Crag",2);
		addToHand(player,"Goblin Bushwhacker",1);
		addToHand(player,"Goblin Chieftain",1);
		addToHand(player,"Char",1);				
		addToHand(player,"Chimeric Mass",1);
		addToHand(player,"Mind Spring",1);								
		addToHand(player,"Remand",1);			
		addToHand(player,"Counterspell",1);					
		addToHand(player,"Draining Whelk",1);
		addToHand(player,"Student of Warfare",1);
		addToHand(player,"Sword of Body and Mind",1);
		addToHand(player,"Szadek, Lord of Secrets",1);		
		createPermanent(game,player,"Crumbling Necropolis",false,10);
		createPermanent(game,player,"Mogg Fanatic",false,1);
		createPermanent(game,player,"Goblin Artillery",false,1);
		createPermanent(game,player,"Akroma, Angel of Wrath",true,1);
		createPermanent(game,opponent,"Bloodrock Cyclops",true,1);
		
		return game;
	}
}