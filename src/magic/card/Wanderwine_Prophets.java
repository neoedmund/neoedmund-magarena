package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeExtraTurnsAction;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Wanderwine_Prophets {
    public static final MagicWhenDamageIsDealtTrigger T3 = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			return (damage.getSource() == permanent &&
					damage.getTarget().isPlayer() &&
					damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new MagicMayChoice(
                            "You may sacrifice a Merfolk. " +
                            "If you do, take an extra turn after this one.",
                            MagicTargetChoice.SACRIFICE_MERFOLK),
                        MagicSacrificeTargetPicker.getInstance(),
                        new Object[]{permanent.getController()},
                        this,
                        "You may$ sacrifice a Merfolk$. " +
                        "If you do, take an extra turn after this one"):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
	                public void doAction(final MagicPermanent creature) {
	                    game.doAction(new MagicSacrificeAction(creature));
	                    game.doAction(new MagicChangeExtraTurnsAction((MagicPlayer)data[0],1));
	                }
				});
		    } 
        }
    };
}
