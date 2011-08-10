package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicManaType;
import magic.model.MagicMappable;
import magic.model.MagicPlayer;
import magic.model.event.MagicSourceManaActivation;
import magic.model.event.MagicSourceManaActivationResult;

import java.util.Arrays;
import java.util.List;

public class MagicBuilderPayManaCostResult implements 
    MagicPayManaCostResult, MagicMappable, Comparable<MagicBuilderPayManaCostResult> {

	private MagicSourceManaActivationResult results[];
	private short amountLeft[];
	private int weight;
	private int count;
	private int x;
	private int hashCode;
	
	public MagicBuilderPayManaCostResult(final List<MagicSourceManaActivation> sourceActivations) {
		count=0;
		x=0;
		amountLeft=new short[MagicManaType.NR_OF_TYPES];
		for (final MagicSourceManaActivation activation : sourceActivations) {
			if (activation.available) {
				for (int index=0;index<MagicManaType.NR_OF_TYPES;index++) {
					if (activation.activations[index]!=null) {
						amountLeft[index]++;
					}
				}				
			} else {
				count++;
				weight+=activation.getWeight();
			}
		}		
		hashCode=Arrays.hashCode(amountLeft);
	}
	
	private MagicBuilderPayManaCostResult() {}
	
	@Override
	public Object map(final MagicGame game) {
		final MagicBuilderPayManaCostResult result=new MagicBuilderPayManaCostResult();
		result.results=new MagicSourceManaActivationResult[results.length];
		for (int index=0;index<results.length;index++) {
			result.results[index]=(MagicSourceManaActivationResult)results[index].map(game);
		}
		result.amountLeft=Arrays.copyOf(amountLeft,amountLeft.length);
		result.weight=weight;
		result.count=count;
		result.x=x;
		result.hashCode=hashCode;
		return result;
	}

	/** Finishes construction when needed. */
	public void buildResults(final List<MagicSourceManaActivation> sourceActivations,final MagicBuilderManaCost cost) {
		x=cost.getX(count);
		results=new MagicSourceManaActivationResult[count];
		int index=0;
		for (final MagicSourceManaActivation activation : sourceActivations) {
			if (!activation.available) {
				results[index++]=activation.getResult();
			}
		}
	}
		
	public int getWeight() {
		return weight;
	}
	
	@Override
	public int getX() {
		return x;
	}
	
	@Override
	public int getConverted() {
		return count;
	}

	public void doAction(final MagicGame game,final MagicPlayer player) {
		for (final MagicSourceManaActivationResult result : results) {
			result.doActivation(game);
		}		
	}
	
	public String getText() {
		final StringBuilder builder=new StringBuilder();
		builder.append(count);
		for (final int amount : amountLeft) {
			builder.append('-').append(amount);
		}
		builder.append('-').append(weight);
		return builder.toString();
	}
	
	@Override
	public String toString() {
        return x > 0 ? "X is " + x : "";
	}

	@Override
	public int hashCode() {
		return hashCode;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null||getClass()!=obj.getClass()) {
			return false;
		}
		final MagicBuilderPayManaCostResult other=(MagicBuilderPayManaCostResult)obj;
		return Arrays.equals(amountLeft,other.amountLeft);
	}

	@Override
	public int compareTo(final MagicBuilderPayManaCostResult result) {
		for (int index=0;index<MagicManaType.NR_OF_TYPES;index++) {
			final int dif=amountLeft[index]-result.amountLeft[index];
			if (dif!=0) {
				return dif;
			}
		}
		return 0;
	}

    @Override
    public long getId() {
        return hashCode;
    }
}
