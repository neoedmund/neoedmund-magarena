package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicUnblockableTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private static final MagicUnblockableTargetPicker INSTANCE = new MagicUnblockableTargetPicker();
	
	private MagicUnblockableTargetPicker() {}
	
    public static MagicUnblockableTargetPicker create() {
		return INSTANCE;
	}	
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		final MagicPlayer controller=permanent.getController();
		if (game.getTurnPlayer()!=controller||
			permanent.hasAbility(game,MagicAbility.Unblockable)||
			!permanent.canBeBlocked(game,game.getOpponent(controller))) {
			return 0;
		}
		if (permanent.isAttacking()) {
			return 100+permanent.getPower(game);
		}
		return 1+permanent.getPower(game);
	}
}
