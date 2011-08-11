package magic.card;

import magic.model.*;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicTapTargetPicker;

public class Merfolk_Seastalkers {

	public static final MagicPermanentActivation V1292 =new MagicPermanentActivation(            "Merfolk Seastalkers",
			new MagicCondition[]{MagicManaCost.TWO_BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Tapping),
            "Tap") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_WITHOUT_FLYING,
                    new MagicTapTargetPicker(true,false),
				    MagicEvent.NO_DATA,
                    this,
                    "Tap target creature$ without flying.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null&&!creature.isTapped()) {
				game.doAction(new MagicTapAction(creature,true));
			}
		}
	};
		
}
