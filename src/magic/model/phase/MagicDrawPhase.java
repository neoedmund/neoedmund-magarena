package magic.model.phase;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicExcludeEvent;

public class MagicDrawPhase extends MagicPhase {

	private static final MagicPhase INSTANCE=new MagicDrawPhase();

	public MagicDrawPhase() {
		super(MagicPhaseType.Draw);
	}
	
	public static MagicPhase getInstance() {
		return INSTANCE;
	}

	@Override
	public void executeBeginStep(final MagicGame game) {
        //skip draw phase for first turn
        if (game.getTurn() == 1) {
			game.setStep(MagicStep.NextPhase);
            return;
        }

        final MagicPlayer player=game.getTurnPlayer();
        game.doAction(new MagicDrawAction(player,1));
        game.setStep(game.canSkip() ? MagicStep.NextPhase : MagicStep.ActivePlayer);

        // Determines what the purpose is for permanents that can attack,
        // block or produce mana. Do this after draw, could be a land card.
        if (player.getPlayerDefinition().isArtificial()) {
            game.addEvent(new MagicExcludeEvent(player));
        }
	}
}
