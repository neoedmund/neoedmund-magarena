package magic.model.choice;

import magic.model.MagicCostManaType;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicRandom;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;
import magic.ui.choice.ManaCostXChoicePanel;

import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/** X must be at least one in a mana cost. */
public class MagicPayManaCostChoice extends MagicChoice {
	
	private final MagicManaCost cost;
	
	public MagicPayManaCostChoice(final MagicManaCost cost) {

		super("Choose how to pay the mana cost.");
		this.cost=cost;
	}
	
	private MagicManaCost getCost() {

		return cost;
	}
	
	@Override
	public int getManaChoiceResultIndex() {

		return 0;
	}

	@Override
	boolean hasOptions(final MagicGame game,final MagicPlayer player,final MagicSource source,final boolean hints) {

		final MagicPayManaCostResultBuilder builder=new MagicPayManaCostResultBuilder(game,player,cost.getBuilderCost());
		return builder.hasResults();
	}
	
	private Collection<Object> buildDelayedPayManaCostResults(final MagicGame game,final MagicPlayer player) {
		
		if (cost.hasX()) {
			final int maxX=player.getMaximumX(game,cost);
			if (maxX==1) {
				return Collections.<Object>singletonList(new MagicDelayedPayManaCostResult(cost,1));
			} else {
				final List<Object> choices=new ArrayList<Object>();
				for (int x=1;x<=maxX;x++) {
					choices.add(new MagicDelayedPayManaCostResult(cost,x));
				}
				return choices;
			}
		} else {
			return Collections.<Object>singletonList(new MagicDelayedPayManaCostResult(cost,0));
		}
	}

	@Override
	Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
		if (game.getFastChoices()) {
			return buildDelayedPayManaCostResults(game,player);
		} else {
			final MagicPayManaCostResultBuilder builder = 
                new MagicPayManaCostResultBuilder(game,player,cost.getBuilderCost());		
			return builder.getResults();
		}
	}
	
    @Override
    public Object[] getSimulationChoiceResult(
			final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
        //in simulation use delayed pay mana cost
		final List<Object> choices = (List<Object>)buildDelayedPayManaCostResults(game,player);
        return new Object[]{choices.get(MagicRandom.nextInt(choices.size()))};
    }
	
	@Override
	public Object[] getPlayerChoiceResults(final GameController controller,final MagicGame game,final MagicPlayer player,final MagicSource source) {

		controller.disableActionButton(false);
		
		final int x;
		if (cost.hasX()) {
			final int maximumX=player.getMaximumX(game,cost);
			final ManaCostXChoicePanel choicePanel = controller.showComponent(new Callable<ManaCostXChoicePanel>() {
                public ManaCostXChoicePanel call() {
			        return new ManaCostXChoicePanel(controller,source,maximumX);
                }
            });
			if (controller.waitForInputOrUndo()) {
				return UNDO_CHOICE_RESULTS;
			}
			x=choicePanel.getValueForX();
		} else {
			x=0;
		}

		final List<MagicCostManaType> costManaTypes=cost.getCostManaTypes(x);
		final MagicBuilderManaCost builderCost=new MagicBuilderManaCost();
		builderCost.addTypes(costManaTypes);
		final MagicPayManaCostResultBuilder builder=new MagicPayManaCostResultBuilder(game,player,builderCost);
		final boolean canSkip = MagicGame.canSkipSingleManaChoice();
		
		for (final MagicCostManaType costManaType : costManaTypes) {

			if (canSkip&&builder.useAllManaSources(costManaType)) {
				controller.update();
				break;
			}
			
			final int costMinAmount = builderCost.getMinimumAmount();
			final Set<Object> validSources=builder.getManaSources(costManaType,!canSkip);
			MagicPermanent sourcePermanent = (MagicPermanent)validSources.iterator().next();
			if (canSkip && 
				(validSources.size() == 1 ||
				(builder.getActivationsSize() == costMinAmount &&
				sourcePermanent.getCounters(MagicCounterType.Charge) == 0))) {
			} else {
				controller.setValidChoices(validSources,false);
				controller.showMessage(source,"Choose a mana source to pay "+costManaType.getText()+".");
				if (controller.waitForInputOrUndo()) {
					return UNDO_CHOICE_RESULTS;
				}
				controller.clearValidChoices();
				sourcePermanent=(MagicPermanent)controller.getChoiceClicked();				
			}
			builder.useManaSource(sourcePermanent,costManaType);
			controller.update();
		}
		return new Object[]{new MagicPlayerPayManaCostResult(x,costManaTypes.size())};
	}
}
