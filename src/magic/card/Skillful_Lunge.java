package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicFirstStrikeTargetPicker;

public class Skillful_Lunge {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(
				final MagicCardOnStack cardOnStack,
				final MagicPayedCost payedCost) {
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicFirstStrikeTargetPicker.create(),
                    new Object[]{cardOnStack},
                    this,
                    "Target creature$ gets +2/+0 and gains " +
                    "first strike until end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,2,0));
                    game.doAction(new MagicSetAbilityAction(
                    		creature,
                    		MagicAbility.FirstStrike));
                }
			});
		}
	};
}
