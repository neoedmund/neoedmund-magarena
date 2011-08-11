package magic.model.trigger;

public enum MagicTriggerType {
	AtUpkeep,               // player
	AtEndOfTurn,            // player
	WhenDamageIsDealt,      // damage
	WhenSpellIsPlayed,      // card on stack
	WhenComesIntoPlay,      // null
	WhenDiscarded,			// card
	WhenDrawn,				// card
	WhenOtherComesIntoPlay, // permanent
	WhenPutIntoGraveyard,   // graveyard trigger data
	WhenOtherPutIntoGraveyardFromPlay, // permanent
	WhenAttacks,            // permanent
	WhenBlocks,             // permanent
    WhenTargeted,           // permanent
	IfDamageWouldBeDealt,   // damage
	;
	
	public boolean usesStack() {
        return this != IfDamageWouldBeDealt;
	}
}
