package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDrawAction;

public class MagicDrawEvent extends MagicEvent {
	
	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choices) {
			game.doAction(new MagicDrawAction((MagicPlayer)data[0],(Integer)data[1]));		
		}
	};
		
	public MagicDrawEvent(final MagicSource source,final MagicPlayer player,final int amount) {
		super(
			source,
			player,
			new Object[]{player,amount},
			EVENT_ACTION,
			player.getName() + genDescription(amount)
		);
	}
	
	private static final String genDescription(final int amount) {
		if (amount!=1) {
			return " draws "+amount+" cards.";
		} else {
			return " draws a card.";
		}
	}
}
