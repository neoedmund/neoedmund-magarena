package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

public class Goblin_Bombardment {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
	    new MagicCondition[] { MagicCondition.ONE_CREATURE_CONDITION },
	    new MagicActivationHints(MagicTiming.Removal), "Damage") {

	@Override
	public MagicEvent[] getCostEvent(final MagicSource source) {
	    return new MagicEvent[] { new MagicSacrificePermanentEvent(source,
		    source.getController(),
		    MagicTargetChoice.SACRIFICE_CREATURE) };
	}

	@Override
	public MagicEvent getPermanentEvent(final MagicPermanent source,
		final MagicPayedCost payedCost) {
	    return new MagicEvent(source, source.getController(),
		    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
		    new MagicDamageTargetPicker(1), new Object[] { source },
		    this, source.getCard()
			    + " deals 1 damage to target creature or player$.");
	}

	@Override
	public void executeEvent(final MagicGame game, final MagicEvent event,
		final Object[] data, final Object[] choiceResults) {
	    event.processTarget(game, choiceResults, 0,
		    new MagicTargetAction() {
			public void doAction(final MagicTarget target) {
			    final MagicDamage damage = new MagicDamage(
				    (MagicSource) data[0], target, 1, false);
			    game.doAction(new MagicDealDamageAction(damage));
			}
		    });
	}
    };
}
