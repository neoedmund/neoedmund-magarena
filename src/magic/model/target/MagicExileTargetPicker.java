package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicExileTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE=new MagicExileTargetPicker();
	
	private MagicExileTargetPicker() {
		
	}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent=(MagicPermanent)target;
		return permanent.getScore(game);
	}
	
	public static MagicTargetPicker getInstance() {
		
		return INSTANCE;
	}
}