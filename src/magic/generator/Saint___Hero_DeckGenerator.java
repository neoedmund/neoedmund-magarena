package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;

public class Saint___Hero_DeckGenerator extends DefaultDeckGenerator {

	private final String colorText = "wu";
	
	public Saint___Hero_DeckGenerator() {
		super(null);
		
		setCubeDefinition(CubeDefinitions.getInstance().getCubeDefinition(getColorText()));
	}
	
	public String getColorText() {
		return colorText;
	}
	
	public int getMinRarity() {
		return 2;
	}
	
	public void addRequiredSpells(MagicCondensedDeck deck) {
		String[] cards = {"Geist of Saint Traft", "Hero of Bladehold", "Hero of Bladehold", "Hero of Bladehold", "Hero of Bladehold", "Batterskull", "Day of Judgment", "Day of Judgment", "Mana Leak", "Mana Leak", "Mana Leak", "Timely Reinforcements", "Timely Reinforcements", "Oblivion Ring", "Oblivion Ring", "Sword of Feast and Famine"};
		addRequiredCards(deck, cards);
	}
	
	public void addRequiredLands(MagicCondensedDeck deck) {
		String[] cards = {"Glacial Fortress", "Glacial Fortress", "Glacial Fortress", "Seachrome Coast", "Seachrome Coast", "Seachrome Coast"};
		addRequiredCards(deck, cards);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
}
