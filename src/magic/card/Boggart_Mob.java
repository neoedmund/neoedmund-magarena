package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Boggart_Mob {
    public static final MagicWhenDamageIsDealtTrigger T3 = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final MagicPlayer player = permanent.getController();
			final MagicSource source = damage.getSource();
			return (damage.isCombat() && 
					damage.getTarget().isPlayer() &&
					source.getController() == player &&
					((MagicPermanent)source).hasSubType(MagicSubType.Goblin,game)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may put a 1/1 black Goblin Rogue " +
                                		"creature token onto the battlefield.",
                                MagicSimpleMayChoice.PLAY_TOKEN,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{player},
                        this,
                        player + " may$ put a 1/1 black Goblin Rogue " +
                        "creature token onto the battlefield."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.getInstance().getTokenDefinition("Goblin Rogue1")));
			}
		}
    };
}
