package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayCardAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Hero_of_Bladehold {

    public static final MagicTrigger V7605 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Hero of Bladehold") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,new Object[]{player},this,
					"You put two 1/1 white Soldier creature tokens onto the battlefield tapped and attacking.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=2;count>0;count--) {
			
				final MagicCard card=MagicCard.createTokenCard(TokenCardDefinitions.SOLDIER_TOKEN_CARD,player);
				game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.TAPPED_ATTACKING));
			}
		}		
    };
    
}
