package magic.model.trigger;

public enum MagicTriggerType {
	AtUpkeep,               // player
	AtEndOfTurn,            // player
	WhenDamageIsDealt,      // damage
	WhenSpellIsPlayed,      // card on stack
	WhenComesIntoPlay,      // controller
	WhenBecomesTapped,      // permanent
	WhenDiscarded,			// card
	WhenDrawn,				// card
	WhenLifeIsGained,		// player, life gained
	WhenOtherComesIntoPlay, // permanent
	WhenPutIntoGraveyard,   // graveyard trigger data
	WhenOtherPutIntoGraveyardFromPlay, // permanent
	WhenAttacks,            // permanent
	WhenBlocks,             // permanent
	WhenBecomesBlocked,		//permanent
    WhenTargeted,           // permanent
	IfDamageWouldBeDealt,   // item on stack
    IfPlayerWouldLose,      // player[]
	;
	
	public boolean usesStack() {
        return this != IfDamageWouldBeDealt;
	}
}
