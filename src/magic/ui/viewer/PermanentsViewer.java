package magic.ui.viewer;

import magic.ui.GameController;
import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.TitleBar;
import magic.ui.widget.ViewerScrollPane;

import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class PermanentsViewer extends JPanel implements ChoiceViewer {

	private static final long serialVersionUID = 1L;
	
	private static final Dimension SEPARATOR_DIMENSION=new Dimension(0,10);

	final ViewerInfo viewerInfo;
	TitleBar titleBar;
	private final GameController controller;

	private final Collection<ChoiceViewer> targetViewers;
	private final ViewerScrollPane viewerPane;
	
	PermanentsViewer(final ViewerInfo viewerInfo, final GameController controller) {
		this.viewerInfo=viewerInfo;
		this.controller=controller;
		setOpaque(false);

		targetViewers=new ArrayList<ChoiceViewer>();
		
		controller.registerChoiceViewer(this);
		
		setLayout(new BorderLayout());
		
		viewerPane=new ViewerScrollPane();
		add(viewerPane,BorderLayout.CENTER);
	}

	public final void update() {
		final int maxWidth=getWidth()-25;
		
		if (titleBar!=null) {
			titleBar.setText(getTitle());
		}

		targetViewers.clear();
		final JPanel contentPanel=viewerPane.getContent();		
		final JPanel basicLandsPanel=new JPanel(null);
		basicLandsPanel.setOpaque(false);
		contentPanel.add(basicLandsPanel);

		final Color separatorColor=ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_SEPARATOR_BACKGROUND);
		PermanentViewerInfo previousPermanentInfo=null;
		final SortedSet<PermanentViewerInfo> basicLands=new TreeSet<PermanentViewerInfo>(PermanentViewerInfo.NAME_COMPARATOR);
		for (final PermanentViewerInfo permanentInfo : getPermanents()) {
			if (permanentInfo.basic) {
				basicLands.add(permanentInfo);
				previousPermanentInfo=permanentInfo;
				continue;
			}			
			if (previousPermanentInfo!=null&&isSeparated(previousPermanentInfo,permanentInfo)) {
				final JPanel separatorPanel=new JPanel();
				separatorPanel.setPreferredSize(SEPARATOR_DIMENSION);
				separatorPanel.setBackground(separatorColor);
				separatorPanel.setOpaque(true);
				contentPanel.add(separatorPanel);
			}
			previousPermanentInfo=permanentInfo;
			final PermanentPanel panel=new PermanentPanel(permanentInfo,controller,getBorder(permanentInfo),maxWidth);
			targetViewers.add(panel);
			contentPanel.add(panel);
		}
		
		if (!basicLands.isEmpty()) {
			basicLandsPanel.setLayout(new GridLayout(1,8));
			for (final PermanentViewerInfo landPermanentInfo : basicLands) {
				final BasicLandPermanentButton button=new BasicLandPermanentButton(landPermanentInfo,controller);
				basicLandsPanel.add(button);
				targetViewers.add(button);
			}
		}

		viewerPane.switchContent();
		repaint();
	}
	
	public boolean isEmpty() {
		return targetViewers.isEmpty();
	}
	
	@Override
	public void showValidChoices(final Set<Object> validChoices) {
		for (final ChoiceViewer targetViewer : targetViewers) {
			targetViewer.showValidChoices(validChoices);
		}
	}
	
	protected abstract String getTitle();
	
	protected abstract Collection<PermanentViewerInfo> getPermanents();
	
	protected abstract boolean isSeparated(final PermanentViewerInfo permanent1, final PermanentViewerInfo permanent2);
	
	protected abstract Border getBorder(final PermanentViewerInfo permanentInfo);
}
