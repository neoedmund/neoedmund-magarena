package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class MagicAttacksPumpTrigger extends MagicWhenAttacksTrigger {
    private final int power;
    private final int toughness;

    public MagicAttacksPumpTrigger(final int power, final int toughness) {
        this.power = power;
        this.toughness = toughness;
    }

    @Override
	public MagicEvent executeTrigger(
			final MagicGame game,
			final MagicPermanent permanent,
			final MagicPermanent creature) {
        return (permanent == creature) ?
            new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent},
                    this,
                    permanent + " gets " + getString(power) + 
                    "/" + getString(toughness) + " until end of turn.") :
            MagicEvent.NONE;
    }
    @Override
    public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
		game.doAction(new MagicChangeTurnPTAction(
				(MagicPermanent)data[0],
				power,
				toughness));
    }
    
    private String getString(final int pt) {
		return pt >= 0 ?
				"+" + pt :
				Integer.toString(pt);
	}
}
