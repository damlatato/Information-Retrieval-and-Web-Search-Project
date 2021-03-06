import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Evaluation {

	public static void main(String args[]) throws IOException {
		
		//Two-dimensional array, modify the line of the path
		//PS：To write the file to append, note the number of runs
		String[][] src = {{"/home/xiaowei/workspace/EvalutionMetrics/src/results.txt","/home/xiaowei/workspace/EvalutionMetrics/src/relevant.txt","/home/xiaowei/workspace/EvalutionMetrics/src/output.txt"}}; 
		process(src[0]);
	}
	
	//封装所有过程，输入为3元数组，args[0]为result，args[1]为relevant,args[2]为输出文件
	//All packaging process, the input array is $ 3, args [0] for the result, args [1] is relevant, args [2] for the output file
	public static void process(String args[]) throws IOException{
		
		if (args.length != 3)  {
			System.out.println("Incorrect Number of Parameters");
			return;
		}
		
		HashMap<Short, short[]> relevantsMap = parseDocs(args[1]);
		HashMap<Short, short[]> resultsMap = parseDocs(args[0]);
		
		short queryCounter = 1;
		
		ArrayList<Float> apValues = new ArrayList<Float>();
		
		Iterator<Entry<Short, short[]>> itRelevant = relevantsMap.entrySet().iterator();


	    while (itRelevant.hasNext()) {
		        Map.Entry<Short,short[]> pairs = itRelevant.next();   
		        
		        apValues.add(calcRecallAndPrecision(pairs.getValue(), resultsMap.get(pairs.getKey()),queryCounter, args[2]));
		        queryCounter++;
	    }
	    
	    // counter - 1 because it is going to have an additional 1 value from the last iteration
	    queryCounter -= 1;
	    
	    // calculating and displaying the MAP values
	    calcMap(apValues,queryCounter,args[2]);
	}
	
	// straightforward parser that can be used for both relevants and results since they have the same structure.
	public static HashMap<Short, short[]> parseDocs(String fileName) throws IOException {
		
		String temp[];
		short[] tempS;
		
		HashMap<Short, short[]> tempMap = new HashMap<Short, short[]>();
		
		BufferedReader br = new BufferedReader(new FileReader(fileName),15000);
		
		String line = br.readLine();
		
		while (line != null) {
			
			temp = line.substring(line.indexOf(" ") + 1).split(",");
			tempS = new short[temp.length];
			
			for(int i = 0;i < temp.length;i++)			
			{
				tempS[i] = Short.parseShort(temp[i]);
			}
			
			tempMap.put(
					Short.valueOf(line.substring(0, line.indexOf(" "))),
					tempS);
			
			line = br.readLine();
			
		}
		
		br.close();
		
		return tempMap;
	}
	
	public static float calcRecallAndPrecision(short[] relevantsList, short[] resultsList, short queryNumber, String output) throws IOException {
		

		boolean[] relevants = new boolean[resultsList.length];
		
		short numberOfRelevants = (short) relevantsList.length;
 
		short realNumberOfRelevants = 0;
		
		float[] recall;
		float[] precision;
		float totalRecal = 0;
		float totalPrecision = 0;
		short counter = 0;
		short relevantsAtPoint = 0;

		for(short i = 0; i < resultsList.length; i++) {
			
			for (short j = 0; j < relevantsList.length; j++) {
				// if the relevant document is in the list, I give the value TRUE at the same position as the ResultList
				if (resultsList[i] == relevantsList[j]) {
					relevants[i] = true;
					// real relevant found. (not every relevant document is in the list)
					realNumberOfRelevants++;
				}
				
			}
			
		}
		
		recall = new float[realNumberOfRelevants];
		precision = new float[realNumberOfRelevants];

		for (short i = 0; i < relevants.length; i++) {
			
			if (relevants[i]) {

				relevantsAtPoint++;
				
				recall[counter] = (float) relevantsAtPoint / numberOfRelevants;
				precision[counter] = (float) relevantsAtPoint / (i+1);
				
				counter++;
			}
		}
		
		//add text to file
		FileWriter fw = new FileWriter(output,true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		// printing the results
		System.out.println("----------Results for Query " + (queryNumber) + "----------");
		bw.write("----------Results for Query " + (queryNumber) + "----------\r\n");
		
		for (short i = 0; i < recall.length; i++) {
			
			// adding the values to print
			totalRecal += recall[i];
			totalPrecision += precision[i];
			
			System.out.println("Relevant : " + (i + 1));
			System.out.println("Recall   : " + recall[i]);
			System.out.println("Precision: " + precision[i]);
			System.out.println();
			
			bw.write("Relevant : " + (i + 1) + "\r\n");
			bw.write("Recall   : " + recall[i] + "\r\n");
			bw.write("Precision: " + precision[i] + "\r\n");
			bw.write("\r\n");
		}
		
		// calculating before since this value is going to be returned
		float ap = totalPrecision / numberOfRelevants;
		
		System.out.println("---------------------");
		bw.write("---------------------\r\n");
		
		// recall can be calculated on the go since is not going to be needed anymore
		System.out.println("Average Recall   : " + (totalRecal / numberOfRelevants));
		System.out.println("Average Precision: " + ap);
		System.out.println();
		
		bw.write("Average Recall   : " + (totalRecal / numberOfRelevants) + "\r\n");
		bw.write("Average Precision: " + ap + "\r\n");
		bw.write("\r\n");
		
		bw.close();
		fw.close();		
		
		// returning just the ap since it is needed to further calculate the MAP
		return ap;
	}

	public static void calcMap(ArrayList<Float> apValues, short numberOfQueries, String output) throws IOException {
		
		float totalAp = 0;
		
		// Adding all the values for further calculation
		for (int i = 0; i < apValues.size(); i++) {
			totalAp += apValues.get(i);
		}
		
		//add text to file
		FileWriter fw = new FileWriter(output,true);
		BufferedWriter bw = new BufferedWriter(fw);
		
		// printing the final results
		System.out.println("----------MAP RESULT----------");
		System.out.println("Total AP     : " + totalAp);
		System.out.println("Total Queries: " + numberOfQueries);
		System.out.println("MAP: " + (totalAp / numberOfQueries) );
		
		bw.write("----------MAP RESULT----------" + "\r\n");
		bw.write("Total AP     : " + totalAp + "\r\n");
		bw.write("Total Queries: " + numberOfQueries + "\r\n");
		bw.write("MAP: " + (totalAp / numberOfQueries) + "\r\n");
		
		bw.close();
		fw.close();
	}
}
