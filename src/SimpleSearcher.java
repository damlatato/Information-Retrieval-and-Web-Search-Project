import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.spell.PlainTextDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.wordnet.SynExpand;

import com.google.common.collect.Multimap;


public class SimpleSearcher
{

	/**
	 * @param Declaring Variables used in class
	 * @throws Exception 
	 */
	public static SimpleSearcher searcher = new SimpleSearcher();
	public static Directory directory;
	public static Directory directoryRelated;
//	public static Directory WNdirectory;
	public static File indexDir = new File("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\indexDir\\");
//	public static File WNdirec = new File("/Users/dhosny/Downloads/Lucene files/Prolog/IndexWN/");
	public static File indexDirRelated = new File("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\indexedRelatedTerms\\");
	public static SpellChecker spellChecker;
	public static String line;
	public static int suggestionsNumber;
	public static String[] suggestions;
	public static IndexSearcher indexSearcher;
	public static QueryParser parser;
	public static Query query;
	public static TopDocs topDocs;
	public static ScoreDoc[] hits;
	public static ScoreDoc[] suggestedHits;
	public static int levDist;
	public static String suggestedWord;	
	public static ArrayList<levDistance> items = new ArrayList<levDistance>();
	public static int hitsNum = 5000;
	public static String definition = null;
	public static Hashtable<String, String> relatedTerms = new Hashtable<String,String>();


	
	public static void main(String[] args) throws Exception
	{
		// Read input from user
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		
	    String s = "";
	    
	    while (!s.equalsIgnoreCase("q")) {
	      try {
	        System.out.println("Enter the search query (q=quit):");
	        s = br.readLine();
	        if (s.equalsIgnoreCase("q")) 
	        {
	          break;
	        }
	        // Check if the term entered is spelled correctly
	        spellCheck(s);
//	        returnRelatedTerms(s);
	        
	      }
	      catch (Exception e) {
	          System.out.println("Error searching " + s + " : " + e.getMessage());
	      }
	   }
	}
	
	public static void spellCheck(String line) throws Exception
	{
		//Term is already passed from user input
				
		// We need a Directory, which is a flat list of files. 
		// This class is abstract so we use FSDirectory as the actual implementation. 
		// FSDirectory stores the index files in the file system (for faster access RAMDirectory, which is the memory resident implementation, should be evaluated)
		directory = FSDirectory.open(indexDir);

		// Create a spellChecker instance.
		// Note that the default StringDistance is the Editdistance and the LevensteinDistance.
		spellChecker = new SpellChecker(directory);
		
		// Instantiate a PlainTextDictionary, which points to the terms list text file 
		spellChecker.indexDictionary(new PlainTextDictionary(new File("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\terms\\Vocabulary_terms.txt")));
		
		// Set max number of suggested words to entered term.
		suggestionsNumber = 10;
		
		// Only if term entered is not spelled correctly, look for suggestions, otherwise, search directly the term.
		if(!spellChecker.exist(line))
		{
			// SpellChecker object is used to retrieve the suggested words for a misspelled term
			// Save suggested words in an array
			suggestions = spellChecker.suggestSimilar(line, suggestionsNumber);
			System.out.println("Spell Checker: ");
			
			for (int i = 0; i < suggestions.length; i++)
				System.out.println(suggestions[i]);
			
			System.out.println("-----This is the end of the suggestions list--------" + "\n");
			
			// Compute Levenstein Distance for the suggestions and get words where distance is < 3
			if(suggestions != null)
			{
			    	for(String word : suggestions) {
			    		levDist = levDistance.computeLevenshteinDistance(line, word);
			    		
			    		if (levDist < 3) {	
			    			// Add suggested words to arraylist of suggestions with their corresponding levDistance		    			
			    			items.add(new levDistance(word, levDist));
			    					    			
			    			//Print suggestions
			    			System.out.println("Did you mean: " + word + ". LevDist is: " + levDist);
			    		}
			    	}
	    			System.out.println("-----This is the end of the levDistance computation list--------" + "\n");
			}
			searcher.searchIndexWithSuggestions(indexDir, hitsNum, items);

		}
		else
		{
			// Save entered term in quotations
			line = "\""  + line + "\" ";
			System.out.println(line + "\n");
			// Search indexed files for documents relevant to entered term by user
			searcher.searchIndex(indexDir, line, hitsNum);
		}
		
	}
	// Search the indexed directory for a term from the suggestions list
	private void searchIndexWithSuggestions(File indexDir, int maxHits, ArrayList<levDistance> items) throws Exception 
	{
		indexSearcher = new IndexSearcher(directory);
        parser = new QueryParser(Version.LUCENE_30, "filename", new StandardAnalyzer(Version.LUCENE_30));
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
        
        // Go through hits to search the documents of the suggestion with the least levDistance and return its definition
        int i = 0; 
        String newTerm = "";
        String newFilename = "";
        if (!(items.isEmpty())){
        	switch (items.get(i).getlevDist()) 
			{
		         case 1:
		        	 query = parser.parse(items.get(i).getSuggestion());
		             topDocs = indexSearcher.search(query, maxHits);
		             hits = topDocs.scoreDocs;
	
		             for (int j = 0; j < hits.length; j++) 
		             {
		             	 int docId = hits[j].doc;
		                 Document d = indexSearcher.doc(docId);
		                 String name = d.get("filename");		             
			             String[] tokens = name.split("/");
				            
				            // Get position of filename eg. disinfection.txt
				            int l = tokens.length - 1;
				            newTerm = items.get(i).getSuggestion();
				            newFilename = tokens[l].substring(0, tokens[l].length()-4);
				            
				            // Get content of the document if term searched matches retrieved docs
				            if (newTerm.equals(newFilename))
				            {
					             // Print definition of term from documents where term is matched
				            	 definition = d.getField("contents").stringValue();
				            	 break;
				            }
		             }
	            	 System.out.println("From " + hits.length + " documents, below is the definition of the most relevant one: ");
		             computeSimilarityRelated.relatedTermsSimilarity(newTerm, definition);
	            	 System.out.println("Definition of " + "\"" + newTerm + "\"" + " is: " + definition);

		        	 break;
	        	 
		         case 2:
		        	 query = parser.parse(items.get(i).getSuggestion());
		             topDocs = indexSearcher.search(query, maxHits);
		             hits = topDocs.scoreDocs;

		             for (int j = 0; j < hits.length; j++) 
		             {
		             	 int docId = hits[j].doc;
		                 Document d = indexSearcher.doc(docId);
		                 String name = d.get("filename");		             
			             String[] tokens = name.split("/");
				            
				            // Get position of filename eg. disinfection.txt
				            int l = tokens.length - 1;
				            newTerm = items.get(i).getSuggestion();
				            newFilename = tokens[l].substring(0, tokens[l].length()-4);
				            
				            // Get content of the document if term searched matches retrieved docs
				            if (newTerm.equals(newFilename))
				            {
					             // Print definition of term from documents where term is matched
				            	 definition = d.getField("contents").stringValue();
				            	 break;
				            }
		             }
	            	 System.out.println("From " + hits.length + " documents, below is the definition of the most relevant one: ");
	            	 System.out.println("\n");
		             computeSimilarityRelated.relatedTermsSimilarity(newTerm, definition);
	            	 System.out.println("Definition of " + "\"" + newTerm + "\"" + " is: " + definition);

		        	 break;
			}
        	i++;
 
        }	
	}
	
	
	// Search the indexed directory for the term entered by user
	private void searchIndex(File indexDir, String queryStr, int maxHits) throws Exception 
	{
		indexSearcher = new IndexSearcher(directory);
        parser = new QueryParser(Version.LUCENE_30, "filename", new StandardAnalyzer(Version.LUCENE_30));
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
        query = parser.parse(queryStr);
        
        // Search the top 500 documents where the term occurs
        topDocs = indexSearcher.search(query, maxHits);
        // Score the docs retrieved
        hits = topDocs.scoreDocs;
        
        // Loop over retrieved documents where term occured to create anchor
        for (int i = 0; i < hits.length; i++) 
        {
        	int docId = hits[i].doc;
            Document d = indexSearcher.doc(docId);
            String name = d.get("filename");
           
            // Print docs name where term was found
            System.out.println(d.get("filename"));
        }   
        
        // Print number of documents where term was found
        System.out.println("Found " + hits.length);
        
        // Print definition of terms in documents where term is matched

        System.out.println(returnDefinition.returnTermDefinition(hits, queryStr, items));
        System.out.println("\n");
        evaluate(queryStr);
    }
	

