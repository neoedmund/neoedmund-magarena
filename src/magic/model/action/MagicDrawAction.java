package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.trigger.MagicTriggerType;

import java.util.ArrayList;
import java.util.List;

public class MagicDrawAction extends MagicAction {

	private final MagicPlayer player;
	private final int amount;
	private List<MagicCard> drawnCards;
	
	public MagicDrawAction(final MagicPlayer player,final int amount) {
		
		this.player=player;
		this.amount=amount;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		
		drawnCards=new ArrayList<MagicCard>();
		final MagicCardList library=player.getLibrary();
		int score=0;
		for (int count=amount;count>0;count--) {

			if (library.isEmpty()) {
				if (MagicGame.LOSE_DRAW_EMPTY_LIBRARY) {
					game.doAction(new MagicLoseGameAction(player,MagicLoseGameAction.DRAW_REASON));
				}
				break;
			} 
			final MagicCard card=library.removeCardAtTop();
			player.addCardToHand(card);
			drawnCards.add(card);
			score+=ArtificialScoringSystem.getCardScore(card);
			game.executeTrigger(MagicTriggerType.WhenDrawn,card);
		}
		setScore(player,score);
		game.setStateCheckRequired();			
	}

	@Override
	public void undoAction(final MagicGame game) {
		for (int index=drawnCards.size()-1;index>=0;index--) {
			final MagicCard card=drawnCards.get(index);
			player.removeCardFromHand(card);
			player.getLibrary().addToTop(card);
		}
	}
}
