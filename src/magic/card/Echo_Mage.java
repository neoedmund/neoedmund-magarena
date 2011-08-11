package magic.card;

import magic.model.*;
import magic.model.action.MagicCopyCardOnStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.stack.MagicCardOnStack;
import magic.model.variable.MagicDummyLocalVariable;
import magic.model.variable.MagicLocalVariable;
import magic.model.variable.MagicStaticLocalVariable;

public class Echo_Mage {
	
    private static final MagicLocalVariable ECHO_MAGE=new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			final int charges=permanent.getCounters(MagicCounterType.Charge);
			if (charges>=4) {
				pt.power=2;
				pt.toughness=5;
			} else if (charges>=2) {
				pt.power=2;
				pt.toughness=4;
			}
		}		
	};

	public static final MagicPermanentActivation V615 = new MagicLevelUpActivation("Echo Mage",MagicManaCost.ONE_BLUE,4);
	
	public static final MagicPermanentActivation V617 = new MagicPermanentActivation("Echo Mage",
			new MagicCondition[]{
                MagicCondition.TWO_CHARGE_COUNTERS_CONDITION,
                MagicCondition.CAN_TAP_CONDITION,MagicManaCost.BLUE_BLUE.getCondition()},
			new MagicActivationHints(MagicTiming.Spell),
            "Copy"
            ) {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.BLUE_BLUE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			final int amount=source.getCounters(MagicCounterType.Charge)>=4?2:1;
			final String description = amount == 2 ?
					"Copy target instant or sorcery spell$ twice. You may choose new targets for the copies.":
					"Copy target instant or sorcery spell$. You may choose new targets for the copy.";
			return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_SPELL,
                    new Object[]{player,amount},
                    this,
                    description);
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicCardOnStack targetSpell=event.getTarget(game,choiceResults,0);
			if (targetSpell!=null) {
				final MagicPlayer player=(MagicPlayer)data[0];
				final int amount=(Integer)data[1];
				for (int count=amount;count>0;count--) {
					game.doAction(new MagicCopyCardOnStackAction(player,targetSpell));
				}
			}
		}
	};
	
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
            cdef.addLocalVariable(ECHO_MAGE);	
            cdef.addLocalVariable(MagicStaticLocalVariable.getInstance());
            cdef.setVariablePT();
        }
    };
}
