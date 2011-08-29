package magic.model;

import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

import magic.model.action.MagicAction;
import magic.model.stack.MagicItemOnStack;
import magic.MagicMain;
import magic.data.FileIO;

public class MagicGameReport {

	private static void buildCard(final MagicGame game,final String place,final MagicCard card,final StringBuilder report) {
		report.append("   - ").append(place).append(" : ").append(card.getName()).append("\n");
	}
	
	private static void buildPermanent(final MagicGame game,final MagicPermanent permanent,final StringBuilder report) {
		report.append("   - Permanent : ").append(permanent.getName());
		if (permanent.isCreature()) {
			final MagicPowerToughness pt=permanent.getPowerToughness(game);
			report.append("  Power : ").append(pt.power);
			report.append("  Toughness : ").append(pt.toughness);
			report.append("  Damage : ").append(permanent.getDamage());
		}
		if (permanent.hasState(MagicPermanentState.Tapped)) {
			report.append("  Tapped");
		}
		if (permanent.hasState(MagicPermanentState.Summoned)) {
			report.append("  Summoned");
		}
		report.append("\n");
	}
	
	private static void buildPlayer(final MagicGame game,final MagicPlayer player,final StringBuilder report) {
		report.append(player.getIndex()).append("] ");
		report.append("Player : ").append(player.getName());
		report.append("  Life : ").append(player.getLife());
		report.append("  Delayed : ").append(player.getBuilderCost());
		report.append("\n");

		for (final MagicCard card: player.getHand()) {
			buildCard(game,"Hand",card,report);
		}

		for (final MagicCard card: player.getGraveyard()) {
			buildCard(game,"Graveyard",card,report);
		}
		
		for (final MagicPermanent permanent : player.getPermanents()) {
			buildPermanent(game,permanent,report);
		}
	}
	
	private static void buildStack(final MagicGame game,final StringBuilder report) {
		report.append("Stack : ").append(game.getStack().size()).append('\n');
		for (final MagicItemOnStack itemOnStack : game.getStack()) {
			report.append("   - Name : ");
            report.append(itemOnStack.getName());
            report.append("  Player : ");
            report.append(itemOnStack.getController().getName());
            report.append('\n');
		}
	}

	private static void buildScore(final MagicGame game,final StringBuilder report) {
		int totalScore=0,count=0;
		for (final MagicAction action : game.getActions()) {
			final int score=action.getScore(game.getScorePlayer());
			totalScore+=score;
			final String text=action.toString();
			if (!text.isEmpty()) {
				report.append(++count).append(". ").append(text).append(" = ").append(score).append("\n");
			}
		}
		report.append("Score = ").append(totalScore).append("\n");
	}
	
	public static String buildReport(final MagicGame game) {
		final StringBuilder report=new StringBuilder();
		report.append("Turn : ").append(game.getTurn());
		report.append("  Phase : ").append(game.getPhase().getType());
		report.append("  Step : ").append(game.getStep());
		report.append("  Player : ").append(game.getTurnPlayer());
		report.append("  Score : ").append(game.getScore());
		report.append("\n");
		
		for (final MagicPlayer player : game.getPlayers()) {
			buildPlayer(game,player,report);
		}

		buildStack(game,report);
		buildScore(game,report);		
		return report.toString();
	}

    public static void buildReport(final MagicGame game, final Throwable ex) {
        final StringBuilder sb = new StringBuilder();
        sb.append("CRASH REPORT CREATED ON " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")).format(new Date()));
        sb.append('\n');
        try {
            //buildReport might throw an exception
            sb.append(buildReport(game));
            sb.append('\n');
        } catch (final Throwable ex2) {
            sb.append("Exception from MagicGameReport.buildReport: " + ex2.getMessage());
            sb.append('\n');
            final StringWriter result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            ex2.printStackTrace(printWriter);
            sb.append(result.toString());
            sb.append('\n');
        }
        sb.append("Exception from controller.runGame: " + ex.getMessage());
        sb.append('\n');
        final StringWriter result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        sb.append(result.toString());
        sb.append('\n');

        //print a copy to stderr
        System.err.println(sb.toString());

        //save a copy to a crash log file
        File clog = new File(MagicMain.getGamePath(), "crash.log");
        try {
            FileIO.toFile(clog, sb.toString(), true); 
        } catch (final IOException ex3) {
            System.err.println("Unable to save crash log");
        }
    }
}
