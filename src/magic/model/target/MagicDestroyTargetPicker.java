package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicDestroyTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private final boolean noRegeneration;

	public MagicDestroyTargetPicker(final boolean noRegeneration) {
		this.noRegeneration=noRegeneration;
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		if (permanent.hasAbility(game,MagicAbility.Indestructible)) {
			return 0;
		}
		if (permanent.isRegenerated()&&!noRegeneration) {
			return 0;
		}
		final int score=permanent.getScore(game);
		return permanent.getController()==player?-score:score;
	}
}
