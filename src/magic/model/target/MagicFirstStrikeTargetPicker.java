package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicFirstStrikeTargetPicker extends MagicTargetPicker<MagicPermanent> {
	
	private static final MagicFirstStrikeTargetPicker INSTANCE=new MagicFirstStrikeTargetPicker();

	private MagicFirstStrikeTargetPicker() {}
	
	public static MagicFirstStrikeTargetPicker create() {
		return INSTANCE;
	}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		if (permanent.getController()!=player) {
			return -50-permanent.getPower(game);
		}
		final long flags=permanent.getAllAbilityFlags(game);
		if (MagicAbility.FirstStrike.hasAbility(flags)||MagicAbility.DoubleStrike.hasAbility(flags)) {
			return 0;
		}
		final int power=permanent.getPower(game);
		if (permanent.isBlocked()||permanent.isBlocking()) {
			return power+permanent.getBlockingCreatures().size()+100;
		} 
		if (permanent.canTap(game)) {
			return power+50;
		} 
		return power+1;
	}
}
