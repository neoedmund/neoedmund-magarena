package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicShroudTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private static final MagicTargetPicker INSTANCE=new MagicShroudTargetPicker();
	
	private MagicShroudTargetPicker() {}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		final long flags=permanent.getAllAbilityFlags(game);
		if (MagicAbility.Shroud.hasAbility(flags)||MagicAbility.CannotBeTheTarget.hasAbility(flags)) {
			return 0;
		}
		return permanent.getScore(game);
	}
	
	public static MagicTargetPicker getInstance() {
		return INSTANCE;
	}
}
