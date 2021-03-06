package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicCopyTargetPicker;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class Followed_Footsteps {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPlayer player=permanent.getController();
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			return (player==data && enchantedCreature.isValid()) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent,player},
                    this,
                    player + " puts a token that's a copy of enchanted creature onto the battlefield."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicPermanent enchantedCreature=permanent.getEnchantedCreature();
			if (enchantedCreature.isValid()) {
				game.doAction(new MagicPlayTokenAction((MagicPlayer)data[1],enchantedCreature.getCardDefinition()));
			}
		}		
    };
}
