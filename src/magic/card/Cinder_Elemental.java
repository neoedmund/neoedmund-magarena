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

public class Cinder_Elemental {

	public static final MagicPermanentActivation V390 =new MagicPermanentActivation(            "Cinder Elemental",
			new MagicCondition[]{
                MagicCondition.CAN_TAP_CONDITION,
                MagicManaCost.X_RED.getCondition()
            },
            new MagicActivationHints(MagicTiming.Removal),
            "Damage"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.X_RED),
				new MagicSacrificeEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final int amount=payedCost.getX();
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(amount),
                    new Object[]{source,amount},
                    this,
                    "Cinder Elemental deals "+amount+" damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,(Integer)data[1],false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
}