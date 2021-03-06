package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class Stronghold_Overseer {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{MagicManaCost.BLACK_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,false,1),
            "Pump") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
					new MagicPayManaCostEvent(
							source,
							source.getController(),
							MagicManaCost.BLACK_BLACK),
					new MagicPlayAbilityEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicEvent.NO_DATA,
                    this,
                    "Creatures with shadow get +1/+0 until end of turn and " +
                    "creatures without shadow get -1/-0 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			Collection<MagicTarget> targets =
					game.filterTargets(game.getPlayer(0),MagicTargetFilter.TARGET_CREATURE_WITH_SHADOW);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,1,0));
			}
			targets = game.filterTargets(game.getPlayer(0),MagicTargetFilter.TARGET_CREATURE_WITHOUT_SHADOW);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,-1,0));
			}
		}
	};
}
