package magic.card;

import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicPumpTargetPicker;

public class Volcanic_Strength {
	public static final MagicSpellCardEvent E = new MagicPlayAuraEvent(
			MagicTargetChoice.POS_TARGET_CREATURE,MagicPumpTargetPicker.create());
}
