package magic.card;

import magic.model.MagicManaCost;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;

public class Cavern_Thoctar {
	public static final MagicPermanentActivation A = new MagicPumpActivation(MagicManaCost.ONE_RED,1,0);
}