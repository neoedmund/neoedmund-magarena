package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Hissing_Miasma {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPermanent creature=(MagicPermanent)data;
			final MagicPlayer controller=creature.getController();
			return (controller!=permanent.getController()) ?
                new MagicEvent(
                        permanent,
                        controller,
                        new Object[]{controller},
                        this,
                        controller.getName() + " lose 1 life."):
                null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],-1));
		}
    };
}
