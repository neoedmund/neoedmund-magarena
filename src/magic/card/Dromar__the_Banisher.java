package magic.card;

import magic.model.MagicColor;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicColorChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


import java.util.Collection;

public class Dromar__the_Banisher {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
			return (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            "You may pay {2}{U}.",
                            new MagicPayManaCostChoice(MagicManaCost.TWO_BLUE),
                            MagicColorChoice.UNSUMMON_INSTANCE),
                        new Object[]{player},
                        this,
                        "You may$ pay {2}{U}$. If you do, choose a color$. " + 
                        "Return all creatures of that color to their owner's hand."):
                 MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicColor color=(MagicColor)choiceResults[2];
				final Collection<MagicTarget> targets=
                    game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE);
				for (final MagicTarget target : targets) {
					final MagicPermanent creature=(MagicPermanent)target;
					if (color.hasColor(creature.getColorFlags(game))) {
						game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
					}
				}
			}			
		}
    };
}
