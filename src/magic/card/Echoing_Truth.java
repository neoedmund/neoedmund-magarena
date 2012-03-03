package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import java.util.Collection;

public class Echoing_Truth {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    MagicTargetChoice.NEG_TARGET_NONLAND_PERMANENT,
                    MagicBounceTargetPicker.getInstance(),
                    new Object[]{cardOnStack},
                    this,
                    "Return target nonland permanent$ and all other " +
                    "permanents with the same name as that permanent " +
                    "to their owners' hands.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent targetPermanent) {
                    final MagicTargetFilter targetFilter = 
                        new MagicTargetFilter.NameTargetFilter(targetPermanent.getName());
                    final Collection<MagicTarget> targets = 
                        game.filterTargets(cardOnStack.getController(),targetFilter);
                    for (final MagicTarget target : targets) {
                    	game.doAction(new MagicRemoveFromPlayAction(
                    			(MagicPermanent)target,
                    			MagicLocationType.OwnersHand));
                    }
                }
			});
		}
	};
}
