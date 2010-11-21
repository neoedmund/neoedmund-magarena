package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.phase.MagicPhaseType;

public enum MagicTiming {
	
	// Creature spells
	Main("main",5),    	 	 	    // Main
	FirstMain("fmain",5),	        // First main : haste, can't block, shroud, comes into play, static effect, non tap ability, combat trigger
	SecondMain("smain",5),          // Second main : defender, non combat or defensive ability, red mana unlikely
	Flash("flash",5),        	    // Declare attackers or end of turn from opponent
	CounterFlash("counterflash",9), // Like flash, but also when there is a spell of opponent on stack	
	PumpFlash("pumpFlash",6),       // Like flash, but also declare blockers, as response

	// Other permanent spells
	Land("land",1),				    // Main
	TapLand("tapland",1),			// Second main
	Enchantment("enchantment",4),	// Main
	Artifact("artifact",4), 		// Main
	Aura("aura",7),					// Main
	Equipment("equipment",7), 		// Main
	
	// Remaining spells & abilities.
	Draw("draw",1), 				// Main
	Tapping("tapping",2),			// Main, declare attackers or as response
	Removal("removal",3),			// Main, declare blockers or as response
	Pump("pump",8),                 // First main your turn, declare blockers or as response
	Counter("counter",9),	        // When there is a spell of opponent on stack
	Attack("attack",8),			    // Declare attackers
	Block("block",8),			 	// Declare blockers
	Animate("animate",6),			// First main of your turn, declare attackers opponent's turn
	Token("token",6),				// First main of your turn, declare attackers or end of turn opponent, as response
	NextTurn("nextturn",9), 		// Second main
	MustAttack("mustattack",8),		// First main opponent's turn
	
	// No timing
	None("none",0),				    // No restrictions.
	;
	
	private final String code;
	private final int priority;
	
	private MagicTiming(final String code,final int priority) {
		
		this.code=code;
		this.priority=priority;
	}
	
	public String getCode() {
		
		return code;
	}
	
	public int getPriority() {
		
		return priority;
	}
	
	public boolean canPlay(final MagicGame game,final MagicSource source) {
		
		final MagicPlayer controller=source.getController();
		switch (this) {
			case Main:
			case Land:
			case Enchantment:
			case Artifact:
			case Aura:
			case Equipment:
			case Draw:
				return game.isMainPhase();
			case FirstMain:
				return game.isPhase(MagicPhaseType.FirstMain);
			case SecondMain:
			case TapLand:
			case NextTurn:
				return game.isPhase(MagicPhaseType.SecondMain);
			case Flash:
				return game.getTurnPlayer()!=controller&&(game.isPhase(MagicPhaseType.DeclareAttackers)||game.isPhase(MagicPhaseType.EndOfTurn));				
			case CounterFlash:
				return (game.getTurnPlayer()!=controller&&(game.isPhase(MagicPhaseType.DeclareAttackers)||game.isPhase(MagicPhaseType.EndOfTurn)))||
					game.getStack().containsOpponentSpells(controller);
			case PumpFlash:
				return (game.getTurnPlayer()!=controller&&(game.isPhase(MagicPhaseType.DeclareAttackers)||game.isPhase(MagicPhaseType.EndOfTurn)))||
					game.isPhase(MagicPhaseType.DeclareBlockers)||game.getStack().isResponse(controller);				
			case Tapping:
				return game.isMainPhase()||game.isPhase(MagicPhaseType.DeclareAttackers)||game.getStack().isResponse(controller);
			case Removal:
				return game.isMainPhase()||game.isPhase(MagicPhaseType.DeclareBlockers)||game.getStack().isResponse(controller);
			case Pump:
				return (game.getTurnPlayer()==controller&&game.isPhase(MagicPhaseType.FirstMain))||
					game.isPhase(MagicPhaseType.DeclareBlockers)||game.getStack().isResponse(controller);
			case Counter:
				return game.getStack().containsOpponentSpells(controller);
			case Attack:
				return game.isPhase(MagicPhaseType.DeclareAttackers);
			case Block:
				return game.isPhase(MagicPhaseType.DeclareBlockers);
			case Animate:
				return (game.getTurnPlayer()==controller&&game.isPhase(MagicPhaseType.FirstMain))||
					   (game.getTurnPlayer()!=controller&&game.isPhase(MagicPhaseType.DeclareAttackers));
			case Token:
				return (game.getTurnPlayer()==controller&&game.isPhase(MagicPhaseType.FirstMain))||
					   (game.getTurnPlayer()!=controller&&(game.isPhase(MagicPhaseType.DeclareAttackers)||game.isPhase(MagicPhaseType.EndOfTurn)))||
					   game.getStack().isResponse(controller);
			case MustAttack:
				return game.getTurnPlayer()!=controller&&game.isPhase(MagicPhaseType.FirstMain);
			default:
				return true;
		}
	}
	
	public static MagicTiming getTimingFor(final String code) {
		
		for (final MagicTiming timing : values()) {
			
			if (timing.getCode().equalsIgnoreCase(code)) {
				return timing;
			}
 		}
		return None;
	}
}