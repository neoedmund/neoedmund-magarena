package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Boros_Guildmage {

	public static final MagicPermanentActivation V197 =new MagicPermanentActivation(			"Boros Guildmage",
            new MagicCondition[]{MagicManaCost.ONE_RED.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true),
            "Haste"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicHasteTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Target creature$ gains haste until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
			}
		}
	};

	public static final MagicPermanentActivation V226 =new MagicPermanentActivation(			"Boros Guildmage",
            new MagicCondition[]{MagicManaCost.ONE_WHITE.getCondition()},
            new MagicActivationHints(MagicTiming.Block,true),
            "First strike"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {

			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {

			return new MagicEvent(source,source.getController(),MagicTargetChoice.POS_TARGET_CREATURE,MagicFirstStrikeTargetPicker.getInstance(),
				MagicEvent.NO_DATA,this,"Target creature$ gains first strike until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
			}
		}	
	};
	
}