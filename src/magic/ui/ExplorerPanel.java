package magic.ui;

import magic.data.CardImagesProvider;
import magic.model.MagicCardDefinition;
import magic.model.MagicCubeDefinition;
import magic.model.MagicPlayerDefinition;
import magic.model.MagicPlayerProfile;
import magic.ui.viewer.CardViewer;
import magic.ui.viewer.DeckStatisticsViewer;
import magic.ui.viewer.DeckStrengthViewer;
import magic.ui.widget.ZoneBackgroundLabel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SpringLayout;


public class ExplorerPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	public static final int ALL = 0;
	public static final int LAND = 1;
	public static final int SPELL = 2;	
	
	private static final String CLOSE_BUTTON_TEXT = "Close";
	private static final String SWAP_BUTTON_TEXT = "Swap Selected Cards";
	private static final String CARD_POOL_TITLE = "Card Pool";
	private static final String DECK_TITLE = "Deck";
 	private static final int SPACING=10;
	private static final Dimension STATS_VIEWER_SIZE = new Dimension(270, 170);
	
 	private final MagicFrame frame;
	private final MagicPlayerDefinition player;
	private final DeckStatisticsViewer remoteStatsViewer;
	
	private final CardTable cardPoolTable;
	private final CardTable deckTable;
 	private final ZoneBackgroundLabel backgroundImage;
	private final CardViewer cardViewer;
	private final DeckStatisticsViewer statsViewer;
	private final ExplorerFilterPanel filterPanel;
	private final JButton closeButton;
	private final JButton swapButton;
	
	private List<MagicCardDefinition> cardPoolDefs;
	private List<MagicCardDefinition> deckDefs;
	
	public ExplorerPanel(final MagicFrame frame, final int mode, final MagicPlayerDefinition player, final MagicCubeDefinition cube, final DeckStatisticsViewer remoteStatsViewer) {
		this.frame=frame;
		this.player=player;
		this.remoteStatsViewer = remoteStatsViewer;
		
		final SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		// buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setOpaque(false);
		
		// create swap button for deck editing
		if (isEditingDeck()) {
			swapButton = new JButton(SWAP_BUTTON_TEXT);
			swapButton.setFocusable(false);
			swapButton.addActionListener(this);
			buttonsPanel.add(swapButton);
			
			buttonsPanel.add(Box.createHorizontalStrut(SPACING));			
		} else {
			swapButton = null;
		}
		
		// close button
		closeButton = new JButton(CLOSE_BUTTON_TEXT);
		closeButton.setFocusable(false);
		closeButton.addActionListener(this);
		buttonsPanel.add(closeButton);
		
		add(buttonsPanel);
		
		// left side (everything but buttons)
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setOpaque(false);
		
		// card image
		cardViewer = new CardViewer("",false,true);
		cardViewer.setPreferredSize(CardImagesProvider.CARD_DIMENSION);
		cardViewer.setMaximumSize(CardImagesProvider.CARD_DIMENSION);
		leftPanel.add(cardViewer);
		
		// deck statistics
		if (isEditingDeck()) {
			leftPanel.add(Box.createVerticalStrut(SPACING));
			statsViewer = new DeckStatisticsViewer();
			statsViewer.setPreferredSize(STATS_VIEWER_SIZE);
			statsViewer.setMaximumSize(STATS_VIEWER_SIZE);
			leftPanel.add(statsViewer);
		} else {
			statsViewer = null;
		}
		
		// add scrolling to left side
		JScrollPane leftScrollPane = new JScrollPane(leftPanel);
		leftScrollPane.setBorder(null);
		leftScrollPane.setOpaque(false);
		leftScrollPane.getViewport().setOpaque(false);
		add(leftScrollPane);
		
		// filters
		MagicPlayerProfile profile=null;
		if (isEditingDeck()) {
			profile=getPlayer().getProfile();
		}
		filterPanel = new ExplorerFilterPanel(this, mode, profile, cube);
		
		final JScrollPane filterScrollPane = new JScrollPane(filterPanel);
		filterScrollPane.setBorder(null);
		filterScrollPane.setOpaque(false);
		filterScrollPane.getViewport().setOpaque(false);
		add(filterScrollPane);
		
		// card pool
		cardPoolDefs = filterPanel.getCardDefinitions();
		cardPoolTable = (isEditingDeck()) ? new CardTable(cardPoolDefs, cardViewer, CARD_POOL_TITLE) : new CardTable(cardPoolDefs, cardViewer);

		// deck
		final Container cardsPanel; // reference panel holding both card pool and deck
		
		if (isEditingDeck()) {
			deckDefs = getPlayer().getDeck();
			deckTable = new CardTable(deckDefs, cardViewer, DECK_TITLE);
			
			JSplitPane cardsSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			cardsSplitPane.setOneTouchExpandable(true);
			cardsSplitPane.setLeftComponent(cardPoolTable);
			cardsSplitPane.setRightComponent(deckTable);
			cardsSplitPane.setResizeWeight(0.7);
			
			add(cardsSplitPane);
			cardsPanel = cardsSplitPane;
			
			// update deck stats
			statsViewer.setPlayer(getPlayer());
		} else {
			// no deck
			deckDefs = null;
			deckTable = null;
			
			add(cardPoolTable);
			cardsPanel = cardPoolTable;
		}
		
		// background - must be added last or anything else
		// will be hidden behind it
		backgroundImage = new ZoneBackgroundLabel();
		add(backgroundImage);
		
		// set sizes by defining gaps between components
		Container contentPane = this;
		
		// background's gaps with top left bottom and right are 0
		// (i.e., it fills the window)
        springLayout.putConstraint(SpringLayout.WEST, backgroundImage,
                             0, SpringLayout.WEST, contentPane);
        springLayout.putConstraint(SpringLayout.NORTH, backgroundImage,
                             0, SpringLayout.NORTH, contentPane);
        springLayout.putConstraint(SpringLayout.EAST, backgroundImage,
                             0, SpringLayout.EAST, contentPane);
        springLayout.putConstraint(SpringLayout.SOUTH, backgroundImage,
                             0, SpringLayout.SOUTH, contentPane);		
		
		// card image's gap (top left)
        springLayout.putConstraint(SpringLayout.WEST, leftScrollPane,
                             SPACING, SpringLayout.WEST, backgroundImage);
        springLayout.putConstraint(SpringLayout.NORTH, leftScrollPane,
                             SPACING, SpringLayout.NORTH, backgroundImage);
				
        // gap between card image and filter
        springLayout.putConstraint(SpringLayout.WEST, filterScrollPane,
                             SPACING, SpringLayout.EAST, leftScrollPane);
							 
		// filter panel's gaps (top right)
        springLayout.putConstraint(SpringLayout.NORTH, filterScrollPane,
                             0, SpringLayout.NORTH, leftScrollPane);
        springLayout.putConstraint(SpringLayout.EAST, filterScrollPane,
                             -SPACING, SpringLayout.EAST, backgroundImage);
		
		// filter panel's gap with card tables
        springLayout.putConstraint(SpringLayout.WEST, cardsPanel,
                             0, SpringLayout.WEST, filterScrollPane);
        springLayout.putConstraint(SpringLayout.NORTH, cardsPanel,
                             SPACING, SpringLayout.SOUTH, filterScrollPane);
		
		// card tables' gap (right)
		springLayout.putConstraint(SpringLayout.EAST, cardsPanel,
							 -SPACING, SpringLayout.EAST, backgroundImage);
		springLayout.putConstraint(SpringLayout.SOUTH, cardsPanel,
								 -SPACING, SpringLayout.SOUTH, backgroundImage);
							 
		// buttons' gap (top right bottom)
        springLayout.putConstraint(SpringLayout.EAST, buttonsPanel,
                             0, SpringLayout.EAST, leftScrollPane);
        springLayout.putConstraint(SpringLayout.SOUTH, leftScrollPane,
                             -SPACING, SpringLayout.NORTH, buttonsPanel);
        springLayout.putConstraint(SpringLayout.SOUTH, buttonsPanel,
                             -SPACING, SpringLayout.SOUTH, backgroundImage);
		
		// set initial card image
		if (cardPoolDefs.isEmpty()) {
			cardViewer.setCard(MagicCardDefinition.UNKNOWN,0);
 		} else {
 			cardViewer.setCard(cardPoolDefs.get(0),0);
 		}
	}
	
	private boolean isEditingDeck() {
		return player != null;
	}
	
	private MagicPlayerDefinition getPlayer() {
		return player;
	}
	
	public void updateCardPool() {
		cardPoolDefs = filterPanel.getCardDefinitions();
		cardPoolTable.setCards(cardPoolDefs);
	}
	
	public void updateDeck() {
		deckDefs = getPlayer().getDeck();
		deckTable.setCards(deckDefs);
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
	
		final Object source=event.getSource();
		
		if (source == closeButton) {
			frame.closeCardExplorer();
		} else if (source == swapButton && isEditingDeck()) {
			MagicCardDefinition cardPoolCard = cardPoolTable.getSelectedCard();
			MagicCardDefinition deckCard = deckTable.getSelectedCard();
			if (cardPoolCard != null && deckCard != null) {
				getPlayer().getDeck().remove(deckCard);
				getPlayer().getDeck().add(cardPoolCard);
				updateDeck();
			
				// update deck stats
				statsViewer.setPlayer(getPlayer());
				
				// update remote stats
				if (remoteStatsViewer != null) {
					remoteStatsViewer.setPlayer(getPlayer());
				}
			}
		}
	}
}
