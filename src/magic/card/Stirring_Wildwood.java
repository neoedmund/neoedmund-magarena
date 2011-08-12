package magic.card;

import magic.model.*;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;
import magic.model.event.*;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.variable.MagicDummyLocalVariable;

import java.util.Arrays;
import java.util.EnumSet;

public class Stirring_Wildwood {

	public static final MagicPermanentActivation V3115 =new MagicPermanentActivation(			"Stirring Wildwood",
            new MagicCondition[]{new MagicArtificialCondition(
					MagicManaCost.ONE_GREEN_WHITE.getCondition(),
                    MagicManaCost.GREEN_GREEN_WHITE_WHITE.getCondition())},
			new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.ONE_GREEN_WHITE)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},this,
                    "Until end of turn, Stirring Wildwood becomes a 3/4 green and white Elemental creature with reach. It's still a land.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,
                final Object[] data,final Object[] choiceResults) {
			game.doAction(new MagicBecomesCreatureAction((MagicPermanent)data[0],
	new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.power=3;
			pt.toughness=4;
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return flags|MagicAbility.Reach.getMask();
		}
		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
            final EnumSet<MagicSubType> mod = flags.clone();
            mod.add(MagicSubType.Elemental);
            return mod;
		}
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
			return flags|MagicType.Creature.getMask();
		}
				
		@Override
		public int getColorFlags(final MagicPermanent permanent,final int flags) {
			return MagicColor.Green.getMask()|MagicColor.White.getMask();
		}		
	}));
		}
	};
	
    public static final MagicTrigger V9903 =new MagicTappedIntoPlayTrigger("Stirring Wildwood");
    
    public static final MagicManaActivation V1 = new MagicTapManaActivation(
            Arrays.asList(MagicManaType.Green,MagicManaType.White), 1);
    
    public static final MagicChangeCardDefinition SET = new MagicChangeCardDefinition() {
        @Override
        public void change(MagicCardDefinition cdef) {
		    cdef.setExcludeManaOrCombat();
        }
    };

}
