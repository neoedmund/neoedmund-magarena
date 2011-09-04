package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicDamage;
import magic.model.MagicCard;
import magic.model.event.MagicEvent;

public abstract class MagicWhenDrawnTrigger extends MagicTrigger<MagicCard> {
    public MagicWhenDrawnTrigger(final int priority) {
        super(priority); 
	}
	
	public MagicWhenDrawnTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDrawn;
    }
}
