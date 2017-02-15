import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;




public class relatedHashMap
{

	/**
	 * @param args
	 */
	
	public static File indexDir = new File("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\indexDir\\");
	public static Directory directory;
	public static QueryParser parser;
	public static Query query;
	public static TopDocs topDocs;
	public static ScoreDoc[] hits;
	public static Multimap<String, String> related = ArrayListMultimap.create();
	public static void main(String[] args) throws CorruptIndexException, IOException, SecurityException
	{
		// TODO Auto-generated method stub
				// Open indexDirectory
				directory = FSDirectory.open(indexDir);
				IndexSearcher indexSearcher = new IndexSearcher(directory);
				QueryParser parser = new QueryParser(Version.LUCENE_30, "contents", new StandardAnalyzer(Version.LUCENE_30));
				
				// Read Vocabulary List File
				FileReader input = new FileReader("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\terms\\Vocabulary_terms.txt");
				BufferedReader bufRead = new BufferedReader(input);
				String myLine = null;

				while ( (myLine = bufRead.readLine()) != null)
				{    
					// Initiate phrase query
					PhraseQuery phrase = new PhraseQuery();
					// Slop is the number of allowed strings between each token of the phrase query, since we want exact match, set slop to 0
					phrase.setSlop(0);
					
					Term t = null;
					
					// Create term for the query and add them to the phrase
					final String[] words = myLine.split(" ");
					for (int i = 0; i< words.length; i++) {
						t = new Term("contents", words[i]);
						phrase.add(t);
					}
						        
			        
			        // Search documents where the term phrase occurs
			        topDocs = indexSearcher.search(phrase, 5000);
			        // Score the docs retrieved
			        hits = topDocs.scoreDocs;

			        
			        // Loop over retrieved documents where term occured to add related terms to hashmap
			        for (int i = 0; i < hits.length; i++) 
			        {
			        	int docId = hits[i].doc;
			            Document d = indexSearcher.doc(docId);
			            
			            String name = d.get("filename");
			            Path p = Paths.get(name);
			            String file = p.getFileName().toString();
			            file = file.substring(0, file.length()-4);

			            String content = d.getField("contents").stringValue();
			            
			            if (!myLine.equals(file))
			            	related.put(myLine, file + "///" + content);
			        }

	}

//				//confirm it is creating hashmap correctly
//				System.out.println("Fetching Keys and corresponding [Multiple] Values \n");
//		        // get all the set of keys
//		        Set<String> keys = related.keySet();
//		        // iterate through the key set and display key and values
//		        for (String key : keys) {
//		            System.out.println("Key = " + key);
//		            System.out.println("Values = " + related.get(key) + "\n");
//		        }
				
				System.out.println("----------------------Creating hashmap is successful----------------------");
		        
				// Write hashmap to file
		        writeMap(related);
		        
		        // Read hashmap from file
//		        readMap();
			}   
	
	public static void writeMap(Multimap<String, String> related){
        // Write hashmap to file: 
	    try{
	    File fileOne=new File("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\terms\\hashMapRelated.txt");
	    FileOutputStream fos=new FileOutputStream(fileOne);
	        ObjectOutputStream oos=new ObjectOutputStream(fos);

	        oos.writeObject(related);
	        oos.flush();
	        oos.close();
	        fos.close();
	    }catch(Exception e){}
	    
	    System.out.println("Writing to file completed and successfull");
	    
	}  
	
//	  Read hashmap from file:
//	public static void readMap(){
//
//	    try{
//	        File toRead=new File("/Users/dhosny/Downloads/Lucene files/hashMapRelated.txt");
//	        FileInputStream fis=new FileInputStream(toRead);
//	        ObjectInputStream ois=new ObjectInputStream(fis);
//
//	        Multimap<String, String> mapInFile=(Multimap<String, String>)ois.readObject();
//
//	        ois.close();
//	        fis.close();
//	        //print All data in MAP
//	        Set<String> keys = mapInFile.keySet();
//	        
//	        // iterate through the key set and display key and values
////	        for (String key : keys) {
////	            System.out.println("Key = " + key);
////	            System.out.println("Values = " + mapInFile.get(key) + "\n");
////	        }
//	        
//	        
//	        
//	        for (String key : keys) {
//	            System.out.println("Key = " + key);
//	            System.out.println("value = " + mapInFile.get(key));
//	        
//	            List<String> myValues = (List<String>) mapInFile.get(key);
//	            
//	            for(int i = 0; i < myValues.size() ; i++){
//
//	        		String value = myValues.get(i);
//	        		String[] tokens = value.split("///");
//	        		for (int j = 0; j < tokens.length; j++){
//	        			if (j == 0){
//	        				System.out.println("Related term is: " + tokens[j]);}
//	        			else if (j == 1){
//	        				System.out.println("Definition of related term is: " + tokens[j]);}
//	        		}
//	        	}
//	            
//	        }
//	        System.out.println(mapInFile.size());
//	    }catch(Exception e){}
//	  }
}
