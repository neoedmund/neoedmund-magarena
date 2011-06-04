package magic.model.event;

import magic.model.MagicGame;
import magic.model.stack.MagicItemOnStack;

public class MagicStackGetChoicesEvent extends MagicEvent {

	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			
			final MagicItemOnStack itemOnStack=(MagicItemOnStack)data[0];
			itemOnStack.setChoiceResults(choiceResults);
			// Pay mana cost when there is one.
			event.payManaCost(game,itemOnStack.getController(),choiceResults);
		}
	};

	public MagicStackGetChoicesEvent(final MagicItemOnStack itemOnStack) {
		
		super(itemOnStack.getSource(),itemOnStack.getController(),itemOnStack.getEvent().getChoice(),
				itemOnStack.getEvent().getTargetPicker(),new Object[]{itemOnStack},EVENT_ACTION,null);
	}
}