package magic.ui.viewer;

import magic.data.IconImages;
import magic.model.MagicTournament;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

public class TournamentDifficultyViewer extends JPanel implements ChangeListener {
	
	private static final long serialVersionUID = 1L;
	
	public static final Dimension PREFERRED_SIZE = new Dimension(280, 120);

	private final TournamentViewer tournamentViewer;
	private final DifficultyViewer difficultyViewer;
	private final JPanel cardPanel;
	private final CardLayout cardLayout;
	private final TitleBar titleBar;
	private final TabSelector tabSelector;
	
	public TournamentDifficultyViewer(final MagicTournament tournament) {

		tournamentViewer=new TournamentViewer(tournament);
		difficultyViewer=new DifficultyViewer();

		setPreferredSize(PREFERRED_SIZE);
		
		setLayout(new BorderLayout());
		titleBar=new TitleBar("");
		add(titleBar,BorderLayout.NORTH);
		
		cardLayout=new CardLayout();
		cardPanel=new JPanel(cardLayout);
		cardPanel.add(tournamentViewer,"0");
		cardPanel.add(difficultyViewer,"1");
		add(cardPanel,BorderLayout.CENTER);

		tabSelector=new TabSelector(this,false);
		tabSelector.addTab(IconImages.PROGRESS,"Progress");
		tabSelector.addTab(IconImages.DIFFICULTY,"Difficulty");
		titleBar.add(tabSelector,BorderLayout.EAST);
	}

	private void update() {

		switch (tabSelector.getSelectedTab()) {
			case 0: 
				TournamentViewer.setTitle(titleBar);
				break;
			case 1:
				DifficultyViewer.setTitle(titleBar);
				break;
		}		
	}
	
	@Override
	public void stateChanged(final ChangeEvent event) {
		final int selectedTab=tabSelector.getSelectedTab();
		cardLayout.show(cardPanel,Integer.toString(selectedTab));
		update();
	}
}
