package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.event.MagicLevelUpActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Coralhelm_Commander {
	public static final MagicStatic S = new MagicStatic(
			MagicLayer.ModPT, 
			MagicTargetFilter.TARGET_MERFOLK_YOU_CONTROL) {

		private int amount = 0;

		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.add(amount, amount);
		}
		@Override
		public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			amount = source.getCounters(MagicCounterType.Charge) >= 4 ? 1:0;
			return source != target;
		}
	};
	    
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
		@Override
		public void getPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
			final int charges = permanent.getCounters(MagicCounterType.Charge);
			if (charges >= 4) {
				pt.set(4,4);
			} else if (charges >= 2) {
				pt.set(3,3);
			}
		}
    };

    public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
		@Override
		public long getAbilityFlags(
                final MagicGame game,
                final MagicPermanent permanent,
                final long flags) {
			if (permanent.getCounters(MagicCounterType.Charge) >= 2) {
				return flags|MagicAbility.Flying.getMask();
			}
			return flags;
		}
    };

	public static final MagicPermanentActivation A = new MagicLevelUpActivation(MagicManaCost.ONE,4);
}
