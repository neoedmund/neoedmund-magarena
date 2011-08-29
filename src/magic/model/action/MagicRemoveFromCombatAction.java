package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPermanentState;

public class MagicRemoveFromCombatAction extends MagicAction {

	private final MagicPermanent permanent;
	private boolean attacking;
	private boolean blocking;
	private MagicPermanentList blockingCreatures;
	private MagicPermanent blockedCreature;
	
	public MagicRemoveFromCombatAction(final MagicPermanent permanent) {
		
		this.permanent=permanent;
	}

	@Override
	public void doAction(final MagicGame game) {

		attacking=permanent.hasState(MagicPermanentState.Attacking);
		if (attacking) {
			game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.Attacking,false));
			game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.Blocked,false));
			blockingCreatures=new MagicPermanentList(permanent.getBlockingCreatures());
			permanent.removeBlockingCreatures();
			for (final MagicPermanent blockingCreature : blockingCreatures) {

				blockingCreature.setBlockedCreature(null);
			}
		} else {
			blocking=permanent.hasState(MagicPermanentState.Blocking);
			if (blocking) {
				game.doAction(new MagicChangeStateAction(permanent,MagicPermanentState.Blocking,false));
				blockedCreature=permanent.getBlockedCreature();
				if (blockedCreature!=null) {
					permanent.setBlockedCreature(null);
					blockingCreatures=new MagicPermanentList(blockedCreature.getBlockingCreatures());
					blockedCreature.removeBlockingCreature(permanent);
				}
			}
		}
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {

		if (attacking) {
			permanent.setBlockingCreatures(blockingCreatures);
			for (final MagicPermanent blockingCreature : blockingCreatures) {

				blockingCreature.setBlockedCreature(permanent);
			}
		} else if (blocking) {
			permanent.setBlockedCreature(blockedCreature);
			if (blockedCreature!=null) {
				blockedCreature.setBlockingCreatures(blockingCreatures);
			}
		}
	}
}