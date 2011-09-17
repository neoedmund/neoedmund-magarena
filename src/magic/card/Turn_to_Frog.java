package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSubType;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetTurnColorAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicBecomeTargetPicker;
import magic.model.variable.MagicDummyLocalVariable;

import java.util.EnumSet;

// Backed out because 'loses all abilities' can't be implemented at the moment
//
//>Turn to Frog
//image=http://magiccards.info/scans/en/m12/78.jpg
//value=3
//removal=3
//rarity=U
//type=Instant
//color=u
//converted=2
//cost={1}{U}
//timing=removal
public class Turn_to_Frog {
    private static final MagicDummyLocalVariable LV = new MagicDummyLocalVariable() {
		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.set(1,1);
		}
		@Override
		public long getAbilityFlags(final MagicGame game,final MagicPermanent permanent,final long flags) {
			return 0;
		}
		@Override
		public EnumSet<MagicSubType> getSubTypeFlags(final MagicPermanent permanent,final EnumSet<MagicSubType> flags) {
			return EnumSet.of(MagicSubType.Frog);
		}
	};

	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    cardOnStack.getCard(),
                    cardOnStack.getController(),
                    MagicTargetChoice.TARGET_CREATURE,
                    new MagicBecomeTargetPicker(1,1,false),
                    new Object[]{cardOnStack},
                    this,
					"Target creature$ loses all abilities " +
					"and becomes a 1/1 blue Frog until end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicBecomesCreatureAction(creature,LV));
                    game.doAction(new MagicSetTurnColorAction(creature,MagicColor.Blue));
                }
			});
		}
	};
}
