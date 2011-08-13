package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Septic_Rats {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			MagicEvent e = null;
            if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				if (game.getOpponent(player).getPoison()>0) {
					e = new MagicEvent(
                            permanent,
                            player,
                            new Object[]{permanent},
                            this,
                            "Septic Rats gets +1/+1 until end of turn.");
				}
			}
			return e;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],1,1));
		}
    };
}
