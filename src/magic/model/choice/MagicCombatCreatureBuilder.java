package magic.model.choice;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicCombatCreatureBuilder {
	
	private static final Comparator<MagicCombatCreature> ATTACKER_COMPARATOR=new Comparator<MagicCombatCreature>() {

 		@Override
		public int compare(final MagicCombatCreature attacker1,final MagicCombatCreature attacker2) {

 			final int adif=attacker1.attackerScore-attacker2.attackerScore;
 			if (adif!=0) {
 				return adif;
 			}
			final int sdif=attacker1.score-attacker2.score;
			if (sdif!=0) {
				return sdif;
			}
			return attacker1.permanent.compareTo(attacker2.permanent);
 		}
	};	
	
	private final MagicGame game;
	private final MagicPlayer attackingPlayer;
	private final MagicPlayer defendingPlayer;
	private SortedSet<MagicCombatCreature> attackers;
	private Set<MagicCombatCreature> blockers;
	
	public MagicCombatCreatureBuilder(final MagicGame game,final MagicPlayer attackingPlayer,final MagicPlayer defendingPlayer) {
		
		this.game=game;
		this.attackingPlayer=attackingPlayer;
		this.defendingPlayer=defendingPlayer;
	}

	/** Must be called before building attackers. */
	public boolean buildBlockers() {
	
		blockers=new HashSet<MagicCombatCreature>();
		for (final MagicPermanent permanent : defendingPlayer.getPermanents()) {
			
			if (permanent.canBlock(game)) {
				blockers.add(new MagicCombatCreature(game,permanent));
			}
		}
		return blockers.size()>0;
	}
	
	private MagicCombatCreature createAttacker(final MagicPermanent permanent) {

		final MagicCombatCreature attacker=new MagicCombatCreature(game,permanent);
		attacker.setAttacker(game,blockers);
		return attacker;
	}

	public boolean buildAttackers() {

		attackers=new TreeSet<MagicCombatCreature>(ATTACKER_COMPARATOR);
		for (final MagicPermanent permanent : attackingPlayer.getPermanents()) {
			
			if (permanent.canAttack(game)) {
				attackers.add(createAttacker(permanent));
			}
		}
		return attackers.size()>0;
	}
	
	public boolean buildBlockableAttackers() {

		attackers=new TreeSet<MagicCombatCreature>(ATTACKER_COMPARATOR);
		for (final MagicPermanent permanent : attackingPlayer.getPermanents()) {
			
			if (permanent.isAttacking()&&permanent.canBeBlocked(game,defendingPlayer)) {
				final MagicCombatCreature attacker=createAttacker(permanent);
				if (attacker.candidateBlockers.length>0) {
					attackers.add(attacker);
				}
			}
		}
		return attackers.size()>0;
	}
		
	public SortedSet<MagicCombatCreature> getAttackers() {
		
		return attackers;
	}
	
	public Set<MagicCombatCreature> getBlockers() {
		
		return blockers;
	}
	
	public Set<MagicPermanent> getCandidateBlockers() {
		
		final Set<MagicPermanent> candidateBlockers=new HashSet<MagicPermanent>();
		for (final MagicCombatCreature attacker : attackers) {
			
			for (final MagicCombatCreature blocker : attacker.candidateBlockers) {
				
				candidateBlockers.add(blocker.permanent);
			}
		}
		return candidateBlockers;		
	}
	
	public Set<MagicPermanent> getBlockableAttackers(final MagicPermanent blocker) {
		
		final Set<MagicPermanent> blockableAttackers=new HashSet<MagicPermanent>();
		for (final MagicCombatCreature attacker : attackers) {

			for (final MagicCombatCreature candidateBlocker : attacker.candidateBlockers) {
				
				if (candidateBlocker.permanent==blocker) {
					blockableAttackers.add(attacker.permanent);
					break;
				}
			}
		}
		return blockableAttackers;
	}
}