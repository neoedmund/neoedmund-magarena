package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Favor_of_the_Woods {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.getInstance());
	
    public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
    	@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPermanent creature) {
			final MagicPlayer player = permanent.getController();
            return (creature == permanent.getEnchantedCreature()) ?
            	new MagicEvent(
            			permanent,
            			player,
            			new Object[]{player},
            			this,
            			player + " gains 3 life.") :
            MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],3));
		}
    };
}