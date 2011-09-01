package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Roc_Egg {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
			final MagicPlayer player = (permanent != null) ? permanent.getController() : null;
			return (MagicLocationType.Play == triggerData.fromLocation) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    player + " puts a 3/3 white Bird creature token with flying onto the battlefield.") :
                null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.BIRD_TOKEN_CARD));
		}
    };
}
