package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicAttachEquipmentAction;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;


public class Deathrender {
	public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			return (permanent.getEquippedCreature() == data) ?
				new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new MagicMayChoice(
                            "You may put a creature card from your hand onto the " +
                            "battlefield. and attach " + permanent + " to it.",
                            MagicTargetChoice.TARGET_CREATURE_CARD_FROM_HAND),
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{permanent.getController(),permanent},
                    this,
                    "You may$ put a creature card$ from your hand onto the battlefield " +
                    "and attach " + permanent + " to it."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetCard(game,choiceResults,1,new MagicCardAction() {
					public void doAction(final MagicCard card) {
						game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
						final MagicPlayCardAction action = new MagicPlayCardAction(card,(MagicPlayer)data[0],MagicPlayCardAction.NONE);
						game.doAction(action);
						MagicPermanent creature = action.getPermanent();
						if (!creature.hasAbility(game, MagicAbility.Shroud)) {
							game.doAction(new MagicAttachEquipmentAction((MagicPermanent)data[1],creature));
						}
					}
				});
			}
		}
    };
}
