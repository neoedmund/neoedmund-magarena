package magic.model.event;

import magic.data.CardDefinitions;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicSingleActivationCondition;

public abstract class MagicActivation implements MagicEventAction, Comparable<MagicActivation> {
	
    private final MagicCondition conditions[];
    private final String text;
	private final MagicActivationHints hints;
	private final int priority;
    private final int index;
	
    private int cardIndex;
	private long id;
	private MagicTargetChoice targetChoice;

	MagicActivation(
        final int index,
        final MagicCondition conditions[],
        final MagicActivationHints hints,
        final String txt) {
        
        this.text = txt;
        this.index = index;
		this.conditions=conditions;
		this.hints=hints;
		this.priority=hints.getTiming().getPriority();
        
        //depends on the card
        this.cardIndex = -1;
        this.id = -1;
		this.targetChoice = MagicTargetChoice.TARGET_NONE;
	}
    
    public void setCardIndex(final int cardIndex) {
        this.cardIndex = cardIndex;
        this.id = (cardIndex << 16) + index;
        this.targetChoice = getTargetChoice();
		
        // set the activation for the single activation condition, depends on id
        for (final MagicCondition condition : conditions) {
            if (condition instanceof MagicSingleActivationCondition) {
                final MagicSingleActivationCondition singleCondition = (MagicSingleActivationCondition)condition;
                singleCondition.setActivation(id);
            }
        }
    }
	
    final MagicCardDefinition getCardDefinition() {
		return CardDefinitions.getInstance().getCard(cardIndex);
	}
		
	private final MagicCondition[] getConditions() {
		return conditions;
	}

	public final MagicActivationHints getActivationHints() {
		return hints;
	}
	
	public final long getId() {
		return id;
	}

    public final String getText() {
        return text;
    }
	
	private final boolean checkActivationPriority(final MagicSource source) {
		final MagicActivationPriority actpri = source.getController().getActivationPriority();
		final int priorityDif = priority - actpri.getPriority();
		if (priorityDif > 0) {
			return true;
		} else if (priorityDif < 0) {
			return false;
		} 
		return id >= actpri.getActivationId();		
	}
	
	void changeActivationPriority(final MagicGame game,final MagicSource source) {
        final MagicActivationPriority actpri = source.getController().getActivationPriority();
		actpri.setPriority(priority);
		actpri.setActivationId(id);
	}
	
	public final boolean canPlay(
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source,
            final boolean useHints) {
		
		if (useHints && 
            (!checkActivationPriority(source) || 
             !hints.getTiming().canPlay(game,source) || 
             hints.isMaximum(source)
            )
           ) {
			return false;
		}

        for (final MagicCondition condition : conditions) {
            if (!condition.accept(game,source)) {
                return false;
            }
        }
		
        // Check for legal targets.
		final boolean useTargetHints = useHints || GeneralConfig.getInstance().getSmartTarget();
		return game.hasLegalTargets(player,source,targetChoice,useTargetHints);
	}
	
	@Override
	public int compareTo(final MagicActivation other) {
		return Long.signum(id-other.id);
	}
	
	abstract boolean usesStack();
	
	protected abstract MagicEvent[] getCostEvent(final MagicSource source);
	
	abstract MagicEvent getEvent(final MagicSource source);
	
	abstract MagicTargetChoice getTargetChoice();	
}
