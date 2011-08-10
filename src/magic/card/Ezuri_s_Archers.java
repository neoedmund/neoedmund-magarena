package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Ezuri_s_Archers {

    public static final MagicTrigger V7251 =new MagicTrigger(MagicTriggerType.WhenBlocks,"Ezuri's Archers") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPermanent blocked=permanent.getBlockedCreature();
				if (blocked!=null&&blocked.hasAbility(game,MagicAbility.Flying)) {
					return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Ezuri's Archers gets +3/+0 until end of turn.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],3,0));
		}
    };
    
}
