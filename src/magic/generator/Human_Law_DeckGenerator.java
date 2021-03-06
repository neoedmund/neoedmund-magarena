package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;
import magic.model.MagicSubType;

public class Human_Law_DeckGenerator extends DefaultDeckGenerator {

	private static final String colorText = "w";
	private static final String[] cards = {
        "Champion of the Parish", 
        "Champion of the Parish", 
        "Champion of the Parish", 
        "Elite Vanguard", 
        "Gideon's Lawkeeper", 
        "Hero of Bladehold", 
        "Hero of Bladehold", 
        "Hero of Bladehold", 
        "Mirran Crusader", 
        "Mirran Crusader", 
        "Angelic Destiny", 
        "Angelic Destiny", 
        "Honor of the Pure", 
        "Honor of the Pure", 
        "Day of Judgment"
    };
	
	public Human_Law_DeckGenerator() {
		super(null);
		setCubeDefinition(CubeDefinitions.getCubeDefinition(getColorText()));
	}
	
	public String getColorText() {
		return colorText;
	}
	
	public int getMinRarity() {
		return 2;
	}
	
	public boolean acceptPossibleSpellCard(MagicCardDefinition card) {
		return !card.isCreature() || card.hasSubType(MagicSubType.Human);
	}
	
	public void addRequiredSpells(MagicCondensedDeck deck) {
		addRequiredCards(deck, cards);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
}
