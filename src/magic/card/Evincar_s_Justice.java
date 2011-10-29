package magic.card;

import java.util.Collection;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicBuybackChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Evincar_s_Justice {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new MagicBuybackChoice(MagicManaCost.THREE),
                    new Object[]{cardOnStack},
                    this,
                    cardOnStack.getCard() + " deals 2 damage to each creature " +
                    "and each player. If the buyback cost was payed$, " +
                    "return " + cardOnStack + " to its owner's hand as it resolves.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			final MagicSource source = cardOnStack.getCard();
			final Collection<MagicTarget> targets = 
                game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				final MagicDamage damage = new MagicDamage(source,target,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			for (final MagicPlayer player : game.getPlayers()) {
				final MagicDamage damage = new MagicDamage(source,player,2,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			if (MagicBuybackChoice.isYesChoice(choiceResults[1])) {
				game.doAction(new MagicMoveCardAction(
						cardOnStack.getCard(),
						MagicLocationType.Stack,
						MagicLocationType.OwnersHand));
			} else {
				game.doAction(new MagicMoveCardAction(cardOnStack));
			}
		}
	};
}
