package magic.card;

import magic.model.*;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicPlayAbilityAction;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;

import java.util.Collection;
import java.util.EnumSet;

public class Mirror_Entity {

	public static final MagicPermanentActivation V1326 = new MagicPermanentActivation(            "Mirror Entity",
			new MagicCondition[]{MagicManaCost.X.getCondition()},
            new MagicActivationHints(MagicTiming.Pump,true,1),
            "X/X"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.X)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final int x=payedCost.getX();
			return new MagicEvent(source,source.getController(),new Object[]{source,x},this,
				"Creatures you control become "+x+"/"+x+" and gain all creature types until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,
                final Object[] data,final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
            final Integer X = (Integer)data[1];
			final MagicLocalVariable localVariable=
	        new MagicDummyLocalVariable() {

		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=X;
			pt.toughness=X;
		}

		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            final EnumSet<MagicSubType> mod = flags.clone();
            mod.addAll(MagicSubType.ALL_CREATURES);
			return mod;
	    }
    };
			final Collection<MagicTarget> creatures=game.filterTargets(
                    permanent.getController(),
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
			for (final MagicTarget creature : creatures) {
				game.doAction(new MagicBecomesCreatureAction((MagicPermanent)creature,localVariable));
			}
			game.doAction(new MagicPlayAbilityAction(permanent));
		}
	};
	
}
