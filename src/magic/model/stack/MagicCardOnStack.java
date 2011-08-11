package magic.model.stack;

import magic.model.*;

import javax.swing.*;
import java.util.Arrays;

public class MagicCardOnStack extends MagicItemOnStack {

	private MagicLocationType moveLocation=MagicLocationType.Graveyard;
	private int x=0;
	
	public MagicCardOnStack(final MagicCard card,final MagicPlayer controller,final MagicPayedCost payedCost) {
		setSource(card);
		setController(controller);
		setEvent(card.getCardDefinition().getCardEvent().getEvent(this,payedCost));
		x=payedCost.getX();
	}
	
	public MagicCardOnStack(final MagicCard card,final MagicPayedCost payedCost) {
		this(card,card.getController(),payedCost);
	}
	
	private MagicCardOnStack() {}
	
	public MagicCardOnStack copyCardOnStack(final MagicPlayer player) {
		final MagicPayedCost cost=new MagicPayedCost();
		cost.setX(x);
		final MagicCard card=MagicCard.createTokenCard(getCardDefinition(),player);
		final MagicCardOnStack copyCardOnStack=new MagicCardOnStack(card,cost);
		final Object choiceResults[]=getChoiceResults();
		if (choiceResults!=null) {
			copyCardOnStack.setChoiceResults(Arrays.copyOf(choiceResults,choiceResults.length));
		}
		return copyCardOnStack;
	}
	
	@Override
	public MagicCopyable create() {
		return new MagicCardOnStack();
	}
	
	@Override
	public void copy(final MagicCopyMap copyMap,final MagicCopyable source) {
		super.copy(copyMap,source);
		final MagicCardOnStack sourceCardOnStack=((MagicCardOnStack)source);
		moveLocation=sourceCardOnStack.moveLocation;
		x=sourceCardOnStack.x;
	}
	
	public MagicCard getCard() {
		return (MagicCard)getSource();
	}
	
	public MagicCardDefinition getCardDefinition() {
		return getCard().getCardDefinition();
	}

	public void setMoveLocation(final MagicLocationType moveLocation) {
		this.moveLocation=moveLocation;
	}
	
	public MagicLocationType getMoveLocation() {
		return moveLocation;
	}
	
	public int getConvertedCost() {
		return getCardDefinition().getConvertedCost()+x;
	}

	@Override
	public boolean isSpell() {
		return true;
	}
	
	@Override
	public boolean canBeCountered() {
		return !getCardDefinition().hasAbility(MagicAbility.CannotBeCountered);
	}
	
	@Override
	public ImageIcon getIcon() {
		return getCard().getCardDefinition().getIcon();
	}
}
