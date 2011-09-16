package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicLayer;
import magic.model.MagicAbility;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;

public class Razorjaw_Oni {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_BLACK_CREATURE) {
        @Override
        public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
            return flags | MagicAbility.CannotBlock.getMask();
        }
    };
}