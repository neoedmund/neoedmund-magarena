package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Breath_of_Darigaaz {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.TWO,false),
                    new Object[]{cardOnStack},
                    this,
                    card + " deals 1 damage to each creature without flying and each player. " + 
                    "If " + card + " was kicked$, " + 
                    "it deals 4 damage to each creature without flying and each player instead.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {

			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicSource source=cardOnStack.getSource();
			game.doAction(new MagicMoveCardAction(cardOnStack));
			final int amount=(Integer)choiceResults[1]>0?4:1;
			final Collection<MagicTarget> targets=
                game.filterTargets(cardOnStack.getController(),MagicTargetFilter.TARGET_CREATURE_WITHOUT_FLYING);
			for (final MagicTarget target : targets) {
				final MagicDamage damage=new MagicDamage(source,target,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			for (final MagicPlayer player : game.getPlayers()) {
				final MagicDamage damage=new MagicDamage(source,player,amount,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
}
