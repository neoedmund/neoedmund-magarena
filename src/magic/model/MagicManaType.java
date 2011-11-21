package magic.model;

import magic.data.IconImages;

import javax.swing.ImageIcon;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MagicManaType {

	Colorless("colorless","{1}"),
	Black("black","{B}"),
	Blue("blue","{U}"),
	Green("green","{G}"),
	Red("red","{R}"),
	White("white","{W}"),
	NONE("none","{N}"),
	;
	
	public static final List<MagicManaType> ALL_COLORS = Collections.unmodifiableList(Arrays.asList(
        Black,Blue,Green,Red,White));
	public static final List<MagicManaType> ALL_TYPES = Collections.unmodifiableList(Arrays.asList(
        Colorless,Black,Blue,Green,Red,White)); // Colorless must be in front.
	
	public static final int NR_OF_TYPES = ALL_TYPES.size();
	
	private final String name;
	private final String text;
	
	private MagicManaType(final String name, final String text) {
		this.name=name;
		this.text=text;
	}

    public boolean isValid() {
        return this != MagicManaType.NONE;
    }
	
	public String getName() {
		return name;
	}
		
	public String getText() {
		return text;
	}
	
    public static MagicManaType get(final String name) {
		for (final MagicManaType type : values()) {
			if (type.toString().equalsIgnoreCase(name)) {
				return type;
			}
		}
        throw new RuntimeException("Unknown mana type " + name);
	}

	@Override
	public String toString() {
		return text;
	}
	
	public ImageIcon getIcon(final boolean small) {
		switch (this) {
			case Colorless: return small?IconImages.COST_ONE:IconImages.ONE;
			case Black: return small?IconImages.COST_BLACK:IconImages.BLACK;
			case Blue: return small?IconImages.COST_BLUE:IconImages.BLUE;
			case Green: return small?IconImages.COST_GREEN:IconImages.GREEN;
			case Red: return small?IconImages.COST_RED:IconImages.RED;
			case White: return small?IconImages.COST_WHITE:IconImages.WHITE;
		}
        throw new RuntimeException("No icon available for MagicManaType " + this);
	}
}
