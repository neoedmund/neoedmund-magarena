package magic.ui.widget;

import magic.ui.theme.Theme;
import magic.ui.theme.ThemeFactory;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class TexturedPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private boolean start;
	private int startX;
	private int startY;
	
	public TexturedPanel() {
		
		setOpaque(false);
		start=false;
	}

	private static int getStart(final int value,final int size) {
		
		int start=(value*97)%size;
		if (start>0) {
			start-=size;
		}
		return start;
	}
	
	public void paintComponent(final Graphics g) {
		
		final BufferedImage image=ThemeFactory.getInstance().getCurrentTheme().getTexture(Theme.TEXTURE_COMPONENT);
		final int imageWidth=image.getWidth();
		final int imageHeight=image.getHeight();
		final int width=this.getWidth();
		final int height=this.getHeight();
		
		if (!start) {
			final Point p=getLocationOnScreen();
			startX=getStart(p.x,imageWidth);
			startY=getStart(p.y,imageHeight);
			start=true;
		}
		
		for (int y=startY;y<height;y+=imageHeight) {
			
			for (int x=startX;x<width;x+=imageWidth) {
				
				g.drawImage(image,x,y,this);
			}
		}		
	}	
}