package magic.ai;

public class ArtificialChoiceResults {
	
    final Object choiceResults[];
	ArtificialScore aiScore=ArtificialScore.INVALID_SCORE;
	int worker=-1;
	int gameCount=1;

	ArtificialChoiceResults(final Object choiceResults[]) {
		this.choiceResults=choiceResults;
	}
	
	public String toString() {
		final StringBuilder buffer=new StringBuilder();
		buffer.append("[").append(worker).append('/').append(gameCount).append('/').append(aiScore).append("]");
		if (choiceResults!=null) {
			buffer.append(" (");
			boolean first=true;
			for (final Object choiceResult : choiceResults) {
			
				if (first) {
					first=false;
				} else {
					buffer.append(',');
				}
				buffer.append(choiceResult);
			}
			buffer.append(')');
		}
		return buffer.toString();
	}
}
