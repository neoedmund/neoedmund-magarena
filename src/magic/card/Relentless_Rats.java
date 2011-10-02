package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Relentless_Rats {
	public static final MagicStatic S = new MagicStatic(MagicLayer.ModPT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final MagicTargetFilter targetFilter = 
                    new MagicTargetFilter.NameTargetFilter(permanent.getName());	
			final int size = game.filterTargets(game.getPlayer(0),targetFilter).size() - 1;
				pt.add(size,size);
			
		}		
	};
}
