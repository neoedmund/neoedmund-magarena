package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicMustAttackTargetPicker;

public class Imps__Taunt {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new MagicBuybackChoice(
                    		MagicTargetChoice.NEG_TARGET_CREATURE,
                    		MagicManaCost.THREE),
                    MagicMustAttackTargetPicker.create(),
                    new Object[]{cardOnStack},
                    this,
                    "Target creature$ attacks this turn if able. If the " +
                    "buyback cost was payed$, " +
                    "return " + cardOnStack + " to its owner's hand as it resolves.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			final boolean hasTarget = event.processTargetPermanent(
					game,
					choiceResults,
					0,
					new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.AttacksEachTurnIfAble));
                    if (MagicBuybackChoice.isYesChoice(choiceResults[1])) {
        				game.doAction(new MagicMoveCardAction(
        						cardOnStack.getCard(),
        						MagicLocationType.Stack,
        						MagicLocationType.OwnersHand));
        			} else {
        				game.doAction(new MagicMoveCardAction(cardOnStack));
        			}
                }
			});
			if (!hasTarget) {
				game.doAction(new MagicMoveCardAction(cardOnStack));
			}
		}
	};
}
