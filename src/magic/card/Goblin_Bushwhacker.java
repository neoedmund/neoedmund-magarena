package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Goblin_Bushwhacker {
                        
    private static final MagicEventAction KICKED = new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final Collection<MagicTarget> targets = 
                game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				final MagicPermanent creature=(MagicPermanent)target;
				game.doAction(new MagicChangeTurnPTAction(creature,1,0));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
			}			
		}
	};

	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    new MagicKickerChoice(MagicManaCost.RED,false),
                    new Object[]{cardOnStack},
                    this,
                    "$Play " + card + ". If " + card + " was kicked$, " + 
                    "creatures you control get +1/+0 and gain haste until end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final int kickerCount=(Integer)choiceResults[1];
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack);
			game.doAction(action);
			if (kickerCount>0) {
				final MagicPermanent permanent=action.getPermanent();
				final MagicPlayer player=permanent.getController();
				final MagicEvent triggerEvent=new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        KICKED,
					    "Creatures you control get +1/+0 and gain haste until end of turn.");
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
}
