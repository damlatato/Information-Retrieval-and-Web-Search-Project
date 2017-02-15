import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;

import com.google.common.collect.Multimap;

public class computeSimilarityRelated {
	//This variable will hold all terms of each document in an array.
	private static List<String[]> termsDocsArray_queryDoc;
	private static List<String[]> termsDocsArray_relatedTermDoc;
	private static List<String> allTerms; //to hold all terms
	private static List<String> allTerms1; //to hold all terms
	private static List<double[]> tfidfDocsVector_relatedTermDoc;
	private static List<double[]> tfidfDocsVector_queryDoc;
	public static Map<String, Double> unsortMap = new HashMap<String, Double>();
	
	public static HashMap<String, Double> DiceSimilarityScore = new HashMap<String, Double>();
	//	For descending order in Map Sorting
	public static boolean DESC = false;
	public static String[] tokenizedTerms_for_word;
	public static String[] tokenizedTerms_for_relatedTerm;
	public static DiceCoefficient dice = new DiceCoefficient();


	public static String[] tokenize(String definition)
	{
//		Set for query document and related term document to calculate jaccard 
		Set<String> setA = new HashSet<String>();

		//	Tokenize terms in definition of query term
		String[]tokenizedTerms_for_word1 = definition.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
		for (String term : tokenizedTerms_for_word1) {
			if (!allTerms1.contains(term)) {  //avoid duplicate entry
				allTerms1.add(term);
				//			add terms in related term document in set A to calculate Jaccard
				setA.add(term);
			}
		}
		return tokenizedTerms_for_word1;
	}
	static void relatedTermsSimilarity(String word, String definition) throws CorruptIndexException, IOException, ParseException
	{
		Map<String, Double> unsortMap_Jaccard = new HashMap<String, Double>();
		// related terms and Jaccard sim
		
		// Sets for query document and related term document to calculate jaccard 
		Set<String> setA = new HashSet<String>();

		Set<String> setB = new HashSet<String>();
		//keep related terms in list
		List<String> relatedTerms_Jaccard= new ArrayList<String>();
		
		//for cosine sim. 
		termsDocsArray_queryDoc = new ArrayList<String[]>();
		termsDocsArray_relatedTermDoc = new ArrayList<String[]>();

		allTerms = new ArrayList<String>(); //to hold all terms
		allTerms1 = new ArrayList<String>(); //to hold all terms
		tfidfDocsVector_relatedTermDoc = new ArrayList<double[]>();
		tfidfDocsVector_queryDoc = new ArrayList<double[]>();

		List<String> relatedTerms = new ArrayList<String>();
		String[] tokenizedTerms_for_word = definition.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
		for (String term : tokenizedTerms_for_word) {
			if (!allTerms1.contains(term)) {  //avoid duplicate entry
				allTerms1.add(term);
				setA.add(term);
			}
		}		
		termsDocsArray_queryDoc.add(tokenizedTerms_for_word);
		
		//Print the definition of the entered user query
		System.out.println("Query term definition: " + definition + "\n");

		// Read hashmap from file to get the related terms based on the the user query term(s)
		try{
			File toRead=new File("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\terms\\hashMapRelated.txt");
			FileInputStream fis=new FileInputStream(toRead);
			ObjectInputStream ois=new ObjectInputStream(fis);

			Multimap<String, String> mapInFile=(Multimap<String, String>)ois.readObject();

			ois.close();
			fis.close();

			//print All data in MAP
			//loops over the key (which are all the related terms in the hashmap)
			Set<String> keys = mapInFile.keySet();


			for (String key : keys) {	
				List<String> myValues = (List<String>) mapInFile.get(key);
				// for each key, make sure that it equals the user query so as to retrieve the corresponding related terms
				if (word.equals(key))
				{
					// loop over the corresponding related terms to get each term and  its definition from the hashmap
					for(int i = 0; i < myValues.size() ; i++)
					{
						// Get related term at index
						String value = myValues.get(i);
						// Split term and its definition at ///
						String[] tokens = value.split("///");
						// Get term and definition + Compute similarity
						for (int j = 0; j < tokens.length; j++)
						{
							// Index 0 is ALWAYS the related term name
							if (j == 0)
							{
								System.out.println("Related term is: " + tokens[j]);
								relatedTerms.add(tokens[j]);
								relatedTerms_Jaccard.add(tokens[j]);
							}
							// Index 1 is ALWAYS the related term definition
							else if (j == 1)
							{
								System.out.println("Definition of related term is: " + tokens[j]);
								System.out.println("\n");

								// Tokenize terms in definition of related words
								String[] tokenizedTerms = tokens[j].toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
								for (String term : tokenizedTerms) {
									if (!allTerms.contains(term)) {  //avoid duplicate entry
										allTerms.add(term);
										setB.add(term);
									}
								}

								termsDocsArray_relatedTermDoc.add(tokenizedTerms);

								// Calculate Jaccard Similarity
								double errorFactor = 0.001;				
								LSHMinHash<String> minHash = new LSHMinHash<String>(errorFactor, setA, setB);
								unsortMap_Jaccard.put(relatedTerms_Jaccard.get(i), minHash.findSimilarities());
								

								// Calculate Dice Similarity
								double diceSim = dice.score(definition, tokens[j]);
								DiceSimilarityScore.put(tokens[j-1], diceSim);
							}
						}
					}
				}
			}
		
			DESC = false;
			Map<String, Double> sortedMapDesc_Dice = sortByComparator(DiceSimilarityScore, DESC);
			Set<String> sortedKeys = sortedMapDesc_Dice.keySet();

			System.out.println("Related documents ranked based on Dice Similarity: \n");
			for (String key : sortedKeys){
				System.out.println("Ranked Related Term: " + key + " = " + DiceSimilarityScore.get(key));
			}
			
			System.out.println("\n");
		}
		catch(Exception e)
		{
			
		}
		

		// create sorted map for jaccard values
		Map<String, Double> sortedMapDesc_Jaccard = sortByComparator(unsortMap_Jaccard, DESC);
		System.out.println("Related documents ranked based on Jaccard Coefficient: \n");
		//	Calculate Jaccard Coefficient
		for(int l = 0 ; l < sortedMapDesc_Jaccard.size(); l++) {

			System.out.println("Between document of " + "/ " + word + " / "  + " and  document of " +  "/ " + sortedMapDesc_Jaccard.keySet().toArray()[l] + "/ " + " = "  + sortedMapDesc_Jaccard.values().toArray()[l]);
			
		}

		System.out.println("\n");
		//	alculate tfidf		
		calcualte_tfidf(termsDocsArray_relatedTermDoc,termsDocsArray_queryDoc);
		
		System.out.println("Related documents ranked based on Cosine Similarity: \n");
		//	Calculate cosine similarity
		CosineSimilarity.return_cosine(word, relatedTerms, tfidfDocsVector_queryDoc, tfidfDocsVector_relatedTermDoc) ;
		System.out.println("\n");



	}

