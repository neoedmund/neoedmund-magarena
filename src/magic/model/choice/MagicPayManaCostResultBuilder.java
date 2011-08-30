package magic.model.choice;

import magic.model.*;
import magic.model.event.MagicSourceManaActivation;

import java.util.*;

public class MagicPayManaCostResultBuilder {

	private final MagicGame game;
	private final MagicBuilderManaCost cost;
	private final List<MagicSourceManaActivation> activations;
	private MagicCostManaType types[];
	private int amounts[];
	private int activationsSize;
	private Map<MagicBuilderPayManaCostResult,MagicBuilderPayManaCostResult> results;
	
	public MagicPayManaCostResultBuilder(final MagicGame game,final MagicPlayer player,final MagicBuilderManaCost cost) {
		this.game=game;
		this.cost=cost;
		activations=player.getManaActivations(game);
		activationsSize=activations.size();
		types=cost.getTypes();
		amounts=cost.getAmounts();
	}
	
	private boolean build(final int index,final boolean single) {
		// Check if valid result is reached.
		if (index==types.length) {
			if (single) {
				return true;
			}
			final MagicBuilderPayManaCostResult result=new MagicBuilderPayManaCostResult(activations);
			final MagicBuilderPayManaCostResult currentResult=results.get(result);
			if (currentResult==null||currentResult.getWeight()>result.getWeight()) {
				result.buildResults(activations,cost);
				results.put(result,result);
			}
			return true;
		}

		// Generate all available activations for mana cost type.
		final MagicCostManaType costManaType=types[index];
		final MagicSourceManaActivation typeActivations[]=new MagicSourceManaActivation[activationsSize];
		final MagicManaType producedTypes[]=new MagicManaType[activationsSize];
		int typeActivationSize=0;
		for (final MagicSourceManaActivation activation : activations) {
			final MagicManaType manaType=activation.canProduce(costManaType);
			if (manaType != MagicManaType.NONE) {
				typeActivations[typeActivationSize]=activation;
				producedTypes[typeActivationSize]=manaType;
				typeActivationSize++;
			}
		}

		final int minAmount=amounts[index];
		
		// Skip when the amount is 0.
		if (minAmount==0) {
			return build(index+1,single);
		}
		
		// Check if there are enough activations for amount > 0.
		if (minAmount>typeActivationSize) {
			return false;
		}

		final boolean hasX=costManaType==MagicCostManaType.Colorless&&cost.hasX();
		
		// Fast implementation when minimum amount is 1 without X.
		if (minAmount==1&&!hasX) {
			for (int activationIndex=0;activationIndex<typeActivationSize;activationIndex++) {
				final MagicSourceManaActivation typeActivation=typeActivations[activationIndex];
				typeActivation.available=false;
				typeActivation.manaType=producedTypes[activationIndex];
				if (build(index+1,single)&&single) {
					return true;
				}
				typeActivation.available=true;
			}
			return false;
		}

		// Fast implementation when minimum amount is equal to number of left activations.
		if (minAmount==typeActivationSize) {
			for (int activationIndex=0;activationIndex<typeActivationSize;activationIndex++) {
				final MagicSourceManaActivation typeActivation=typeActivations[activationIndex];
				typeActivation.available=false;
				typeActivation.manaType=producedTypes[activationIndex];				
			}
			if (build(index+1,single)&&single) {
				return true;
			}
			for (int activationIndex=0;activationIndex<typeActivationSize;activationIndex++) {
				typeActivations[activationIndex].available=true;
			}
			return false;
		}
		
		// Generate all possible combinations with at least the minimum number of sources.
		int optionIndices[]=new int[typeActivationSize];
		int activationIndex=0;
		int count=0;
		optionIndices[0]=-1;
		while (activationIndex>=0) {
			switch (++optionIndices[activationIndex]) {
				case 0:
					typeActivations[activationIndex].available=false;
					typeActivations[activationIndex].manaType=producedTypes[activationIndex];
					count++;
					if (count>=minAmount&&build(index+1,single)&&single) {
						return true;
					}
					if (count<minAmount||(hasX&&activationIndex+1!=typeActivationSize)) {
						activationIndex++;
						optionIndices[activationIndex]=-1;
					}
					break;
				case 1:
					typeActivations[activationIndex].available=true;	
					count--;
					if (typeActivationSize-activationIndex+count>minAmount&&activationIndex+1!=typeActivationSize) {
						activationIndex++;
						optionIndices[activationIndex]=-1;
					}
					break;
				case 2:
					activationIndex--;
					break;
			}
		}
		
		return false;
	}
	
	public boolean hasResults() {
		// Check if there are enough mana sources.
		if (cost.getMinimumAmount()>activationsSize) {
			return false;
		}		
		return build(0,true);
	}

	/** Finds all possible options to pay the cost for AI. */
	public Collection<Object> getResults() {
		for (final MagicSourceManaActivation activation : activations) {
			activation.available=true;
		}
		results=new HashMap<MagicBuilderPayManaCostResult,MagicBuilderPayManaCostResult>();
		build(0,false);
		return new TreeSet<Object>(results.values());
	}	
	
	/** Find all possible mana sources to pay one mana of given type for the player. */
	public Set<Object> getManaSources(final MagicCostManaType type,final boolean all) {
		cost.removeType(type,1);
		cost.compress();
		types=cost.getTypes();
		amounts=cost.getAmounts();
		
		final Set<Object> manaSources=new HashSet<Object>();
		final Set<Integer> manaIds=new HashSet<Integer>();
		for (final MagicSourceManaActivation currentActivation : activations) {
			currentActivation.available=true;
			if (currentActivation.canProduce(type) != MagicManaType.NONE) {
				for (final MagicSourceManaActivation activation : activations) {
					activation.available=activation!=currentActivation;
				}
				if (hasResults()) {
					final MagicPermanent permanent=currentActivation.permanent;
					if (all) {
						manaSources.add(permanent);
					} else {
						final int manaId=permanent.getManaId();
						if (!manaIds.contains(manaId)) {
							manaSources.add(permanent);
							manaIds.add(manaId);
						}
					}
				}
			}
		}
		return manaSources;
	}
	
	public void useManaSource(final MagicPermanent permanent,final MagicCostManaType type) {
		// Use the mana activation.
		final MagicSourceManaActivation sourceActivation=new MagicSourceManaActivation(game,permanent);
		sourceActivation.produce(game,type);
		
		// Remove permanent for available sources.
		for (final Iterator<MagicSourceManaActivation> iterator=activations.iterator();iterator.hasNext();) {
			
			final MagicSourceManaActivation activation=iterator.next();
			if (activation.permanent==permanent) {
				iterator.remove();
				break;
			}
		}
		activationsSize=activations.size();
	}
	
	/** Works only for all the remaining colorless mana. */
	public boolean useAllManaSources(final MagicCostManaType type) {
		if (activationsSize>cost.getMinimumAmount()||type!=MagicCostManaType.Colorless) {
			return false;
		}
		for (final MagicSourceManaActivation activation : activations) {
			final MagicSourceManaActivation sourceActivation=new MagicSourceManaActivation(game,activation.permanent);
			if (sourceActivation.available) {
				sourceActivation.produce(game,MagicCostManaType.Colorless);
			}
		}
		return true;
	}
}
