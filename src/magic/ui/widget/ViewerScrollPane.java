package magic.ui.widget;

import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Rectangle;

public class ViewerScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;

	private JPanel contentPanel=null;
	
	public ViewerScrollPane() {
		
		setOpaque(false);
		getViewport().setOpaque(false);
		setBorder(FontsAndBorders.NO_BORDER);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getVerticalScrollBar().setUnitIncrement(80);
		getVerticalScrollBar().setBlockIncrement(80);
	}
	
	public synchronized JPanel getContent() {
		if (contentPanel==null) {
			contentPanel=new JPanel();
			contentPanel.setBorder(FontsAndBorders.BLACK_BORDER_2);
			contentPanel.setLayout(new BoxLayout(contentPanel,BoxLayout.Y_AXIS));
			contentPanel.setBackground(ThemeFactory.getInstance().getCurrentTheme().getColor(Theme.COLOR_VIEWER_BACKGROUND));
		}
		return contentPanel;
	}
	
	public synchronized void switchContent() {
		if (contentPanel!=null) {
			final Rectangle rect=getViewport().getViewRect();
			final JPanel mainPanel=new JPanel(new BorderLayout());
			mainPanel.setOpaque(false);
			mainPanel.add(contentPanel,BorderLayout.NORTH);
			getViewport().add(mainPanel);
			getViewport().scrollRectToVisible(rect);
			contentPanel=null;
		}
	}
}
