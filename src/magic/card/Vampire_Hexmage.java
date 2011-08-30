package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.*;
import magic.model.target.MagicCountersTargetPicker;
import magic.model.condition.MagicCondition;
import magic.model.action.MagicPermanentAction;

public class Vampire_Hexmage {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            MagicCondition.NONE,
            new MagicActivationHints(MagicTiming.Removal),
            "Remove") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.TARGET_PERMANENT,
                    MagicCountersTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Remove all counters from target permanent$.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent permanent) {
                    for (final MagicCounterType counterType : MagicCounterType.values()) {
                        final int amount=permanent.getCounters(counterType);
                        if (amount>0) {
                            game.doAction(new MagicChangeCountersAction(permanent,counterType,-amount,true));
                        }
                    }
                }
			});
		}
	};
}
