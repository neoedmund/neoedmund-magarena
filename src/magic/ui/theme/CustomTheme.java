package magic.ui.theme;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.ImageIcon;

import magic.data.IconImages;

public class CustomTheme extends AbstractTheme {

	private static final String THEME_PROPERTIES_FILE="theme.properties";

	private static final int MAX_AVATARS=100;
	
	private final File file;
	private ZipFile zipFile=null;
	private boolean loaded=false;
	private PlayerAvatar playerAvatars[];
	private int nrOfAvatars=0;
	
	public CustomTheme(final File file,final String name) {
		super(name);
		this.file=file;
		playerAvatars=new PlayerAvatar[MAX_AVATARS];
	}
	
	@Override
	public int getNumberOfAvatars() {
		if (nrOfAvatars==0) {
			return super.getNumberOfAvatars();
		}
		return nrOfAvatars;
	}

	@Override
	public ImageIcon getAvatarIcon(final int index,final int size) {
		if (nrOfAvatars==0) {
			return super.getAvatarIcon(index, size);
		}
		return playerAvatars[index%nrOfAvatars].getIcon(size);
	}

	private void parseEntry(final String key,final String value) {
		if (value.isEmpty()) {
			return;
		}
		final int index=key.indexOf('_');
		if (index<0) {
			return;
		}
		Object typeValue=null;
		final String type=key.substring(0,index);
		if ("avatar".equals(type)) {
			final int avatarIndex=Integer.parseInt(key.substring(index+1));
			if (avatarIndex>0&&avatarIndex<=MAX_AVATARS) {
				final BufferedImage image=loadImage("avatars/"+value);
				playerAvatars[avatarIndex-1]=new PlayerAvatar(image);
				nrOfAvatars=Math.max(avatarIndex,nrOfAvatars);
			}
			return;
		}
		if ("value".equals(type)) {
			typeValue=Integer.parseInt(value);
		} else if ("color".equals(type)) {
			final String parts[]=value.split(",");
			final int r=Integer.parseInt(parts[0],16);
			final int g=Integer.parseInt(parts[1],16);
			final int b=Integer.parseInt(parts[2],16);
			if (parts.length==4) {
				typeValue=new Color(r,g,b,Integer.parseInt(parts[3],16));
			} else if (parts.length==3) {
				typeValue=new Color(r,g,b);
			}
		} else if ("texture".equals(type)) {
			typeValue=loadImage(value);
		} else if ("icon".equals(type)) {
			typeValue=new ImageIcon(loadImage(value));
		}
		if (typeValue!=null) {
			addToTheme(key,typeValue);
		}
	}
	
	private InputStream getInputStream(final String filename) {
        try {
            if (zipFile!=null) {
                final ZipEntry zipEntry=zipFile.getEntry(filename);
                return zipFile.getInputStream(zipEntry);
            } else {
                return new FileInputStream(new File(file,filename));
            }
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to obtain input stream from " + filename);
            return null;
        }
	}
	
	private BufferedImage loadImage(final String filename) {
        final InputStream ins = getInputStream(filename);
        return magic.data.FileIO.toImg(ins, IconImages.MISSING);
	}
	
	@Override
	public void load() {
		if (loaded) {
			return;
		}
		
        if (file.isFile()) {
            try {
                zipFile=new ZipFile(file);
            } catch (final java.util.zip.ZipException ex) {
                System.err.println("ERROR! Unable to create ZipFile from " + file.getName());
            } catch (final IOException ex) {
                System.err.println("ERROR! Unable to create ZipFile from " + file.getName());
            }
        }
			
        final InputStream inputStream=getInputStream(THEME_PROPERTIES_FILE);
        final Properties properties=new Properties();
        try {
            properties.load(inputStream);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load them properties");
        } finally {
            magic.data.FileIO.close(inputStream);
        }
        for (final Map.Entry<Object,Object> entry : properties.entrySet()) {
            parseEntry(entry.getKey().toString(),entry.getValue().toString().trim());
        }						
	}	
}
