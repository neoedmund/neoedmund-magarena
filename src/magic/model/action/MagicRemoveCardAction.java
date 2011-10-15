package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class MagicRemoveCardAction extends MagicAction {

	private final MagicCard card;
	private final MagicLocationType locationType;
	private int index;
	
	public MagicRemoveCardAction(final MagicCard card,final MagicLocationType locationType) {
		this.card=card;
		this.locationType=locationType;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		final MagicPlayer owner=card.getOwner();
		switch (locationType) {
			case OwnersHand:
				index=owner.removeCardFromHand(card);
                if (index >= 0) {
    				setScore(owner,-ArtificialScoringSystem.getCardScore(card));
                }
				break;
			case OwnersLibrary:
				index=owner.getLibrary().removeCard(card);
				break;
			case Graveyard:
				index=owner.getGraveyard().removeCard(card);
				break;		
			case Exile:
				index=owner.getExile().removeCard(card);
				break;
		}		
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {
        //do nothing if index is invalid
        if (index < 0) {
            return;
        }

		final MagicPlayer owner=card.getOwner();
		switch (locationType) {
			case OwnersHand:
    			owner.addCardToHand(card,index);
				break;
			case OwnersLibrary:
				owner.getLibrary().add(index,card);
				break;
			case Graveyard:
				owner.getGraveyard().add(index,card);
				break;
			case Exile:
				owner.getExile().add(index,card);
				break;
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName()+" ("+card.getName()+")";
	}	
}
