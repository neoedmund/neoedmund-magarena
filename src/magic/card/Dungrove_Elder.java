package magic.card;

import java.util.Collection;

import magic.model.*;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Dungrove_Elder {

	private static final MagicLocalVariable DUNGROVE_ELDER = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final Collection<MagicTarget> targets =
                    game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_FOREST_YOU_CONTROL);
			pt.power = targets.size();
			pt.toughness = targets.size();
		}
	};
	
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(DUNGROVE_ELDER);	
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.setVariablePT();
        }
    };
}