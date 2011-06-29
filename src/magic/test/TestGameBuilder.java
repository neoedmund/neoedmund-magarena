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
		if (cardDefinition!=null) {
			for (int c=count;c>0;c--) {
				
				player.addCardToHand(new MagicCard(cardDefinition,player,currentId.incrementAndGet()));
			}
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
			game.getStack().removeAllItems();
			permanent.clearState(MagicPermanentState.Summoned);
			if (tapped) {
				permanent.setState(MagicPermanentState.Tapped);
			} 
		}
		return lastPermanent;
	}

    public MagicGame getGame() {
        return null;
    }
	
	public static MagicGame buildGame(final String id) {
        MagicGame game = null;
        try {
            TestGameBuilder gb = (TestGameBuilder)Class.forName("magic.test." + id).newInstance();
            game = gb.getGame();
        } catch (final Exception ex) {
            System.err.println("ERROR! Unable to build game " + id);
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(1); 
        }
        return game;
	}
}
