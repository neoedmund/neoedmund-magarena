package magic.model.target;

import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;

public class MagicEquipTargetPicker extends MagicTargetPicker<MagicPermanent> {
	
	private final long abilityFlags;
	private final boolean defensive;
	
	public MagicEquipTargetPicker(final MagicCardDefinition cardDefinition) {
		this.abilityFlags=cardDefinition.getAbilityFlags();
		this.defensive=cardDefinition.getToughness()>cardDefinition.getPower();
	}
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		// Penalty when allready equipped.
		int penalty=permanent.isEquipped()?3:0;
		// Penalty when there is an overlap between abilities.
		if ((permanent.getAllAbilityFlags(game)&abilityFlags)!=0) {
			penalty+=6;
		}
		final MagicPowerToughness pt=permanent.getPowerToughness(game);
		// Defensive
		if (defensive) {
			return 20-pt.toughness-penalty;
		}
		// Offensive
		return 1+pt.power*2-pt.toughness-penalty;
	}
}
