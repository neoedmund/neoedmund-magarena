package magic.data;

import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicRarity;

import javax.swing.ImageIcon;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CardStatistics {

	private static final List<String> MANA_CURVE_TEXT = Collections.unmodifiableList(Arrays.asList(
        "X","1","2","3","4","5","6","7","8","9+"));
	public static final List<ImageIcon> MANA_CURVE_ICONS = Collections.unmodifiableList(Arrays.asList(
		IconImages.COST_X,
		IconImages.COST_ONE,
		IconImages.COST_TWO,
		IconImages.COST_THREE,
		IconImages.COST_FOUR,
		IconImages.COST_FIVE,
		IconImages.COST_SIX,
		IconImages.COST_SEVEN,
		IconImages.COST_EIGHT,
		IconImages.COST_NINE
	));
	public static final int MANA_CURVE_SIZE=MANA_CURVE_TEXT.size();
	
	private static final List<String> TYPE_NAMES = Collections.unmodifiableList(Arrays.asList(
        "Land","Spell","Creature","Equipment","Aura","Enchantment","Artifact"));
	public static final List<ImageIcon> TYPE_ICONS = Collections.unmodifiableList(Arrays.asList(
		IconImages.LAND,
		IconImages.SPELL,
		IconImages.CREATURE,
		IconImages.EQUIPMENT,
		IconImages.AURA,	
		IconImages.ENCHANTMENT,
		IconImages.ARTIFACT
	));
	public static final int NR_OF_TYPES=TYPE_NAMES.size();
	
	private final Collection<MagicCardDefinition> cards;
	
	public int totalCards=0;
	public int totalTypes[]=new int[NR_OF_TYPES];
	
	public int totalRarity[]=new int[MagicRarity.length];
	
	public double averageCost=0;
	public double averageValue=0;
	
	public int colorCount[]=new int[MagicColor.NR_COLORS];
	public int colorMono[]=new int[MagicColor.NR_COLORS];
	public int colorLands[]=new int[MagicColor.NR_COLORS];
	public int manaCurve[]=new int[MANA_CURVE_SIZE];
	public int monoColor=0;
	public int multiColor=0;
	public int colorless=0;
	
	public CardStatistics(final Collection<MagicCardDefinition> cards) {
		this.cards=cards;
		createStatistics();
	}
	
	private void createStatistics() {
		
		totalCards=cards.size();

		if (cards.size()==0) {
			return;
		}
		
		for (final MagicCardDefinition card : cards) {
												
			totalRarity[card.getRarity()]++;
						
			if (card.isLand()) {
				totalTypes[0]++;
				for (final MagicColor color : MagicColor.values()) {
					
					if (card.getManaSource(color)>0) {
						colorLands[color.ordinal()]++;
					}
				}				
			} else {
				if (card.hasX()) {
					manaCurve[0]++;
				} else {
					final int convertedCost=card.getConvertedCost();
					manaCurve[convertedCost>=MANA_CURVE_SIZE?MANA_CURVE_SIZE-1:convertedCost]++;
				}
				
				averageCost+=card.getConvertedCost();
				averageValue+=card.getValue();
				
				if (card.isCreature()) {
					totalTypes[2]++;
				} else if (card.isEquipment()) {
					totalTypes[3]++;
				} else if (card.isArtifact()) {
					totalTypes[6]++;
				} else if (card.isAura()) {
					totalTypes[4]++;
				} else if (card.isEnchantment()) {
					totalTypes[5]++;
				} else {
					totalTypes[1]++;
				}
				
				int count=0;
				int index=-1;
				for (final MagicColor color : MagicColor.values()) {
				
					if (color.hasColor(card.getColorFlags())) {
						index=color.ordinal();
						colorCount[index]++;
						count++;
					}
				}
				if (count==0) {
					colorless++;
				} else if (count==1) {
					colorMono[index]++;
					monoColor++;
				} else {
					multiColor++;
				}
			}
		}
		
		final int total=totalCards-totalTypes[0];
		if (total>0) {
			averageValue /= total;
			averageCost /= total;
		}
	}
	
	public void printStatictics(final PrintStream stream) {

		stream.print("Cards : "+totalCards);
		for (int index=0;index<NR_OF_TYPES;index++) {
			stream.print("  "+TYPE_NAMES.get(index)+" : "+totalTypes[index]);
		}
		stream.println();
		
		for (int index=0;index<MagicRarity.length;index++) {
			
			stream.print(MagicRarity.values()[index].getName() + " : " + totalRarity[index] + "  ");
		}
		stream.println();
		stream.printf("Average Cost : %.2f  Value : %.2f\n", averageCost, averageValue);
		stream.println("Monocolor : "+monoColor+"  Multicolor : "+multiColor+"  Colorless : "+colorless);

		for (final MagicColor color : MagicColor.values()) {
			
			final int index=color.ordinal();
			stream.print("Color "+color.getName()+" : "+colorCount[index]);
			stream.print("  Mono : "+colorMono[index]);
			stream.print("  Lands : "+colorLands[index]);
			stream.println();			
		}	
		
		for (int index=0;index<MANA_CURVE_SIZE;index++) {
			
			stream.print(MANA_CURVE_TEXT.get(index)+" = "+manaCurve[index]+"  ");
		}
		stream.println();
	}
}
