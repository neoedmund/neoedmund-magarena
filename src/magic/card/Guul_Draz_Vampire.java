package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;

public class Guul_Draz_Vampire {
	public static final MagicLocalVariable GUUL_DRAZ_VAMPIRE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			if (game.getOpponent(permanent.getController()).getLife()<=10) {
				pt.add(2,1);
			}
		}	
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return game.getOpponent(permanent.getController()).getLife()<=10 ? 
                flags|MagicAbility.Intimidate.getMask():
                flags;
		}
	};
}
