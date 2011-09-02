package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicAttachEquipmentAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Ronin_Warclub {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
			final MagicPlayer player=permanent.getController();
            return (otherPermanent.isCreature()&&otherPermanent.getController()==player) ?
				new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,otherPermanent},
                        this,
                         "Attach " + permanent + " to " + otherPermanent + ".") :
                null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicAttachEquipmentAction((MagicPermanent)data[0],(MagicPermanent)data[1]));
		}
    };
}
