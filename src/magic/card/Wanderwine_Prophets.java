package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeExtraTurnsAction;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicExileTargetPicker;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Wanderwine_Prophets {
	public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			final MagicTargetFilter targetFilter = 
					new MagicTargetFilter.MagicOtherPermanentTargetFilter(
	                MagicTargetFilter.TARGET_MERFOLK_YOU_CONTROL,permanent);
			final MagicTargetChoice targetChoice = 
					new MagicTargetChoice(
	                targetFilter,false,MagicTargetHint.None,"another Merfolk to exile");
	        final MagicChoice championChoice = 
	        		new MagicMayChoice(
	        		"You may exile another Merfolk you control. " +
	        		"If you don't, sacrifice " + permanent + ".",
	        		targetChoice);
			return new MagicEvent(
                    permanent,
                    player,
                    championChoice,
                    MagicExileTargetPicker.getInstance(),
                    new Object[]{permanent},
                    this,
                    "You may$ exile another Merfolk you control$. " +
	        		"If you don't, sacrifice " + permanent + ".");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent = (MagicPermanent)data[0];
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
					public void doAction(final MagicPermanent creature) {
						game.doAction(new MagicExileUntilThisLeavesPlayAction(permanent,creature));
					}
				});
			} else {
				game.doAction(new MagicSacrificeAction(permanent));
			}
		}
    };
    
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent data) {
			if (permanent == data &&
				permanent.getExiledCard() != MagicCard.NONE) {
				final MagicCard exiledCard = permanent.getExiledCard();
				return new MagicEvent(
						permanent,
						permanent.getController(),
						new Object[]{exiledCard,permanent.getController()},
						this,
						"Return " + exiledCard + " to the battlefield");
			}
            return MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicCard exiledCard = (MagicCard)data[0];
			game.doAction(new MagicRemoveCardAction(exiledCard,MagicLocationType.Exile));
			game.doAction(new MagicPlayCardAction(exiledCard,exiledCard.getOwner(),MagicPlayCardAction.NONE));
		}
    };
    
    public static final MagicWhenDamageIsDealtTrigger T3 = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			return (damage.getSource() == permanent &&
					damage.getTarget().isPlayer() &&
					damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new MagicMayChoice(
                            "You may sacrifice a Merfolk. " +
                            "If you do, take an extra turn after this one.",
                            MagicTargetChoice.SACRIFICE_MERFOLK),
                        MagicSacrificeTargetPicker.getInstance(),
                        new Object[]{permanent.getController()},
                        this,
                        "You may$ sacrifice a Merfolk$. " +
                        "If you do, take an extra turn after this one"):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
	                public void doAction(final MagicPermanent creature) {
	                    game.doAction(new MagicSacrificeAction(creature));
	                    game.doAction(new MagicChangeExtraTurnsAction((MagicPlayer)data[0],1));
	                }
				});
		    } 
        }
    };
}
