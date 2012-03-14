package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public abstract class MagicPutIntoPlayAction extends MagicAction {

	private MagicPermanent permanent = MagicPermanent.NONE;
	private MagicPermanent enchantedPermanent = MagicPermanent.NONE;

	@Override
	public void doAction(final MagicGame game) {
		permanent=createPermanent(game);
		final int score=ArtificialScoringSystem.getTurnScore(game)-permanent.getStaticScore(game);
		
		final MagicPlayer controller=permanent.getController();
		controller.addPermanent(permanent);
				
		if (enchantedPermanent.isValid()) {
			enchantedPermanent.addAura(permanent);
			permanent.setEnchantedCreature(enchantedPermanent);			
		}

        game.addTriggers(permanent);
        game.addCardStatics(permanent);
	
        //execute come into play triggers
		for (final MagicTrigger trigger : permanent.getCardDefinition().getComeIntoPlayTriggers()) {
			game.executeTrigger(trigger,permanent,permanent,permanent.getController());
		}

        //execute other come into player triggers
		game.executeTrigger(MagicTriggerType.WhenOtherComesIntoPlay,permanent);
		
		setScore(controller,permanent.getScore(game)+permanent.getStaticScore(game)+score);
		
		game.checkLegendRule(permanent);
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {
		if (enchantedPermanent.isValid()) {			
			enchantedPermanent.removeAura(permanent);
			permanent.setEnchantedCreature(MagicPermanent.NONE);
		}
		permanent.getController().removePermanent(permanent);
		game.removeTriggers(permanent);
		game.removeCardStatics(permanent);
	}
	
	void setEnchantedPermanent(final MagicPermanent aEnchantedPermanent) {
		enchantedPermanent = aEnchantedPermanent;
	}
	
	protected abstract MagicPermanent createPermanent(final MagicGame game);
	
	public MagicPermanent getPermanent() {
		return permanent;
	}
	
	@Override
	public String toString() {
        if (enchantedPermanent.isValid()) {
    		return getClass().getSimpleName()+" ("+permanent+','+enchantedPermanent+')';
        } else { 
    		return getClass().getSimpleName()+" ("+permanent+')';
        }
	}
}
