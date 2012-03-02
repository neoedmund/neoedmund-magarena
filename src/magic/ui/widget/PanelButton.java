package magic.ui.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;

public abstract class PanelButton extends TexturedPanel {

	private static final long serialVersionUID = 1L;
	
	private final JPanel layeredPanel;
	private final JPanel overlayPanel;
	
	public PanelButton() {
		setLayout(new BorderLayout());
		setBorder(FontsAndBorders.UP_BORDER);
		
		// create subpanel with overlay manager so color overlay and buttons overlap
		layeredPanel = new JPanel();
		OverlayLayout layout = new OverlayLayout(layeredPanel);
		layeredPanel.setLayout(layout);
		
		// color overlay
		overlayPanel = new JPanel();
		overlayPanel.setVisible(false);
		overlayPanel.setAlignmentX(0.5f); // align center middle
		overlayPanel.setAlignmentY(0.5f);
		layeredPanel.add(overlayPanel);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent event) {
				setBorder(FontsAndBorders.DOWN_BORDER);
			}

			@Override
			public void mouseReleased(final MouseEvent event) {
				setBorder(FontsAndBorders.UP_BORDER);
				if (PanelButton.this.contains(event.getX(),event.getY())) {
					PanelButton.this.mouseClicked();
				}
			}

			@Override
			public void mouseEntered(final MouseEvent event) {
				PanelButton.this.mouseEntered();
			}

			@Override
			public void mouseExited(final MouseEvent event) {
				PanelButton.this.mouseExited();
			}
		});
		
		add(layeredPanel, BorderLayout.CENTER);
	}
	
	public void setValid(final boolean valid) {
		overlayPanel.setBackground(getValidColor());
		overlayPanel.setVisible(valid);
	}
	
	public void setComponent(final JComponent component) {
		layeredPanel.add(component);
		component.setAlignmentX(0.5f); // align center middle
		component.setAlignmentY(0.5f);
	}
	
	public void mouseClicked() {}
	
	public void mouseEntered() {}
	
	public void mouseExited() {}
	
	public abstract Color getValidColor();
}
