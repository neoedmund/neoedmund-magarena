package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicMappable;
import magic.model.MagicPermanent;

public class MagicSourceManaActivationResult implements MagicMappable {

	private final MagicPermanent permanent;
	private final MagicManaActivation activation;
	
	public MagicSourceManaActivationResult(final MagicPermanent permanent,final MagicManaActivation activation) {
		this.permanent=permanent;
		this.activation=activation;
	}

	@Override
	public MagicSourceManaActivationResult map(final MagicGame game) {
		return new MagicSourceManaActivationResult(permanent.map(game),activation);
	}

	public void doActivation(final MagicGame game) {
		for (final MagicEvent costEvent : activation.getCostEvent(permanent)) {
			// Mana activation cost events do not have choices.
			game.executeEvent(costEvent,MagicEvent.NO_CHOICE_RESULTS); 
		}
	}

    @Override
    public long getId() {
        return permanent.getId() * 31 + activation.hashCode();
    }
}
