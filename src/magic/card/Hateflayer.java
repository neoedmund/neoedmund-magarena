package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Hateflayer {

	public static final MagicPermanentActivation V1104 =new MagicPermanentActivation(            "Hateflayer",
			new MagicCondition[]{MagicCondition.CAN_UNTAP_CONDITION,MagicManaCost.TWO_RED.getCondition()},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_RED),
				new MagicUntapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(5),
                    new Object[]{source},
                    this,
                    "Hateflayer deals damage equal to its power to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicPermanent permanent=(MagicPermanent)data[0];
				final MagicDamage damage=new MagicDamage(permanent,target,permanent.getPower(game),false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};

}