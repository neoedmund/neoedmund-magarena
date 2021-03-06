package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Looter_il_Kor {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final MagicPlayer player = permanent.getController();
			return (damage.getSource() == permanent &&
					damage.getTarget().isPlayer() &&
					damage.getTarget() != player) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player,permanent},
                        this,
                        player + " draws a card, then discards a card."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			game.doAction(new MagicDrawAction(player,1));
			game.addEvent(new MagicDiscardEvent((MagicPermanent)data[1],player,1,false));
		}
    };
}
