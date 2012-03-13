package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Sensor_Splicer {
	public static final MagicStatic S = new MagicStatic(
			MagicLayer.Ability, 
			MagicTargetFilter.TARGET_GOLEM_YOU_CONTROL) {
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags | MagicAbility.Vigilance.getMask();
		}
	};
    
    public static final Object T = Blade_Splicer.T;
}
