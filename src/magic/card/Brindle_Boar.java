package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.*;

public class Brindle_Boar {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
            null,
            new MagicActivationHints(MagicTiming.Removal),
            "Life+4") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicSacrificeEvent((MagicPermanent)source)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player = source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    player + " gains 4 life.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],4));
		}
	};
}