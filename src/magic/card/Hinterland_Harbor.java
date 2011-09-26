package magic.card;

import magic.model.MagicSubType;
import magic.model.trigger.MagicTappedIntoPlayUnlessTrigger;
import magic.model.trigger.MagicTrigger;

public class Hinterland_Harbor {
    public static final MagicTrigger T = new MagicTappedIntoPlayUnlessTrigger(MagicSubType.Forest,MagicSubType.Island);
}