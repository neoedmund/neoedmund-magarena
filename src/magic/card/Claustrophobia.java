package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Claustrophobia {
	public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPermanent enchantedCreature = permanent.getEnchantedCreature();
            return (enchantedCreature.isValid()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent},
                        this,
                        "Tap " + enchantedCreature + ". It doesn't " +
                        "untap during its controller's untap step") :
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPermanent enchantedCreature = permanent.getEnchantedCreature();
			if (enchantedCreature.isValid()) {
				game.doAction(new MagicTapAction(enchantedCreature,true));
			}
		}		
    };
}
