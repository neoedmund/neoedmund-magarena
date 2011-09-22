package magic.card;

import magic.model.trigger.MagicRefugeLandTrigger;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicTrigger;

public class Jwar_Isle_Refuge {
    public static final MagicTrigger T1 = new MagicTappedIntoPlayTrigger();
	public static final MagicTrigger T2 = new MagicRefugeLandTrigger();
}
