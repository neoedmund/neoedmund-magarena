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

public class Guardian_Seraph {

    public static final MagicTrigger V7500 =new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,"Guardian Seraph",5) {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final int amount=damage.getAmount();
			if (!damage.isUnpreventable()&&amount>0&&damage.getSource().getController()!=player&&damage.getTarget()==player) {
				// Prevention effect.
				damage.setAmount(amount-1);
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };

}