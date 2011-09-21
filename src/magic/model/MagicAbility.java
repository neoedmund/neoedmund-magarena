package magic.model;

public enum MagicAbility {
	
	AttacksEachTurnIfAble("attacks each turn if able",-10),
	CannotBlock("can't block",-50),
	CannotAttackOrBlock("can't attack or block",-200),
	CannotBlockWithoutFlying("can't block creatures without flying",-40),
	CannotBeBlockedByFlying("can't be blocked by creatures with flying",20),
	CannotBeBlockedExceptWithFlying("can't be blocked except by creatures with flying",30),
	CannotBeBlockedExceptWithFlyingOrReach("can't be blocked except by creatures with flying or reach",25),
	CannotBeCountered("can't be countered",0),
	CannotBeTheTarget("hexproof",80),
	CannotBeTheTarget0("can't be the target of spells or abilities your opponents control",80),
	CannotBeTheTarget1("can't be the target of spells or abilities your opponents control",80),
	Changeling("changeling",10),
	Deathtouch("deathtouch",60),
	Defender("defender",-100),
	DoubleStrike("double strike",100),
	Exalted("exalted",10),
	Fear("fear",50),
	Flash("flash",0),
	Flying("flying",50),
	FirstStrike("first strike",50),
	Forestwalk("forestwalk",10),
	Indestructible("indestructible",150),
	Islandwalk("Islandwalk",10),
	Haste("haste",0),
	LifeLink("lifelink",40),
	Mountainwalk("mountainwalk",10),
	Persist("persist",60),
	PlainsWalk("plainswalk",10),
	ProtectionFromBlack("protection from black",20),
	ProtectionFromBlue("protection from blue",20),
	ProtectionFromGreen("protection from green",20),
	ProtectionFromRed("protection from red",20),
	ProtectionFromWhite("protection from white",20),
	ProtectionFromMonoColored("protection from monocolored",50),
	ProtectionFromAllColors("protection from all colors",150),
	ProtectionFromCreatures("protection from creatures",100),
	ProtectionFromDemons("protection from Demons",10),
	ProtectionFromDragons("protection from Dragons",10),
	Reach("reach",20),
	Shroud("shroud",60),
	Swampwalk("swampwalk",10),
	Trample("trample",30),
	Unblockable("unblockable",100),
	Vigilance("vigilance",20),
	Wither("wither",30),
	TotemArmor("totem armor",0),
	Intimidate("intimidate",45),
	BattleCry("battle cry",0),
	Infect("infect",35),
    LivingWeapon("living weapon", 10);
	;

	public static final long PROTECTION_FLAGS=
		ProtectionFromBlack.getMask()|
		ProtectionFromBlue.getMask()|
		ProtectionFromGreen.getMask()|
		ProtectionFromRed.getMask()|
		ProtectionFromWhite.getMask()|
		ProtectionFromMonoColored.getMask()|
		ProtectionFromAllColors.getMask()|
		ProtectionFromCreatures.getMask()|
		ProtectionFromDemons.getMask()|
		ProtectionFromDragons.getMask();
	
	public static final long LANDWALK_FLAGS=
		Forestwalk.getMask()|
		Islandwalk.getMask()|
		Mountainwalk.getMask()|
		PlainsWalk.getMask()|
		Swampwalk.getMask();
	
    public static final long EXCLUDE_MASK = 
        Long.MAX_VALUE-Flash.getMask()-CannotBeCountered.getMask()-TotemArmor.getMask();
	
	private final String name;
	private final int score;
	private final long mask;
	
	private MagicAbility(final String name,final int score) {
		this.name=name;
		this.score=score;
		mask=1L<<ordinal();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

	private int getScore() {
		return score;
	}
	
	public long getMask() {
		return mask;
	}

	public boolean hasAbility(final long flags) {
		return (flags&mask)!=0;
	}
	
	public static int getScore(final long flags) {
		int score=0;
		for (final MagicAbility ability : values()) {
			if (ability.hasAbility(flags)) {
				score+=ability.getScore();
			}
		}
		return score;
	}
	
	public static MagicAbility getAbility(final String name) {
		for (final MagicAbility ability : values()) {
			if (ability.getName().equalsIgnoreCase(name)) {
				return ability;
			}
		}
        throw new RuntimeException("Unable to convert " + name + " to an ability");
	}
	
    public static long getAbilities(final String[] names) {
        long flags = 0;
		for (final String name : names) {
            flags |= getAbility(name).getMask();
		}
        return flags;
	}
}
