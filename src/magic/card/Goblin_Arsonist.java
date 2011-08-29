package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Goblin_Arsonist {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			return (MagicLocationType.Play == triggerData.fromLocation) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new MagicMayChoice(
                        		"You may deal 1 damage to target creature or player.",
                                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER),
                        new MagicDamageTargetPicker(1),
                        new Object[]{permanent},
                        this,
                        "You may$ deal 1 damage to target creature or player$") :
                null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicTarget target = event.getTarget(game,choiceResults,1);
				if (target != null) {
					final MagicDamage damage = new MagicDamage((MagicPermanent)data[0],target,1,false);
					game.doAction(new MagicDealDamageAction(damage));
				}
			}
		}
    };
}