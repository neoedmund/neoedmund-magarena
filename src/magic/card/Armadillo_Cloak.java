package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Armadillo_Cloak {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            final int amount = damage.getDealtAmount();
			return (permanent.getEnchantedCreature() == damage.getSource()) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,amount},
                    this,
                    player + "gains " + amount + " life."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction(
					(MagicPlayer)data[0],
					(Integer)data[1]));
		}
    };
}
