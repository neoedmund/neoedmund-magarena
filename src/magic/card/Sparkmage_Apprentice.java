package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;


public class Sparkmage_Apprentice {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(1),
                    new Object[]{permanent},
                    this,
                    permanent + " deals 1 damage to target creature or player$.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
				public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage = new MagicDamage((MagicPermanent)data[0],creature,1,false);
                    game.doAction(new MagicDealDamageAction(damage));
                }
			});
		}
    };
}