package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicPayDelayedCostAction;

/**
 * Delayed mana costs can give wrong scores when a mana source:
 * - taps to produces mana and then attacks or blocks : not possible
 * - produced mana and then leaves play : reduces the available mana incorrectly
 */
public class MagicDelayedPayManaCostResult implements MagicPayManaCostResult {

	private final MagicManaCost cost;
	private final int x;
	
	MagicDelayedPayManaCostResult(final MagicManaCost cost,final int x) {
		this.cost=cost;
		this.x=x;
	}
	
	public MagicManaCost getCost() {
		return cost;
	}

    public String toString() {
        return cost.getText() + x;
    }

	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getConverted() {
		return x+cost.getConvertedCost();
	}

	@Override
	public void doAction(final MagicGame game,final MagicPlayer player) {
		game.doAction(new MagicPayDelayedCostAction(player,this));
	}
}
