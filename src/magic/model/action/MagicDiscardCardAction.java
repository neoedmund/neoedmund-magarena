package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;

public class MagicDiscardCardAction extends MagicAction {

	private final MagicPlayer player;
	private final MagicCard card;
	private int index;
	
	public MagicDiscardCardAction(final MagicPlayer player,final MagicCard card) {
		this.player=player;
		this.card=card;
	}

	@Override
	public void doAction(final MagicGame game) {
		index = player.removeCardFromHand(card);
        if (index >= 0) {
		    setScore(player,-ArtificialScoringSystem.getCardScore(card));
            game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersHand,MagicLocationType.Graveyard));
            game.setStateCheckRequired();
        }
	}

	@Override
	public void undoAction(final MagicGame game) {
        if (index >= 0) {
    		player.addCardToHand(card,index);
        }
	}
}
