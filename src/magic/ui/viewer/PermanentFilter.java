package magic.ui.viewer;

import magic.data.IconImages;
import magic.ui.GameController;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class PermanentFilter implements ActionListener {

	private static final Comparator<PermanentViewerInfo> PERMANENT_COMPARATOR=new Comparator<PermanentViewerInfo>() {
		
		@Override
		public int compare(final PermanentViewerInfo permanentInfo1,final PermanentViewerInfo permanentInfo2) {
	
			final int positionDif=permanentInfo1.position-permanentInfo2.position;
			if (positionDif!=0) {
				return positionDif;
			}
			
			final int nameDif=permanentInfo1.name.compareTo(permanentInfo2.name);
			if (nameDif!=0) {
				return nameDif;
			}
			
			return permanentInfo1.permanent.compareTo(permanentInfo2.permanent);
		}
	};
	
	private static final ImageIcon FILTER_ICONS[]={
		IconImages.ALL,IconImages.LAND,IconImages.CREATURE,IconImages.ARTIFACT,IconImages.ENCHANTMENT,IconImages.VALID};
	private static final String FILTER_TOOLTIPS[]={
		"All","Mana","Creatures","Artifacts","Enchantments","Choices"
	};
	
	private static final Dimension HORIZONTAL_BUTTON_DIMENSION=new Dimension(28,20);
	private static final Dimension VERTICAL_BUTTON_DIMENSION=new Dimension(24,24);

	private final JPanel viewer;
	private final GameController controller;
	private final JToggleButton filterButtons[];
	private int filter=0;
	
	public PermanentFilter(final JPanel viewer,final GameController controller) {

		this.viewer=viewer;
		this.controller=controller;
		filterButtons=new JToggleButton[PermanentFilter.FILTER_ICONS.length];
	}
	
	public void createFilterButtons(final JPanel filterPanel,final boolean vertical) {

		final Dimension dimension;
		if (vertical) {
			dimension=VERTICAL_BUTTON_DIMENSION;
			filterPanel.setLayout(new GridLayout(PermanentFilter.FILTER_ICONS.length,1));			
		} else {
			dimension=HORIZONTAL_BUTTON_DIMENSION;
			filterPanel.setLayout(new GridLayout(1,PermanentFilter.FILTER_ICONS.length));
		}
		
		for (int index=0;index<filterButtons.length;index++) {
		
			filterButtons[index]=new JToggleButton(PermanentFilter.FILTER_ICONS[index],index==0);
			filterButtons[index].setToolTipText(FILTER_TOOLTIPS[index]);
			filterButtons[index].setPreferredSize(dimension);
			filterButtons[index].setActionCommand(String.valueOf(index));
			filterButtons[index].setFocusable(false);
			filterButtons[index].addActionListener(this);
			filterPanel.add(filterButtons[index]);
		}
	}
	
	public boolean changeFilter(final int newFilter) {
		
		if (filter!=newFilter) {
			filter=newFilter;
			return true;
		}
		return false;
	}
	
	public int getFilter() {
		
		return filter;
	}
	
	public boolean accept(final PermanentViewerInfo permanentInfo) {

		if (filter!=5&&(permanentInfo.attacking||permanentInfo.blocking)) {
			return false;
		}
		
		switch (filter) {
			case 1: return permanentInfo.mana;
			case 2: return permanentInfo.creature;
			case 3: return permanentInfo.artifact;
			case 4: return permanentInfo.enchantment;
			case 5: return controller.getValidChoices().contains(permanentInfo.permanent);
			default: return permanentInfo.root;
		}		
	}
	
	public SortedSet<PermanentViewerInfo> getPermanents(final ViewerInfo viewerInfo,final boolean opponent) {
		
		final PlayerViewerInfo player=viewerInfo.getPlayerInfo(opponent);
		final SortedSet<PermanentViewerInfo> permanents=new TreeSet<PermanentViewerInfo>(PERMANENT_COMPARATOR);
		for (final PermanentViewerInfo permanentInfo : player.permanents) {
			
			if (accept(permanentInfo)) {
				permanents.add(permanentInfo);
			}
		}
		return permanents;
	}
	
	@Override
	public void actionPerformed(final ActionEvent event) {
		
		final int newFilter=Integer.parseInt(event.getActionCommand());
		if (changeFilter(newFilter)) {
			for (int index=0;index<filterButtons.length;index++) {
				
				filterButtons[index].setSelected(index==newFilter);
			}		
			if (viewer instanceof BattlefieldViewer) {
				((BattlefieldViewer)viewer).update();
			} else if (viewer instanceof ImageBattlefieldViewer) {
				((ImageBattlefieldViewer)viewer).update();
			}
		} else {
			filterButtons[newFilter].setSelected(true);
		}
	}	
}