package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.action.MagicChangePlayerStateAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;

public class Exhaustion {

	public static final MagicSpellCardEvent V5518 =new MagicSpellCardEvent("Exhaustion") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.TARGET_OPPONENT,
				new Object[]{cardOnStack},this,"Creatures and lands target opponent$ controls don't untap during that opponent's next untap step.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPlayer player=event.getTarget(game,choiceResults,0);
			if (player!=null) {
				game.doAction(new MagicChangePlayerStateAction(player,MagicPlayerState.Exhausted,true));
			}
		}
	};
	
}
