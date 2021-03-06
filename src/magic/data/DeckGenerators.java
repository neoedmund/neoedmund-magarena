package magic.data;

import magic.generator.DefaultDeckGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ClassCastException;
import java.lang.InstantiationException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 * Load card definitions from deckgenerators.txt
 */
public class DeckGenerators {

	private static final DeckGenerators INSTANCE = new DeckGenerators();
	
	private static final String FILENAME = "deckgenerators.txt";
	
	private final Map<String, Class> generatorsMap;
	
	private DeckGenerators() {
		generatorsMap = new TreeMap<String, Class>();
	}
	
	public Set<String> getGeneratorNames() {
		return generatorsMap.keySet();
	}
	
	private void addDeckGenerator(final String name, final Class c) {
		generatorsMap.put(name, c);
	}
	
	private void addDeckGenerator(final String name) {
		// find class
        final String cname = name.replaceAll("[^A-Za-z]", "_");
        try { // reflection
			final Class c = Class.forName("magic.generator." + cname + "_DeckGenerator");
            
			addDeckGenerator(name, c);
			
			System.err.println("added deck generator " + name);
        } catch (final ClassNotFoundException ex) {
            // no class found
        } catch (final ClassCastException ex) {
            throw new RuntimeException(ex);
        }
	
	}
	
	public DefaultDeckGenerator getDeckGenerator(final String name) {
		return getDeckGenerator(generatorsMap.get(name));
	}
	
	private DefaultDeckGenerator getDeckGenerator(final Class c) {
		DefaultDeckGenerator gen = null;
		
		if(c != null) {
			try {
				gen = (DefaultDeckGenerator) c.newInstance();
			} catch (final ClassCastException ex) {
		        throw new RuntimeException(ex);
		    } catch (final InstantiationException ex) {
		        throw new RuntimeException(ex);
		    } catch (final IllegalAccessException ex) {
		        throw new RuntimeException(ex);
		    }
		}
		
		return gen;
	}
	
	private void loadDeckGenerators(final String filename) {
		final InputStream stream = this.getClass().getResourceAsStream(filename);
        String content = null;
        try { // load file
            content = FileIO.toStr(stream);
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load deck generators from " + filename);
            return;
        }

		// add entries
        final Scanner sc = new Scanner(content);
		while (sc.hasNextLine()) {
			final String line = sc.nextLine().trim();
            if (line.length() == 0) {
                // blank line
            } else {
                addDeckGenerator(line);
			}
		}
	}
	
	public void loadDeckGenerators() {
		loadDeckGenerators(FILENAME);

		System.err.println(getNrGenerators()+ " deck generators loaded");
	}
	
	public int getNrGenerators() {
		return generatorsMap.size();
	}
	
	public static DeckGenerators getInstance() {
		return INSTANCE;
	}
}
