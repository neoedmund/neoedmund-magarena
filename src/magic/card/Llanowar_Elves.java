
package magic.card;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;

import java.util.Arrays;

public class Llanowar_Elves {
    public static final MagicManaActivation V1 = 
        new MagicTapManaActivation(Arrays.asList(MagicManaType.Colorless, MagicManaType.Green), 1);
}
