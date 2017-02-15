import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class CosineSimilarity
{
	
	public static boolean DESC = false;
	
	static Map<String, Double> unsortMap ;
	
//	method for return cosine score 
	 public static void return_cosine (String word, List<String> relatedTerms, List<double[]> tfidfDocsVector_queryDoc, List<double[]> tfidfDocsVector_relatedTermDoc){
		 unsortMap = new HashMap<String, Double>();  //unsorted cosine similartiy documents will be stored here
//			get the tfidd document vector from query document and related term document
			
		  for (int i = 0; i < tfidfDocsVector_queryDoc.size(); i++) {
			  if(i == 1){
				   
				  break;
			  }
			  
			
	            for (int j = 0; j < tfidfDocsVector_relatedTermDoc.size(); j++) {
//	            	calculate cosine for each value from relatedtermDoc and querydoc
	               
	            
//					System.out.println("between document of " + "/ " +  word + "/" + " and " + relatedTerms.get(j)  +  "  =  "
//                                + CosineSimilarity.cosineSimilarity
//	                                       (
//	                                    		   tfidfDocsVector_queryDoc.get(i), 
//	                                         tfidfDocsVector_relatedTermDoc.get(j)
//	                                       )
//	                                  );
					
					unsortMap.put(relatedTerms.get(j), CosineSimilarity.cosineSimilarity
	                                       (
	                                    		   tfidfDocsVector_queryDoc.get(i), 
	                                         tfidfDocsVector_relatedTermDoc.get(j)
	                                       ));
         }
	            
	            
	           
	        }
			//sorting values according to descending order
		  System.out.println("Sorting of Documents according to Cosine Similarity \n");
			Map<String, Double> sortedMapDesc = sortByComparator(unsortMap, DESC);
			for(int l = 0 ; l < sortedMapDesc.size(); l++) {
				
				System.out.println("Between document of " + "/ " + word + " / "  + " and  document of " +  "/ " + sortedMapDesc.keySet().toArray()[l] + "/ " + " = "  + sortedMapDesc.values().toArray()[l]);
	  }
			
			 System.out.println("\n");}
	/**
	 * Method to calculate cosine similarity between two documents.
	 * @param docVector1 : document vector 1 (a)
	 * @param docVector2 : document vector 2 (b)
	 * @return 
	 */
	public static double cosineSimilarity(double[] docVector1, double[] docVector2) {
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;

		for (int i = 0; i < docVector1.length; i++) //docVector1 and docVector2 must be of same length
		{
			dotProduct += docVector1[i] * docVector2[i];  //a.b
			magnitude1 += Math.pow(docVector1[i], 2);  //(a^2)
			magnitude2 += Math.pow(docVector2[i], 2); //(b^2)
		}

		magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
		magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)

		if (magnitude1 != 0.0 | magnitude2 != 0.0) {
			cosineSimilarity = dotProduct / (magnitude1 * magnitude2);

		} else {
			return 0.0;
		}
		return cosineSimilarity;
	}
	
	private static Map<String, Double> sortByComparator(Map<String, Double> unsortMap, final boolean order) 
	{

		List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

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
}
