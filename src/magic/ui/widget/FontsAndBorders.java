package magic.ui.widget;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class FontsAndBorders {
	
	public static final Color GRAY1=new Color(200,200,200);
	public static final Color GRAY2=new Color(210,210,210);
		
	public static final Font FONT0=new Font("dialog",Font.PLAIN,10);

	public static final Font FONT1=new Font("dialog",Font.BOLD,12);
	
	public static final Font FONT2=new Font("dialog",Font.BOLD,16);

	public static final Font FONT3=new Font("Dialog",Font.BOLD,20);

	public static final Font FONT4=new Font("dialog",Font.BOLD,24);

	public static final Font FONT5=new Font("dialog",Font.BOLD,32);
	
	public static final Border NO_TARGET_BORDER=BorderFactory.createEmptyBorder(3,3,3,3);

	public static final Border WHITE_BORDER=BorderFactory.createLineBorder(Color.WHITE);
	
	public static final Border BLACK_BORDER=BorderFactory.createLineBorder(Color.BLACK);
	
	public static final Border BLACK_BORDER_2=BorderFactory.createCompoundBorder(
		BorderFactory.createLineBorder(Color.BLACK),
		BorderFactory.createEmptyBorder(2,2,2,2)
	);
		
	public static final Border TABLE_BORDER=BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(1,1,0,0,Color.BLACK),
			BorderFactory.createEmptyBorder(1,1,1,1)
		);
	
	public static final Border TABLE_ROW_BORDER=BorderFactory.createMatteBorder(0,0,0,1,Color.BLACK);

	public static final Border TABLE_BOTTOM_ROW_BORDER=BorderFactory.createMatteBorder(0,0,1,1,Color.BLACK);	

	public static final Border BEVEL_BORDER=BorderFactory.createCompoundBorder(
		BorderFactory.createLoweredBevelBorder(),
		BorderFactory.createEmptyBorder(2,2,2,2)
	);
	
	public static final Border UP_BORDER=BorderFactory.createCompoundBorder( 
		BorderFactory.createRaisedBevelBorder(),
		BorderFactory.createEmptyBorder(0,0,1,1)
	);

	public static final Border DOWN_BORDER=BorderFactory.createCompoundBorder(
		BorderFactory.createLoweredBevelBorder(),
		BorderFactory.createEmptyBorder(1,1,0,0)
	);	

	public static final Border SMALL_EMPTY_BORDER=BorderFactory.createEmptyBorder(1,1,1,1);
	
	public static final Border EMPTY_BORDER=BorderFactory.createEmptyBorder(2,2,2,2);

	public final static Border PLAYER_BORDER=BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0,4,0,0,Color.BLACK),
			BorderFactory.createEmptyBorder(0,3,0,0)
	);

	public final static Border OPPONENT_BORDER=BorderFactory.createCompoundBorder(
		BorderFactory.createMatteBorder(0,4,0,0,Color.RED),
		BorderFactory.createEmptyBorder(0,3,0,0)
	);
	
	public static Border getPlayerBorder(final boolean visible) {
		
		return visible?PLAYER_BORDER:OPPONENT_BORDER;
	}
}