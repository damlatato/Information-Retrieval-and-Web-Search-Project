package WordNetExpansion;
import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.StringTokenizer;

//import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.document.Document;

public class FileTokenzier {

	public static ArrayList<String> oneGram = new ArrayList<String>();
	public static ArrayList<String> biGram = new ArrayList<String>();
	public static ArrayList<String> triGram = new ArrayList<String>();
	public static ArrayList<String> fourGram = new ArrayList<String>();
	public static ArrayList<String> fiveGram = new ArrayList<String>();
	public static ArrayList<String> sixGram = new ArrayList<String>();
	public static ArrayList<String> sevenGram = new ArrayList<String>();
	public static ArrayList<String> eightGram = new ArrayList<String>();
	public static ArrayList<String> nineGram = new ArrayList<String>();
	public static ArrayList<String> tenGram = new ArrayList<String>();
	public static ArrayList<String> elevenGram = new ArrayList<String>();
	public static ArrayList<String> twelveGram = new ArrayList<String>();
	
	
	public static ArrayList<Integer> countNums = new ArrayList<Integer>();



	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub


		tokenize();
	}

	
	
	public static void tokenize() throws IOException
	{

		FileReader input = new FileReader("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\terms\\Vocabulary_terms.txt");
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;

		while ( (myLine = bufRead.readLine()) != null)
		{    
			StringTokenizer st = new StringTokenizer(myLine);
			Integer count = st.countTokens();
			countNums.add(count);
//			if (count == 5)
//				System.out.println(myLine);
			switch (count) 
			{
				case 1:
					oneGram.add(myLine);
					break;
				case 2:
					biGram.add(myLine);
					break;
				case 3:
					triGram.add(myLine);
					break;
				case 4:
					fourGram.add(myLine);
					break;
				case 5:
					fiveGram.add(myLine);
					break;
				case 6:
					sixGram.add(myLine);
					break;
				case 7:
					sevenGram.add(myLine);
					break;
				case 8:
					eightGram.add(myLine);
					break;
				case 9:
					nineGram.add(myLine);
					break;
				case 10:
					tenGram.add(myLine);
					break;
				case 11:
					elevenGram.add(myLine);
					break;
				case 12:
					twelveGram.add(myLine);
					break;
			}

		}
		
//		for (int i = 0; i < fiveGram.size(); i++)
//			System.out.println("five gram array: " + fiveGram.get(i));
		
		
		// Write oneGram to a file
		FileWriter writer1 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\1Gram.txt"); 
		for(String str: oneGram)
			writer1.write(str + "\n");
		writer1.close();
		//---------------------------------------------
		// Write biGram to a file
		FileWriter writer2 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\2Gram.txt"); 
		for(String str: biGram)
			writer2.write(str + "\n");
		writer2.close();
		//---------------------------------------------
		// Write triGram to a file
		FileWriter writer3 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\3Gram.txt"); 
		for(String str: triGram)
			writer3.write(str + "\n");
		writer3.close();
		//---------------------------------------------
		// Write fourGram to a file
		FileWriter writer4 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\4Gram.txt"); 
		for(String str: fourGram)
			writer4.write(str + "\n");
		writer4.close();
		//---------------------------------------------
		// Write fiveGram to a file
		FileWriter writer5 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\5Gram.txt"); 
		for(String str: fiveGram)
			writer5.write(str + "\n");
		writer5.close();
		//---------------------------------------------
		// Write sixGram to a file
		FileWriter writer6 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\6Gram.txt"); 
		for(String str: sixGram)
			writer6.write(str + "\n");
		writer6.close();
		//---------------------------------------------
		// Write sevenGram to a file
		FileWriter writer7 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\7Gram.txt"); 
		for(String str: sevenGram)
			writer7.write(str + "\n");
		writer7.close();
		//---------------------------------------------
		// Write eightGram to a file
		FileWriter writer8 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\8Gram.txt"); 
		for(String str: eightGram)
			writer8.write(str + "\n");
		writer8.close();
		//---------------------------------------------
		// Write nineGram to a file
		FileWriter writer9 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\9Gram.txt"); 
		for(String str: nineGram)
			writer9.write(str + "\n");
		writer9.close();
		//---------------------------------------------
		// Write tenGram to a file
		FileWriter writer10 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\10Gram.txt"); 
		for(String str: tenGram)
			writer10.write(str + "\n");
		writer10.close();
		//---------------------------------------------
		// Write elevenGram to a file
		FileWriter writer11 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\11Gram.txt"); 
		for(String str: elevenGram)
			writer11.write(str + "\n");
		writer11.close();
		//---------------------------------------------
		// Write twelveGram to a file
		FileWriter writer12 = new FileWriter("C:\\Users\\Tato\\Desktop\\FSS 2016\\Information Retrieval and Web Search\\Lucene files\\TokenGrams\\12Gram.txt"); 
		for(String str: twelveGram)
		  writer12.write(str + "\n");
		writer12.close();
		 
		
		System.out.println("----Writing grams to file successful----");
//		Collections.sort(countNums); // Sort the arraylist
//		System.out.println("max is: " + countNums.get(countNums.size() - 1)); // Get max value of count inside array to know max grams.


	}
	


}
