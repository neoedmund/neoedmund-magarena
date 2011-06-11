package magic.model.choice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.ui.GameController;

public class MagicExcludeChoice extends MagicChoice {

	public static final MagicChoice INSTANCE=new MagicExcludeChoice();
	private static final Collection<Object> EMPTY_EXCLUDE_RESULT=Collections.<Object>singleton(new MagicExcludeResult());
	
	private MagicExcludeChoice() {
		super("AI Exclude Mana or Combat Choice");
	}
	
    public static MagicChoice getInstance() {
		return INSTANCE;
	}
	
	@Override
	public Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
		
        final List<MagicPermanent> excludePermanents=new ArrayList<MagicPermanent>();
		for (final MagicPermanent permanent : player.getPermanents()) {
			if (permanent.getCardDefinition().hasExcludeManaOrCombat()) {
				excludePermanents.add(permanent);
			}
		}		
		
		if (excludePermanents.isEmpty()) {
			return EMPTY_EXCLUDE_RESULT;
		}
		
		// In later turns, favour mana over combat when there are more than one exclude permanents.
		if (game.getRelativeTurn() > 1 && excludePermanents.size() > 1) {
			return Collections.<Object>singleton(new MagicExcludeResult(excludePermanents,0));
		}

        //MEM possible combinatorial explosion that could lead to out of memory 
        final int numOptions = (1<<excludePermanents.size())-1;
		final List<Object> excludeOptions = new ArrayList<Object>();
		for (int flags = numOptions; flags >= 0; flags--) {
			excludeOptions.add(new MagicExcludeResult(excludePermanents,flags));
		}
		return excludeOptions;
	}

	@Override
	public Object[] getPlayerChoiceResults(
            final GameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) {
		// Should be done only by AI player.
		throw new UnsupportedOperationException();
	}
}
