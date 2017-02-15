import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class returnDefinition {
	public static File indexDir = new File("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\indexDir\\");
	public static Directory directory;
	public static IndexSearcher indexSearcher;
	public static String definition = null;

	// Return definition of query term
	public static String returnTermDefinition(ScoreDoc[] hits, String term, ArrayList<levDistance> items) throws CorruptIndexException, IOException, ParseException
	{
		directory = FSDirectory.open(indexDir); 
		indexSearcher = new IndexSearcher(directory);
		String newTerm = "";
		//return definition of term if exists
	        for (int i = 0; i < hits.length; i++) 
	        {
	            int docId = hits[i].doc;
	            Document d = indexSearcher.doc(docId);
	            
	            // Get path of document retrieved
	            String name = d.get("filename");
	            
	            Path p = Paths.get(name);
	            String file = p.getFileName().toString();
	            
	            // Get position of filename eg. disinfection.txt
	             
	            newTerm = term.substring(1, term.length()-2);
	            String newFilename = file.substring(0, file.length()-4);
//	            System.out.println("term is : " + newTerm );
            
	            // Get content of the document if term searched matches retrieved docs
	            if (newTerm.equals(newFilename))
	            {
	            	 definition = d.getField("contents").stringValue();
	            	 
	            	 break;
	            }
	            
	        }
//	        System.out.println("List of Related words: " );
            computeSimilarityRelated.relatedTermsSimilarity(newTerm, definition);
	        System.out.println("Definition of " + newTerm + " is: ");

		return definition;
	}

}
