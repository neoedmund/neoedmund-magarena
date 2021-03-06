package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;

public class MagicTappedIntoPlayTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicTappedIntoPlayTrigger INSTANCE = new MagicTappedIntoPlayTrigger();

    private MagicTappedIntoPlayTrigger() {}

    public static MagicTappedIntoPlayTrigger create() {
        return INSTANCE;
    }

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
		return new MagicEvent(
                permanent,
                permanent.getController(),
                new Object[]{permanent},
                this,
                permanent + " enters the battlefield tapped.");
	}
	
	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
		game.doAction(new MagicTapAction((MagicPermanent)data[0],false));
	}
	
	@Override
	public boolean usesStack() {
		return false;
	}
}
