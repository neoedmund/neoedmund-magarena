package magic.card;

import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicWhenSpellIsPlayedTrigger;


public class Charmbreaker_Devils {
    public static final MagicAtUpkeepTrigger T1 = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPlayer player = permanent.getController();
			if (player == data) {
				final List<MagicTarget> targets =
	                    game.filterTargets(player,MagicTargetFilter.TARGET_INSTANT_OR_SORCERY_CARD_FROM_GRAVEYARD);
				if (targets.size() > 0) {
					final magic.MersenneTwisterFast rng = 
							new magic.MersenneTwisterFast(permanent.getId() + player.getId());
					final int index = rng.nextInt(targets.size());
					final MagicCard card = (MagicCard)targets.get(index);
					return new MagicEvent(
		                    permanent,
		                    player,
		                    new Object[]{card},
		                    this,
		                    "Return an instant or sorcery card at random " +
		                    "from your graveyard to your hand.");
				}
			}
			return MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicCard card = (MagicCard)data[0];
			game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
			game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
		}
    };
    
    public static final MagicWhenSpellIsPlayedTrigger T2 = new MagicWhenSpellIsPlayedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
			final MagicPlayer player = permanent.getController();
			return (cardOnStack.getController() == player &&
					cardOnStack.getCardDefinition().isSpell()) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent},
                    this,
                    permanent + " gets +4/+0 until end of turn."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],4,0));
		}
    };
}
