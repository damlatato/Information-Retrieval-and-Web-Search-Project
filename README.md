# Information-Retrieval-and-Web-Search-Project
As the main focus of the project was to develop a search engine, getting the definition for business terms is our main use case from the perspective of the user.
A user will be displayed a search text box, where the user can enter the query of his/ her choice. Just below the search text box, we have implemented a multi-line text area that will display the definition of the term that the user has searched for.
On the right hand side, we will be generating the related terms associated with the query that was provided. Typically it will list n number of related terms, as long as there were related to the search term. This is an enhancement of the actual business dictionary as we are not listing a fixed number of related terms.

-Approaches and Algorithms
  1. Indexing and Searching
  We benefited from Lucene Java library to index terms and documents. During indexing, terms and definitions were stored as “filename” and “content”. For searching method, we parsed query according to term which is found in the indexed corpus and applied searching method in “filename”.
  2. Spell Checking and Suggestions
  To correct misspelled query, we used Spell Checker method from Lucene library. When the user enters a misspelled query, firstly Spell Checker method is initialized and it returns a list of suggested similar words from the index. Our goal was to find most similar terms from the list and present them to the user under the part of “did you mean?”
  Levenshtein Distance:
  Levenshtein Distance is a similarity measure between two strings. It measures the minimum number of edit to convert one string to another. Whenever user enters a misspelled query, our goal was to correct user input by measuring minimum edit distance with other terms and to retrieve exact term which is wanted to find by the user.
  3. Related Terms
  To find related terms, our approach was to return all documents where query term occurred. We applied this method to all terms and created a list consisting of related terms. Our next goal was that to show those related terms to user by sorting them according to their similarity scores. Three types of similarity measures were computed between the definitions of each related term in the list against the definition of the query term:
  
  Cosine Similarity:
  Cosine similarity is used to measure similarity between documents. We used Cosine Similarity to calculate resemblance between document of query term and document of its related term. We represented documents by vectors of TF-IDF weights. After calculation, apart from query definitions, we showed user the top-10 related terms according to their Cosine Similarity scores. However, we considered that
  as long as we are not sure about our results, calculating only one similarity measure and ranking the results were not a sufficient solution so that we decided to calculate other similarity measures also and compare them to see which measure yields the highest similarity score.
  
  Jaccard Coefficient:
  Cosine Similarity uses term frequency in our calculation, whereas Jaccard Coefficient algorithm is based on common terms among documents. Since we have known that difference, we wanted to calculate Jaccard Coefficient between document of query term and document of its related term to observe how sorting of related terms will be changed in comparison with Cosine Similarity. However, we obtained that the top-10 related terms were almost the same with the ones returned based on Cosine.
  
  Dice Similarity:
  The third measure is Dice Coefficient and it is used to compute the similarity between 2 strings. We applied it to get the degree of relatedness between the definition of the searched term and a related term. The Dice coefficient only looks at presence/absence of words in a string and it does not take into consideration any frequency or common terms. Nevertheless, also the results retrieved based on this similarity were almost the same as the other 2 measures with only slight differences.
  
As a result of these three similarity measures, we pursued a technique which showed us which is the best ranking method. Since we have a large index size, it was going to take long time to calculate the average similarity for each searched term; thus, for each similarity measure, we took a sample of 10 query terms. As we searched for them, we summed up all of the scores for each query term then we calculated the average through dividing by number of query terms. Jaccard Coefficient gave us higher score than the other two similarity measures. Therefore, we used Jaccard Coefficient to sort related terms.

  
