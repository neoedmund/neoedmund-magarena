package magic.model.phase;

import magic.data.SoundEffects;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.action.MagicCombatDamageAction;

public class MagicCombatDamagePhase extends MagicPhase {

	private static final MagicPhase INSTANCE=new MagicCombatDamagePhase();

	private MagicCombatDamagePhase() {
		super(MagicPhaseType.CombatDamage);
	}

	public static MagicPhase getInstance() {
		return INSTANCE;
	}

	@Override
	public void executeBeginStep(final MagicGame game) {
		final MagicPlayer attackingPlayer=game.getTurnPlayer();
		final MagicPlayer defendingPlayer=game.getOpponent(attackingPlayer);
		final int lifeBefore=defendingPlayer.getLife();
		final int poisonBefore=defendingPlayer.getPoison();
		final String playerName = defendingPlayer.getName();
        
  		//deal first strike damage
        if (game.getStep() == MagicStep.Begin) {
            game.doAction(new MagicCombatDamageAction(attackingPlayer,defendingPlayer, true));
        } else {
        //deal regular damage
            game.doAction(new MagicCombatDamageAction(attackingPlayer,defendingPlayer, false));
        }

		final int lifeAfter=defendingPlayer.getLife();
		final int poisonAfter=defendingPlayer.getPoison();
		final StringBuilder message=new StringBuilder();
		if (lifeAfter>lifeBefore) {
			message.append(" gains ").append(lifeAfter-lifeBefore).append(" life.");
		} else if (lifeAfter<lifeBefore) {
			message.append(" loses ").append(lifeBefore-lifeAfter).append(" life.");
		}
		if (poisonAfter>poisonBefore) {
			message.append(" gets ").append(poisonAfter-poisonBefore).append(" poison counters.");
		}
		if (message.length()>0) {
			game.logMessage(defendingPlayer,"{c}" + playerName + message.toString());			
		}

        if (game.getStep() == MagicStep.Begin) {
            game.setStep(MagicStep.ActivePlayer);
        } else {
            SoundEffects.playClip(game,SoundEffects.COMBAT_SOUND);
        }
	}	
	
    protected void executeEndOfPhase(final MagicGame game) {
        executeBeginStep(game);
    }
}
