package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Curiosity {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.POS_TARGET_CREATURE,
			MagicPumpTargetPicker.getInstance());
	
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
			return (damage.getSource() == permanent.getEnchantedCreature() &&
					damage.getTarget().isPlayer() &&
					damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may draw a card.",
                                MagicSimpleMayChoice.DRAW_CARDS,
                                1,
                                MagicSimpleMayChoice.DEFAULT_NONE),
                        new Object[]{player},
                        this,
                        player + " may$ draw a card.") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
			}
		}
    };
}