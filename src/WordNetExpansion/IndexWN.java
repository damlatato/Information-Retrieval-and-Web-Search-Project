package WordNetExpansion;
import java.io.File;

import org.apache.lucene.wordnet.Syns2Index;

public class IndexWN {

	public static String[] path = new String[2];
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		
		// Index WordNet synonyms
		Syns2Index indexWN = new Syns2Index();
		
		path[0] = "/Users/dhosny/Downloads/Lucene files/Prolog/wn_s.pl";
		path[1] = "/Users/dhosny/Downloads/Lucene files/Prolog/IndexWN/";

		indexWN.main(path);

		System.out.println("indexing successful");
	}

}
