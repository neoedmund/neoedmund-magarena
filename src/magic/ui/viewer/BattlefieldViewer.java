package magic.ui.viewer;

import magic.ui.GameController;
import magic.ui.widget.TitleBar;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Collection;

public class BattlefieldViewer extends PermanentsViewer {
		
	private static final long serialVersionUID = 1L;
		
	private final boolean opponent;
	private final PermanentFilter permanentFilter;
	
	public BattlefieldViewer(final ViewerInfo viewerInfo,final GameController controller,final boolean opponent) {
		
		super(viewerInfo,controller);
		this.opponent=opponent;
		permanentFilter=new PermanentFilter(this,controller);
		update();

		titleBar=new TitleBar("");
		add(titleBar,BorderLayout.NORTH);

		final JPanel filterPanel=new JPanel();
		permanentFilter.createFilterButtons(filterPanel,false);
		titleBar.add(filterPanel,BorderLayout.EAST);
	}
	
	@Override
	public String getTitle() {
		
		return "Battlefield : "+viewerInfo.getPlayerInfo(opponent).name;
	}
	
	@Override
	public Collection<PermanentViewerInfo> getPermanents() {

		return permanentFilter.getPermanents(viewerInfo,opponent);
	}

	@Override
	public boolean isSeparated(final PermanentViewerInfo permanentInfo1,final PermanentViewerInfo permanentInfo2) {

		return permanentInfo1.position!=permanentInfo2.position;
	}

	@Override
	public Border getBorder(PermanentViewerInfo permanent) {

		return null;
	}
}