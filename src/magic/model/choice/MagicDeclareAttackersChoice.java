package magic.model.choice;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicRandom;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MagicDeclareAttackersChoice extends MagicChoice {

	private static final MagicDeclareAttackersChoice INSTANCE = new MagicDeclareAttackersChoice();
	
	private static final String MESSAGE =
        "Click on a creature to declare as attacker or remove it from combat.|Press {f} to continue.";

	private MagicDeclareAttackersChoice() {
		super("Declare attackers.");
	}
	
	@Override
	public Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
		final MagicDeclareAttackersResultBuilder builder=new MagicDeclareAttackersResultBuilder(game,player);
		return builder.buildResults();
	}
	
    @Override
    public Object[] getSimulationChoiceResult(
			final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
		
        final MagicDeclareAttackersResult result = new MagicDeclareAttackersResult();
		final MagicCombatCreatureBuilder builder = new MagicCombatCreatureBuilder(game,player,game.getOpponent(player));
		builder.buildBlockers();		
		
		if (builder.buildAttackers()) {
			for (final MagicCombatCreature attacker : builder.getAttackers()) {
				if (attacker.hasAbility(MagicAbility.AttacksEachTurnIfAble) ||
				    MagicRandom.nextInt(2) == 1) {
                    //creatures must attack OR
                    //creature has 50% chance of attacking
					result.add(attacker.permanent);
				}
			}		
		}

        return new Object[]{result};
    }
	
	@Override
	public Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) {
		final MagicDeclareAttackersResult result=new MagicDeclareAttackersResult();
		final MagicCombatCreatureBuilder builder=new MagicCombatCreatureBuilder(game,player,game.getOpponent(player));
		builder.buildBlockers();		
		
		final Set<Object> validChoices=new HashSet<Object>();
		if (builder.buildAttackers()) {
			for (final MagicCombatCreature attacker : builder.getAttackers()) {
				if (attacker.hasAbility(MagicAbility.AttacksEachTurnIfAble)) {
					attacker.permanent.setState(MagicPermanentState.Attacking);
					result.add(attacker.permanent);
				} else {
					validChoices.add(attacker.permanent);
				}
			}		
		} 

		if (validChoices.isEmpty()&&game.canSkipSingleChoice()) {
			return new Object[]{result};
		}
		
		controller.focusViewers(-1,1);
		controller.showMessage(source,MESSAGE);
		controller.setValidChoices(validChoices,true);
		controller.enableForwardButton();
		controller.update();

		boolean undo=false;
		while (true) {
			if (controller.waitForInputOrUndo()) {
				undo=true;
				break;
			}				
			if (controller.isActionClicked()) {
				break;
			}
			final MagicPermanent attacker=(MagicPermanent)controller.getChoiceClicked();
			if (attacker.isAttacking()) {
				attacker.clearState(MagicPermanentState.Attacking);
				result.remove(attacker);
			} else {
				attacker.setState(MagicPermanentState.Attacking);
				result.add(attacker);				
			}
			controller.update();
		}

		// Cleanup.
		for (final MagicCombatCreature creature : builder.getAttackers()) {
			creature.permanent.clearState(MagicPermanentState.Attacking);
		}
		if (undo) {
			return UNDO_CHOICE_RESULTS;	
		}
		game.createUndoPoint();
		return new Object[]{result};
	}

	public static MagicDeclareAttackersChoice getInstance() {
		return INSTANCE;
	}
}
