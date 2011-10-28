package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicSpecterTrigger;
import magic.model.trigger.MagicTrigger;

public class Guul_Draz_Specter {
	public static final MagicStatic S1 = new MagicStatic(MagicLayer.ModPT) {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (game.getOpponent(permanent.getController()).getHand().isEmpty()) {
				pt.add(3,3);
			}
		}		
	};

    public static final MagicTrigger T = new MagicSpecterTrigger(true,false,false);
}
