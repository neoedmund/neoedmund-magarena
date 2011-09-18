package magic.ui.viewer;

import magic.ui.GameController;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ImagePermanentsViewer extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int POSITION_SPACING = 60;
	private static final int HORIZONTAL_SPACING = 40;
	private static final int VERTICAL_SPACING = 30;

	private final GameController controller;
	private List<ImagePermanentViewer> viewers;
	private Set<Object> validChoices;
	
	public ImagePermanentsViewer(final GameController controller) {
		this.controller=controller;
		
		setLayout(null);
		setOpaque(false);
		
		viewers=Collections.emptyList();
		validChoices=Collections.emptySet();
	}

	private static int divideOverXRows(final int numRows, final List<ImagePermanentViewer> cards, final int maxCardsPerRow) {
		int numCardsThisRow = 0;
		int currentRow = 1;
		
		for(ImagePermanentViewer card : cards) {
			if(numCardsThisRow + 1 > maxCardsPerRow) {
				// goto next row
				currentRow++;
				numCardsThisRow = 0;
			}
			
			numCardsThisRow++;
			card.setLogicalRow(currentRow);
		}
		
		return currentRow;		
	}

	private int calculatePositions(final List<ImagePermanentViewer> aViewers) {
		int currentRow=1;
		int x=0;
		int y=0;
		int maxWidth=0;
		int rowHeight=0;
		int prevPosition=aViewers.get(0).getPosition();

		for (final ImagePermanentViewer viewer : aViewers) {
		
			if (currentRow!=viewer.getLogicalRow()) {
				currentRow++;
				x=0;
				y+=VERTICAL_SPACING+rowHeight;
			}
			if (viewer.getPosition()!=prevPosition) {
				prevPosition=viewer.getPosition();
				if (x>0) {
					x+=POSITION_SPACING;
				}
			}
			viewer.setLogicalPosition(new Point(x,y));
			final Dimension logicalSize=viewer.getLogicalSize();
			x+=logicalSize.width;
			maxWidth=Math.max(maxWidth,x);
			x+=HORIZONTAL_SPACING;
			rowHeight=Math.max(rowHeight,logicalSize.height);
		}	

		final int maxHeight=y+rowHeight;
		final int width=getWidth();
		final int height=getHeight();
		int scaleMult=width;
		int scaleDiv=maxWidth;
		if ((maxHeight*scaleMult)/scaleDiv>height) {
			scaleMult=height;
			scaleDiv=maxHeight;
		}
		if (scaleMult>scaleDiv/2) {
			scaleMult=scaleDiv/2;
		}
		
		for (final ImagePermanentViewer viewer : aViewers) {
			final Point point=viewer.getLogicalPosition();
			viewer.setLocation((point.x*scaleMult)/scaleDiv,(point.y*scaleMult)/scaleDiv);
			final Dimension size=viewer.getLogicalSize();
			viewer.setSize((size.width*scaleMult)/scaleDiv,(size.height*scaleMult)/scaleDiv);
		}
		
		return (1000*scaleMult)/scaleDiv;
	}

	private void calculateOptimalPositions(final List<ImagePermanentViewer> cards) {		
		final float screenWidth = (float) getWidth();
		final float screenHeight = (float) getHeight();
		final int numCards = cards.size();
		
		if(numCards > 0 && screenWidth > 0 && screenHeight > 0) { // ignore cases where drawing doesn't matter
			final float cardWidth = (float) cards.get(0).getLogicalSize().getWidth();
			final float cardHeight = (float) cards.get(0).getLogicalSize().getHeight();
			
			final float cardAspectRatio = cardWidth / cardHeight;
			
			int r;
			int bestNumRows = 1;
			int maxCardsForBestNumRow = 0;
			float largestScaledCardSize = 0;
		
			// approximate number of rows needed to contain all the cards
			for(r = 1; r < Math.sqrt(numCards); r++) {
				final float numCardsPerRow = (float) Math.ceil((float) numCards / r); // avoid lost of precision
				
				// max width and height for a card using this number of rows
				float scaledCardHeight = screenHeight / r;
				float scaledCardWidth = screenWidth / numCardsPerRow;
				
				// change width or height to maintain aspect ratio
				if (scaledCardWidth / scaledCardHeight > cardAspectRatio) {
					// height is limiting factor on size of scaled card
					scaledCardWidth = (scaledCardHeight / cardHeight) * cardWidth;
				} else {
					// width is limiting factor on size of scaled card
					scaledCardHeight = (scaledCardWidth / cardWidth) * cardHeight;;
				}
				
				// set best possible
				final float scaledCardSize = scaledCardWidth * scaledCardHeight;
				if(scaledCardSize  > largestScaledCardSize) {
					largestScaledCardSize = scaledCardSize;
					bestNumRows = r;
					maxCardsForBestNumRow = (int) numCardsPerRow;
				}
			}
			
			divideOverXRows(bestNumRows, cards, maxCardsForBestNumRow);
			calculatePositions(cards);
		}
	}
	
	public void viewPermanents(final Collection<PermanentViewerInfo> permanentInfos) {
		final List<ImagePermanentViewer> newViewers=new ArrayList<ImagePermanentViewer>();
		for (final PermanentViewerInfo permanentInfo : permanentInfos) {
			newViewers.add(new ImagePermanentViewer(this,permanentInfo));
		}
		calculateOptimalPositions(newViewers);
		removeAll();
		for (final ImagePermanentViewer viewer : newViewers) {
			add(viewer);
		}		
		viewers=newViewers;		
		revalidate();
		repaint();
	}
	
	public GameController getController() {
		return controller;
	}
	
	public void showValidChoices(final Set<Object> aValidChoices) {
		this.validChoices=aValidChoices;
		for (final ImagePermanentViewer viewer : viewers) {
			viewer.repaint();
		}
	}
	
	public boolean isValidChoice(final PermanentViewerInfo permanentInfo) {
		return validChoices.contains(permanentInfo.permanent);
	}
}
