package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicWhenDrawnTrigger;

public class Spiteful_Visions {
	public static final Object T1 = Rites_of_Flourishing.T3;
    
    public static final MagicWhenDrawnTrigger T2 = new MagicWhenDrawnTrigger() {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard data) {
    		final MagicPlayer player = data.getOwner();
    			return new MagicEvent(
    					permanent,
    					permanent.getController(),
    					new Object[]{permanent,player},
    					this,
    					permanent + " deals 1 damage to " + player + ".");
    	}
    	
    	@Override
    	public void executeEvent(
    			final MagicGame game,
    			final MagicEvent event,
    			final Object data[],
    			final Object[] choiceResults) {
    		final MagicDamage damage = new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],1,false);
    		game.doAction(new MagicDealDamageAction(damage));
    	}		
    };
}
