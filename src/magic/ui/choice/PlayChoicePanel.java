package magic.ui.choice;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import magic.model.MagicSource;
import magic.model.choice.MagicPlayChoiceResult;
import magic.ui.GameController;
import magic.ui.viewer.GameViewer;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TextLabel;

public class PlayChoicePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE="Choose which ability to play.";
	private static final Dimension BUTTON_DIMENSION=new Dimension(80,35);
	
	private final GameController controller;
	private final List<MagicPlayChoiceResult> results;
	private MagicPlayChoiceResult result=null;
	
	public PlayChoicePanel(
            final GameController controller,
            final MagicSource source,
            final List<MagicPlayChoiceResult> results) {
		
		this.controller=controller;
		this.results=results;
		
		setLayout(new BorderLayout());
		setOpaque(false);
		
		final TextLabel textLabel=new TextLabel(controller.getMessageWithSource(source,MESSAGE),GameViewer.TEXT_WIDTH,true);
		add(textLabel,BorderLayout.CENTER);
		
		final JPanel buttonPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,10,0));
		buttonPanel.setBorder(FontsAndBorders.EMPTY_BORDER);
		buttonPanel.setOpaque(false);
		add(buttonPanel,BorderLayout.SOUTH);
		
		for (int index=0;index<results.size();index++) {
			final JButton button=new JButton(results.get(index).getText());
			button.setPreferredSize(BUTTON_DIMENSION);
			button.setActionCommand(""+index);
			button.addActionListener(this);
			button.setFocusable(false);
			buttonPanel.add(button);
		}
	}

	public MagicPlayChoiceResult getResult() {
		return result;
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
		result=results.get(Integer.parseInt(event.getActionCommand()));
		controller.actionClicked();
	}
}
