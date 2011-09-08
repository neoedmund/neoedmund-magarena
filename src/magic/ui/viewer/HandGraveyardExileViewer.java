package magic.ui.viewer;

import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.TabSelector;
import magic.ui.widget.TitleBar;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;

public class HandGraveyardExileViewer extends JPanel implements ChangeListener {

	private static final long serialVersionUID = 1L;

	private final CardListViewer viewers[];
	private final JPanel cardPanel;
	private final CardLayout cardLayout;
	private final TitleBar titleBar;
	private final TabSelector tabSelector;

	public HandGraveyardExileViewer(final ViewerInfo viewerInfo,final GameController controller) {

		final Theme theme=ThemeFactory.getInstance().getCurrentTheme();
		
		viewers=new CardListViewer[]{
			new HandViewer(viewerInfo,controller),
			new GraveyardViewer(viewerInfo,controller,false),
			new GraveyardViewer(viewerInfo,controller,true),
			new ExileViewer(viewerInfo,controller,false),
			new ExileViewer(viewerInfo,controller,true)
		};
				
		setOpaque(false);
		setLayout(new BorderLayout());
		
		titleBar=new TitleBar("");
		add(titleBar,BorderLayout.NORTH);
						
		cardLayout=new CardLayout();
		cardPanel=new JPanel(cardLayout);
		cardPanel.setOpaque(false);
		for (int index=0;index<viewers.length;index++) {
			
			cardPanel.add(viewers[index],String.valueOf(index));
		}		
		add(cardPanel,BorderLayout.CENTER);
		
		final String playerName=viewerInfo.getPlayerInfo(false).name;
		final String opponentName=viewerInfo.getPlayerInfo(true).name;
		
		tabSelector=new TabSelector(this,false);
		tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_HAND),"Hand : "+playerName);
		tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_GRAVEYARD),"Graveyard : "+playerName);
		tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_GRAVEYARD),"Graveyard : "+opponentName);
		tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_EXILE),"Exile : "+playerName);
		tabSelector.addTab(theme.getIcon(Theme.ICON_SMALL_EXILE),"Exile : "+opponentName);
		titleBar.add(tabSelector,BorderLayout.EAST);
		
		viewers[0].viewCard();
	}
	
	public void update() {
		
		for (final CardListViewer viewer : viewers) {
			
			viewer.update();
		}
	}
	
	public void setSelectedTab(final int selectedTab) {
		
		if (selectedTab>=0) {
			tabSelector.setSelectedTab(selectedTab);
		}
	}

	@Override
	public void stateChanged(final ChangeEvent e) {

		final int selectedTab=tabSelector.getSelectedTab();
		cardLayout.show(cardPanel,Integer.toString(selectedTab));
		titleBar.setText(viewers[selectedTab].getTitle());
	}
}
