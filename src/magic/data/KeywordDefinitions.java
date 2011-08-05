package magic.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KeywordDefinitions {

	private static final KeywordDefinitions INSTANCE=new KeywordDefinitions();

	private static final String KEYWORDS_FILENAME="keywords.txt";
	
	private final List<KeywordDefinition> keywordDefinitions;
	
	private KeywordDefinitions() {
		keywordDefinitions=new ArrayList<KeywordDefinition>();
	}
	
	public void loadKeywordDefinitions() {
		keywordDefinitions.clear();
        String content = null;
        try {
            content = FileIO.toStr(this.getClass().getResourceAsStream(KEYWORDS_FILENAME));
        } catch (final IOException ex) {
            System.err.println("ERROR! Unable to load " + KEYWORDS_FILENAME);
            return;
        }

        final Scanner sc = new Scanner(content);
        KeywordDefinition current = null;
		while (sc.hasNextLine()) {
			final String line=sc.nextLine();
			if (line.startsWith("*")) {
			    current = new KeywordDefinition();
				current.name=line.substring(1).trim();
				keywordDefinitions.add(current);
			} else {
				if (current.description.length() > 0) {
					current.description=current.description+"<br>"+line.trim();
				} else {
					current.description=line.trim();
				}
			}
		}
	}
	
	public List<KeywordDefinition> getKeywordDefinitions() {
		return keywordDefinitions;
	}
	
	public static KeywordDefinitions getInstance() {
		return INSTANCE;
	}
	
	public static class KeywordDefinition {
		public String name;
		public String description="";
	}
}
