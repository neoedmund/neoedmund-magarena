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

public class Rakdos_Guildmage {

	public static final MagicPermanentActivation V1564 =new MagicPermanentActivation(            "Rakdos Guildmage",
			new MagicCondition[]{MagicCondition.HAS_CARD_CONDITION,MagicManaCost.THREE_BLACK.getCondition()},
            new MagicActivationHints(MagicTiming.Removal,true),
            "-2/-2") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
				new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE_BLACK),
				new MagicDiscardEvent(source,source.getController(),1,false)
			};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicWeakenTargetPicker(2,2),
                    MagicEvent.NO_DATA,
                    this,
                    "Target creature$ gets -2/-2 until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicChangeTurnPTAction(creature,-2,-2));
			}
		}
	};

	public static final MagicPermanentActivation V1598 =new MagicPermanentActivation(			"Rakdos Guildmage",
            new MagicCondition[]{MagicManaCost.THREE_RED.getCondition()},
            new MagicActivationHints(MagicTiming.Token,true),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.THREE_RED)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(source,player,new Object[]{player},this,
				"Put a 2/1 red Goblin creature token with haste onto the battlefield. Exile it at end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			final MagicCard card=MagicCard.createTokenCard(TokenCardDefinitions.GOBLIN2_TOKEN_CARD,player);
			game.doAction(new MagicPlayCardAction(card,player,MagicPlayCardAction.REMOVE_AT_END_OF_TURN));
		}
	};
	
}
