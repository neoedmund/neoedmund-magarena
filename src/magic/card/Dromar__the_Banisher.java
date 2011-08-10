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

public class Dromar__the_Banisher {

    public static final MagicTrigger V7199 =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt,"Dromar, the Banisher") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicDamage damage=(MagicDamage)data;
			if (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) {				
				final MagicPlayer player=permanent.getController();
				return new MagicEvent(permanent,player,
	new MagicMayChoice(
			"You may pay {2}{U}.",new MagicPayManaCostChoice(MagicManaCost.TWO_BLUE),MagicColorChoice.UNSUMMON_INSTANCE),new Object[]{player},this,
						"You may$ pay {2}{U}$. If you do, choose a color$. Return all creatures of that color to their owner's hand.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicColor color=(MagicColor)choiceResults[2];
				final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE);
				for (final MagicTarget target : targets) {
					
					final MagicPermanent creature=(MagicPermanent)target;
					if (color.hasColor(creature.getColorFlags())) {
						game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
					}
				}
			}			
		}
    };
    
}