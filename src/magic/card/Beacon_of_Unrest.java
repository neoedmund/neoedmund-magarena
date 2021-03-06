package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.action.MagicShuffleIntoLibraryAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

public class Beacon_of_Unrest {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			final MagicCard card=cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    MagicTargetChoice.TARGET_ARTIFACT_OR_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                    new MagicGraveyardTargetPicker(true),
                    new Object[]{card,player},
                    this,
                    "Return target artifact or creature card$ from a graveyard onto the battlefield under your control. "+
                    "Shuffle " + card + " into its owner's library.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicReanimateAction((MagicPlayer)data[1],targetCard,MagicPlayCardAction.NONE));
                }
			});
            game.doAction(new MagicShuffleIntoLibraryAction((MagicCard)data[0]));
		}
	};
}