	private static Map<String, Double> sortByComparator(Map<String, Double> unsortMap_Jaccard, final boolean order) 
	{

		List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap_Jaccard.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Double>>()
		{
			public int compare(Entry<String, Double> o1,
					Entry<String, Double> o2)
			{
				if (order)
				{
					return o1.getValue().compareTo(o2.getValue());
				}
				else
				{
					return o2.getValue().compareTo(o1.getValue());

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : list)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}


		return sortedMap;
	}
	
	public static void calcualte_tfidf (List<String[]> termsDocsArray_relatedTermDoc, List<String[]> termsDocsArray_queryDoc)  
	{
		double tf_relatedTermDoc; //term frequency
		double idf_relatedTermDoc; //inverse document frequency
		double tfidf_relatedTermDoc; //term requency inverse document frequency

		double tf_queryDoc; //term frequency for query doc
		double idf_queryDoc; //inverse document frequency
		double tfidf_queryDoc; //term requency inverse document frequency


		//	--------------------------calculate tf-idf for each related term document ---------------------------------------------------------
		for (String[] docTermsArray : termsDocsArray_relatedTermDoc) {
			double[] tfidfvectors = new double[allTerms.size()];
			int count = 0;
			for (String terms : allTerms) {
				tf_relatedTermDoc = new TfIdf().tfCalculator(docTermsArray, terms);
				idf_relatedTermDoc = new TfIdf().idfCalculator(termsDocsArray_relatedTermDoc, terms);
				tfidf_relatedTermDoc = tf_relatedTermDoc* idf_relatedTermDoc;


				tfidfvectors[count] = tfidf_relatedTermDoc;

				count++;

			} //end of second for
			tfidfDocsVector_relatedTermDoc.add(tfidfvectors);  //storing document vectors;    

		} //end of second for 

		//	----------------------calculate tf-idf for query document---------------------------------------------------------------
		for (String[] docTermsArray1 : termsDocsArray_queryDoc) {
			double[] tfidfvectors1 = new double[allTerms1.size()];
			int count1 = 0;
			for (String terms1 : allTerms1) {
				tf_queryDoc = new TfIdf().tfCalculator(docTermsArray1, terms1);

				idf_queryDoc = new TfIdf().idfCalculator(termsDocsArray_queryDoc, terms1);

				tfidf_queryDoc = tf_queryDoc * idf_queryDoc;




				tfidfvectors1[count1] = tfidf_queryDoc;
				//					System.out.println("tfidf for related term doc for "  + terms1 + tfidfvectors1[count1]);
				count1++;

			}// end of second for 
			tfidfDocsVector_queryDoc.add(tfidfvectors1);  //storing document vectors;    

		} //end of first for

	}


}
