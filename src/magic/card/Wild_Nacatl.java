package magic.card;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

import java.util.Collection;

public class Wild_Nacatl {

	private static final MagicLocalVariable LV = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final Collection<MagicTarget> targets1 =
                    game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_MOUNTAIN_YOU_CONTROL);
			if (targets1.size() > 0) {
				pt.power ++;
				pt.toughness ++;
			}
			
			final Collection<MagicTarget> targets2 =
                    game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_PLAINS_YOU_CONTROL);
			if (targets2.size() > 0) {
				pt.power ++;
				pt.toughness ++;
			}	
		}
	};
	
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.addLocalVariable(LV);	
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.setVariablePT();
        }
    };
}