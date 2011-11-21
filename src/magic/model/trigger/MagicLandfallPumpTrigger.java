package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class MagicLandfallPumpTrigger extends MagicWhenOtherComesIntoPlayTrigger {

    private static final MagicLandfallPumpTrigger INSTANCE = new MagicLandfallPumpTrigger();

    private MagicLandfallPumpTrigger() {}

    public static final MagicLandfallPumpTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
        final MagicPlayer player = permanent.getController();
        return (player == played.getController() && played.isLand()) ?
            new MagicEvent(
                permanent,
                player,
                new Object[]{permanent},
                this,
                permanent + " gets +2/+2 until end of turn."):
            MagicEvent.NONE;
    }
    
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
        game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,2));
    }		
}