package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDeathtouchTargetPicker;

public class Onyx_Mage {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.ONE_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Deathtouch"
            ) {
		
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_BLACK)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
					source,
					source.getController(),
					MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
					MagicDeathtouchTargetPicker.getInstance(),
					MagicEvent.NO_DATA,
					this,
					"Target creature$ you control gains deathtouch until end of turn.");
		}

		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object[] data,
				final Object[] choiceResults) {
			event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
			    public void doAction(final MagicPermanent creature) {
			    	game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Deathtouch));
			    }
			});
		}
	};
}
