package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTapTargetPicker;

public class Backlash {

	public static final MagicSpellCardEvent V3365 =new MagicSpellCardEvent("Backlash") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),
				MagicTargetChoice.NEG_TARGET_UNTAPPED_CREATURE,new MagicTapTargetPicker(true,false),new Object[]{cardOnStack},this,
				"Tap target untapped creature$. That creature deals damage equal to its power to its controller.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicTapAction(creature,true));
				final MagicDamage damage=new MagicDamage(creature,creature.getController(),creature.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
}
