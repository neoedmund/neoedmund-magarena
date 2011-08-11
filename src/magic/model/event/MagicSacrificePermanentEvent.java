package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicSacrificeTargetPicker;

public class MagicSacrificePermanentEvent extends MagicEvent {

	private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicPermanent permanent=event.getTarget(game,choiceResults,0);
			if (permanent!=null) {
				game.doAction(new MagicSacrificeAction(permanent));
			}
		}
	};
	
	public MagicSacrificePermanentEvent(final MagicSource source,final MagicPlayer player,final MagicTargetChoice targetChoice) {
		
		super(source,player,targetChoice,MagicSacrificeTargetPicker.getInstance(),MagicEvent.NO_DATA,EVENT_ACTION,
			"Choose "+targetChoice.getTargetDescription()+"$.");
	}	
}