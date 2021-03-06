package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;


public class MagicSoulshiftTrigger extends MagicWhenPutIntoGraveyardTrigger {
	
	private final int cmc;
	
	public MagicSoulshiftTrigger(final int cmc) {
		this.cmc = cmc;
	}
	
    @Override
    public MagicEvent executeTrigger(
    		final MagicGame game,
    		final MagicPermanent permanent,
    		final MagicGraveyardTriggerData triggerData) {
    	if (MagicLocationType.Play == triggerData.fromLocation) {
			final MagicPlayer player = permanent.getController();
			final MagicTargetFilter targetFilter =
					new MagicTargetFilter.MagicCMCTargetFilter(
	                MagicTargetFilter.TARGET_SPIRIT_CARD_FROM_GRAVEYARD,
	                MagicTargetFilter.MagicCMCTargetFilter.LESS_THAN_OR_EQUAL,
	                cmc);
			final MagicTargetChoice targetChoice = 
					new MagicTargetChoice(
	                targetFilter,false,MagicTargetHint.None,
	                "a Spirit card from your graveyard)");
			final MagicChoice mayChoice = 
	        		new MagicMayChoice(
	        		player + " may return target Spirit card with " +
	        		"converted mana cost " + cmc + " or less " +
	                "from his or her graveyard to his or her hand.",
	        		targetChoice);
            return new MagicEvent(
                    permanent,
                    player,
                    mayChoice,
                    new MagicGraveyardTargetPicker(false),
                    MagicEvent.NO_DATA,
                    this,
                    player + " may$ return target Spirit card$ with " +
    		        "converted mana cost " + cmc + " or less " +
            		"from his or her graveyard to his or her hand.");
		}
		return MagicEvent.NONE;
    }
    @Override
	public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
		if (MagicMayChoice.isYesChoice(choiceResults[0])) {
            event.processTargetCard(game,choiceResults,1,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicRemoveCardAction(
                    		card,
                    		MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(
                    		card,
                    		MagicLocationType.Graveyard,
                    		MagicLocationType.OwnersHand));
                }
            });
        }
	}
}
