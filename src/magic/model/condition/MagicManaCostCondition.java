package magic.model.condition;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicBuilderManaCost;
import magic.model.choice.MagicPayManaCostResultBuilder;

// X must be at least 1.
public class MagicManaCostCondition implements MagicCondition {

	private final MagicManaCost cost;
	
	public MagicManaCostCondition(final MagicManaCost cost) {
		this.cost=cost;
	}

	@Override
	public boolean accept(final MagicGame game,final MagicSource source) {
		final MagicPlayer player=source.getController();
		final MagicBuilderManaCost builderCost;
		if (player.getBuilderCost().isEmpty()) {
			builderCost=cost.getBuilderCost();
		} else {
			builderCost=new MagicBuilderManaCost(player.getBuilderCost());
			cost.addTo(builderCost);
		}		
		return new MagicPayManaCostResultBuilder(game,player,builderCost).hasResults();
	}
}
