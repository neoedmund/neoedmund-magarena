package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;

public class MagicShuffleIntoLibraryAction extends MagicAction {

	private final MagicCard card;
	private MagicCardList oldLibrary;
	
	public MagicShuffleIntoLibraryAction(final MagicCard card) {
		this.card=card;		
	}

	@Override
	public void doAction(final MagicGame game) {
		if (!card.isToken()) {
			final MagicCardList library=card.getOwner().getLibrary();
			oldLibrary=new MagicCardList(library);
			library.addToTop(card);
			library.shuffle(library.getCardsId());
		}	
	}

	@Override
	public void undoAction(final MagicGame game) {
		if (!card.isToken()) {
			card.getOwner().getLibrary().setCards(oldLibrary);
		}
	}
}
