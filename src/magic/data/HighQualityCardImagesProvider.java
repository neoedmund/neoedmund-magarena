package magic.data;

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
	private final Map<String,BufferedImage> scaledImages = 
        new magic.ai.StateCache<String,BufferedImage>(MAX_IMAGES);
	private final Map<String,BufferedImage> origImages = 
        new magic.ai.StateCache<String,BufferedImage>(MAX_IMAGES);
	
	private HighQualityCardImagesProvider() {}
	
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
                return ImageIO.read(imageFile);
            } catch (final Exception ex) {
                System.err.println("ERROR! Unable to read from " + filename);
                return IconImages.MISSING_CARD;
            }
        } else {
            //System.err.println("ERROR! " + filename + " is not a file");
			return IconImages.MISSING_CARD;
		}
	}
	
	@Override
	public BufferedImage getImage(
            final MagicCardDefinition cardDefinition,
            final int index,
            boolean orig) {

		if (cardDefinition == null) {
			return IconImages.MISSING_CARD;
		}

		final String filename=getFilename(cardDefinition,index);

        //put image into the cache
		if (!(origImages.containsKey(filename) && scaledImages.containsKey(filename))) {
            final BufferedImage origImage = loadCardImage(filename);
            origImages.put(filename, origImage);

            final BufferedImage scaledImage = magic.GraphicsUtilities.scale(
                    origImage, 
                    CARD_WIDTH, 
                    CARD_HEIGHT);
            scaledImages.put(filename, scaledImage);
		}

		return orig ? origImages.get(filename) : scaledImages.get(filename);
	}
	
	public static CardImagesProvider getInstance() {
		return INSTANCE;
	}
}
