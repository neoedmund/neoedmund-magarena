package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicUntapAction;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Collection;

public class Sword_of_Feast_and_Famine {

    public static final MagicTrigger V9734 = new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Sword of Feast and Famine") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent.getEquippedCreature() && damage.getTarget().isPlayer() && damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				final MagicPlayer damagedPlayer=(MagicPlayer)damage.getTarget();
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player,damagedPlayer},
                        this,
                        damagedPlayer + " discards a card and you untap all lands you control.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.addEvent(new MagicDiscardEvent((MagicPermanent)data[0],(MagicPlayer)data[2],1,false));
			final Collection<MagicTarget> targets = 
                game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_LAND_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicUntapAction((MagicPermanent)target));
			}
		}
    };
    
}
