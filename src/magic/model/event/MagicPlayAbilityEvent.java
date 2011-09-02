package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicPlayAbilityAction;

public class MagicPlayAbilityEvent extends MagicEvent {

	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			
			game.doAction(new MagicPlayAbilityAction((MagicPermanent)data[0]));
		}
	};
	
	public MagicPlayAbilityEvent(final MagicSource source) {
		
		super(source,source.getController(),new Object[]{source},EVENT_ACTION,null);
	}
}