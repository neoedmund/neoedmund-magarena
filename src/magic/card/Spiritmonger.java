package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPlayAbilityAction;
import magic.model.action.MagicSetTurnColorAction;
import magic.model.choice.MagicColorChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicTiming;

public class Spiritmonger {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.GREEN.getCondition()},
			new MagicActivationHints(MagicTiming.Pump,false,1),
            "Color") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),
                		MagicManaCost.GREEN),
                new MagicPlayAbilityEvent((MagicPermanent)source)};
		}
		@Override
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicColorChoice.BLUE_RED_WHITE_INSTANCE,
                    new Object[]{source},
                    this,
                    source + " becomes the color$ of your choice until end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			game.doAction(new MagicSetTurnColorAction(permanent,(MagicColor)choiceResults[0]));
			game.doAction(new MagicPlayAbilityAction(permanent));
		}
	};
}