	private void evaluate(String word)
	{
		ArrayList<String> suggestedRelated = new ArrayList<String>();
		ArrayList<String> actualRelated = new ArrayList<String>();
		EvaluationMetrics evaluateQuery = new EvaluationMetrics();
		System.out.println("I am evaluating at the moment!!-----------------");

		try{
			//Get suggested list from hashmap
			File toRead=new File("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\terms\\hashMapRelated.txt");
			FileInputStream fis=new FileInputStream(toRead);
			ObjectInputStream ois=new ObjectInputStream(fis);

			Multimap<String, String> mapInFile=(Multimap<String, String>)ois.readObject();

			ois.close();
			fis.close();

			//print All data in MAP
			//loops over the key (which are all the related terms in the hashmap)
			Set<String> keys = mapInFile.keySet();
			word = word.substring(1, word.length()-2);

			for (String key : keys) {
				List<String> myValues = (List<String>) mapInFile.get(key);
				if(key.equals(word)){
					for(int i = 0; i < myValues.size() ; i++)
					{
						String value = myValues.get(i);
						String[] tokens = value.split("///");
						for (int j = 0; j == 0; j++){
							suggestedRelated.add(tokens[j]);
						}
					}
				}
			}

			for(int h=0; h<suggestedRelated.size();h++){
				System.out.println(suggestedRelated.get(h));
				System.out.println(suggestedRelated.size());
			}
			System.out.println("\n");
			
			//Get actual list from indexed related terms
			directoryRelated = FSDirectory.open(indexDirRelated);
			indexSearcher = new IndexSearcher(directoryRelated);
			PhraseQuery phrase = new PhraseQuery();
			phrase.setSlop(0);
			Term t = null;
			
			// Create term for the query and add them to the phrase
			final String[] words = word.split(" ");
			for (int i = 0; i< words.length; i++) {
				t = new Term("contents", words[i]);
				phrase.add(t);
			}

	        // Search documents where the term phrase occurs
	        TopDocs topDocs1 = indexSearcher.search(phrase, 500000);
	        // Score the docs retrieved
	        ScoreDoc[] hits1 = topDocs1.scoreDocs;

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
	            final String[] actualTerms = content.split(",");
				for (int l = 0; l< actualTerms.length; l++) {
					if (!actualTerms[l].equals(""))	
						actualRelated.add(actualTerms[l]);
				}
	        }
	        for(int h=0; h<actualRelated.size();h++){
				System.out.println(actualRelated.get(h));
				System.out.println(actualRelated.size());
			}
	        
	        
	        evaluateQuery.calculateMeasures(actualRelated, suggestedRelated);
		}
		catch(Exception e)
		{

		}
	}

}
