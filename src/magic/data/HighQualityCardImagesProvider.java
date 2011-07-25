package magic.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.imageio.ImageIO;
import magic.MagicMain;
import magic.model.MagicCardDefinition;

/**
 * For a given MagicCardDefinition object returns the corresponding image from
 * the cards directory
 */
public class HighQualityCardImagesProvider implements CardImagesProvider {
	
	private static final CardImagesProvider INSTANCE=new HighQualityCardImagesProvider();
	private static final int MAX_IMAGES=100;
	private final Map<String,Image> imagesMap;
	
	public HighQualityCardImagesProvider() {
		imagesMap = new magic.ai.StateCache<String,Image>(100);
	}
	
	private static final String getFilename(
            final MagicCardDefinition cardDefinition,
            final int index) {
		final int imageIndex=index%cardDefinition.getImageCount();
		final StringBuffer buffer=new StringBuffer();
		buffer.append(MagicMain.getGamePath()).append(File.separator);
		buffer.append(cardDefinition.isToken()?"tokens":"cards").append(File.separator);
		buffer.append(cardDefinition.getImageName());
        buffer.append(imageIndex>0?String.valueOf(imageIndex+1):"");
		buffer.append(IMAGE_EXTENSION);
		return buffer.toString();
	}
	
	private static BufferedImage loadCardImage(final String filename) {
        final File imageFile = new File(filename);
        if (imageFile.isFile()) {
            try {
                final InputStream inputStream=new FileInputStream(imageFile);
                final BufferedImage fullImage=ImageIO.read(inputStream);
                inputStream.close();
                return fullImage;
            } catch (Exception ex) {
                return IconImages.MISSING_CARD;
            }
        } else {
			return IconImages.MISSING_CARD;
		}
	}
	
	@Override
	public Image getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            final boolean high) {

		if (cardDefinition == null) {
			return IconImages.MISSING_CARD;
		}

		final String filename=getFilename(cardDefinition,index);
		Image image = imagesMap.get(filename);

		if (image != null) {
			return image;
		}

		image = loadCardImage(filename).getScaledInstance(
                    CARD_WIDTH,
                    CARD_HEIGHT,
                    Image.SCALE_SMOOTH
                );
        
        imagesMap.put(filename, image);
        
		return image;
	}
	
	public static CardImagesProvider getInstance() {
		return INSTANCE;
	}
}
