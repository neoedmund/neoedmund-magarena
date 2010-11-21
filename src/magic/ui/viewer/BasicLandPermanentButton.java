package magic.ui.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import magic.data.IconImages;
import magic.ui.GameController;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PanelButton;

public class BasicLandPermanentButton extends PanelButton implements ChoiceViewer {

	private static final long serialVersionUID = 1L;

	private final PermanentViewerInfo permanentInfo;
	private final GameController controller;
	private final JPanel landPanel;
	
	public BasicLandPermanentButton(final PermanentViewerInfo permanentInfo,final GameController controller) {
		
		this.permanentInfo=permanentInfo;
		this.controller=controller;
		
		landPanel=new JPanel(new BorderLayout());
		landPanel.setOpaque(false);
		landPanel.setBorder(FontsAndBorders.NO_TARGET_BORDER);
		
		final JLabel manaLabel=new JLabel();
		manaLabel.setHorizontalAlignment(JLabel.CENTER);
		manaLabel.setPreferredSize(new Dimension(0,30));
		manaLabel.setIcon(permanentInfo.manaColor.getIcon());
		landPanel.add(manaLabel,BorderLayout.CENTER);
		
		final JLabel tappedLabel=new JLabel(permanentInfo.tapped?IconImages.TAPPED:null);
		tappedLabel.setPreferredSize(new Dimension(0,16));
		landPanel.add(tappedLabel,BorderLayout.SOUTH);
		
		setComponent(landPanel);
		showValidChoices(controller.getValidChoices());
	}

	@Override
	public void mouseClicked() {

		controller.processClick(permanentInfo.permanent);
	}

	@Override
	public void mouseEntered() {

		controller.viewCard(permanentInfo.cardDefinition);		
	}
	
	@Override
	public void showValidChoices(final Set<Object> validChoices) {

		if (validChoices.contains(permanentInfo.permanent)) {			
			landPanel.setBorder(FontsAndBorders.TARGET_BORDER);
		} else {
			landPanel.setBorder(FontsAndBorders.NO_TARGET_BORDER);
		}
	}
}