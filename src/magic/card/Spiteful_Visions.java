package magic.card;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;

public class Spiteful_Visions {
	public static final MagicTrigger T1 = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player = (MagicPlayer)data;
				return new MagicEvent(
						permanent,
						player,
						new Object[]{player},
						this,
						"You draw a card.");
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			final MagicPlayer player = (MagicPlayer)data[0];
			game.doAction(new MagicDrawAction(player,1));
		}
    };
    
    public static final MagicTrigger T2 = new MagicTrigger(MagicTriggerType.WhenDrawn) {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
    		final MagicPlayer player = ((MagicCard)data).getOwner();
    			return new MagicEvent(
    					permanent,
    					permanent.getController(),
    					new Object[]{permanent,player},
    					this,
    					"Spiteful Visions deals 1 damage to you.");
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
