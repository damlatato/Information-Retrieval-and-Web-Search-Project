import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
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

public class testAnything {
	public static File indexDir = new File("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\indexDir\\");

	public static Directory directory;
	public static QueryParser parser;
	public static Query query;
	public static TopDocs topDocs;
	public static ScoreDoc[] hits;
//	public static Hashtable<String, ArrayList<String>> relatedTerms = new Hashtable<String, ArrayList<String>>();
//	public static ArrayList<String> related = new ArrayList<String>();
//	public static HashMap<String, Set<String>> related = new HashMap<String,Set<String>>();
	public static Multimap<String, String> related = ArrayListMultimap.create();
	
	public static void main(String[] args) throws IOException, ParseException, NoSuchFieldException, SecurityException {
		// TODO Auto-generated method stub
		
		directory = FSDirectory.open(indexDir);
		IndexSearcher indexSearcher = new IndexSearcher(directory);
		QueryParser parser = new QueryParser(Version.LUCENE_30, "contents", new StandardAnalyzer(Version.LUCENE_30));
		
		
		FileReader input = new FileReader("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\terms\\Vocabulary_terms.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;
		int count = 0;

		while ( (myLine = bufRead.readLine()) != null)
		{    count = count+1;		
			PhraseQuery phrase = new PhraseQuery();
			phrase.setSlop(0);

			Term t = null;
			final String[] words = myLine.split(" ");
			for (int i = 0; i<words.length; i++) {
				System.out.println(words[i]);
//				t.getClass().getField("contents");
				t = new Term("contents", words[i]);
				phrase.add(t);
			}
//			for(int k = 0; k <words.length; k++)
//				System.out.println(words[k]);

//	        query = parser.parse(myLine);
	        System.out.println("this is query term: " + phrase);
	        
	        
	        // Search the top 500 documents where the term occurs
	        topDocs = indexSearcher.search(phrase, 5000);
	        // Score the docs retrieved
	        hits = topDocs.scoreDocs;
	        System.out.println("this is query hits: " + hits.length);

	        
	        // Loop over retrieved documents where term occured to create anchor
	        for (int i = 0; i < hits.length; i++) 
	        {
	        	int docId = hits[i].doc;
	            Document d = indexSearcher.doc(docId);
	            String name = d.get("filename");
//		        System.out.println("this is hits filename: " + name);

	            Path p = Paths.get(name);
	            String file = p.getFileName().toString();
	            file = file.substring(0, file.length()-4);
//	            System.out.println();
	            if (!myLine.equals(file))
	            	related.put(myLine, file);
	        }
	        System.out.println("this is the total number of lines in vocab.txt: " + count);
		}

//		//confirm it is creating hashmap correctly
//		System.out.println("Fetching Keys and corresponding [Multiple] Values \n");
//        // get all the set of keys
//        Set<String> keys = related.keySet();
//        // iterate through the key set and display key and values
//        for (String key : keys) {
//            System.out.println("Key = " + key);
//            System.out.println("Values = " + related.get(key) + "\n");
//        }
		
		System.out.println("----------------------Creating hashmap is successful----------------------");
        
        method1(related);
	}   
	public static void method1(Multimap<String, String> related){
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

        // Read hashmap from file:
	    try{
	        File toRead=new File("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\terms\\hashMapRelated.txt");
	        FileInputStream fis=new FileInputStream(toRead);
	        ObjectInputStream ois=new ObjectInputStream(fis);

	        Multimap<String, String> mapInFile=(Multimap<String, String>)ois.readObject();

	        ois.close();
	        fis.close();
	        
	        //print All data in MAP
	        Set<String> keys = mapInFile.keySet();
	        // iterate through the key set and display key and values
	        for (String key : keys) {
	        		String value = mapInFile.get(key).iterator().next();
	        		String[] tokens = value.split(":");
	        		for (String t : tokens)
	        			  System.out.println("Values = " + t);
	        }

	        System.out.println(mapInFile.size());
	    }catch(Exception e){}
	  }
	    
	}
