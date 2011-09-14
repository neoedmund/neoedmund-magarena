package magic.model.action;

import magic.model.MagicGame;
import magic.model.trigger.MagicPermanentTrigger;

import java.util.List;

public class MagicCleanupTurnTriggersAction extends MagicAction {

	private List<MagicPermanentTrigger> removedTriggers;
	
	@Override
	public void doAction(final MagicGame game) {
		removedTriggers=game.removeTurnTriggers();		
	}

	@Override
	public void undoAction(final MagicGame game) {
		game.addTurnTriggers(removedTriggers);
	}
}
