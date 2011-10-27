package magic.generator;

import magic.data.CubeDefinitions;
import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicPlayerProfile;
import magic.model.MagicSubType;

public class Zombie_Madness_DeckGenerator extends DefaultDeckGenerator {

	private final String colorText = "b";
	
	public Zombie_Madness_DeckGenerator() {
		super(null);
		
		setCubeDefinition(CubeDefinitions.getInstance().getCubeDefinition(getColorText()));
	}
	
	public String getColorText() {
		return colorText;
	}
	
	public int getMinRarity() {
		return 2;
	}
	
	public boolean acceptPossibleSpellCard(MagicCardDefinition card) {
		return !card.isCreature() || card.hasSubType(MagicSubType.Zombie);
	}
	
	public void addRequiredSpells(MagicCondensedDeck deck) {
		String[] cards = {"Cemetery Reaper", "Cemetery Reaper", "Cemetery Reaper", "Cemetery Reaper", "Death Baron", "Death Baron", "Festering Goblin", "Festering Goblin", "Lord of the Undead", "Lord of the Undead", "Lord of the Undead", "Call to the Grave", "Call to the Grave", "Severed Legion"};
		addRequiredCards(deck, cards);
	}
	
	public void setColors(MagicPlayerProfile profile) {
		profile.setColors(getColorText());
	}
}
