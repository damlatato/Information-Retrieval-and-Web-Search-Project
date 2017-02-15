import java.util.ArrayList;

public class EvaluationMetrics {
	
	public static ArrayList<String> actualRelated = new ArrayList<String>(); // List of related terms for a query from Business Dictionary
	public static ArrayList<String> suggestedRelated = new ArrayList<String>(); // List of related terms for a query based on our index retrieval
	
	public static double Precision;
	public static double Recall;
	public static double Accuracy;
	public static double FMeasure;

	// Calculate the measure used for P. R. A. and FM.
	public void calculateMeasures(ArrayList<String> actualRelated, ArrayList<String> suggestedRelated)
	{	
		double tp = 0; // True Positive
		double tn = 0; // True Negative
		double fp = 0; // False Positive
		double fn = 0; // False Negative
		boolean flagActual = false;
		boolean flagSuggested = false;
		for (int i = 0; i < actualRelated.size(); i++)
		{
			for (int k = 0; k < suggestedRelated.size(); k++)
			{
				if (actualRelated.get(i).equals(suggestedRelated.get(k)))
				{
					// Increase TP
					// Term is in the actual and the suggested terms list
					tp++;
					flagActual = true;
					System.out.println("im in at: " + k);

				}
			}
			if (flagActual == false){
				// Increase FN
				// Terms is in the actual but not the suggested terms list 
				fn++;
				System.out.println("im in when j = " + i);

			}
			flagActual = false;
		}
		for (int j = 0; j < suggestedRelated.size(); j++)
		{
			for (int w = 0; w < actualRelated.size(); w++)
			{
				if (suggestedRelated.get(j).equals(actualRelated.get(w)))
				{
					flagSuggested = true;
				}
			}
			// Term is in the suggested but not in the actual terms list
			if (flagSuggested == false){
				// Increase FP
				fp++;
			}
			flagSuggested = false;
		}
		System.out.println("fp: " + fp);
		System.out.println("tp: " + tp);
		System.out.println("fn: " + fn);


		System.out.println("Precision = " + calculatePrecision(tp, fp));
		System.out.println("Recall = " + calculateRecall(tp, fn));
		System.out.println("Accuracy = " + calculateAccuracy(tp, fp, tn, fn));
		System.out.println("F-Measure = " + calculateFMeasure(calculatePrecision(tp, fp), calculateRecall(tp, fn)));

	}
	
	// Calculate Precision
	public double calculatePrecision(double tp, double fp)
	{
		// Formula to calculate Precision
		Precision = tp / (tp + fp);
		return Precision;
	}
	
	// Calculate Recall
	public double calculateRecall(double tp, double fn)
	{
		// Formula to calculate Recall
		Recall = tp / (tp + fn);
		return Recall;		
	}
	
	// Calculate Accuracy
	public double calculateAccuracy(double tp, double fp, double tn, double fn)
	{
		// Formula to calculate Accuracy
		Accuracy = (tp + tn) / (tp + fp + tn + fn);
		return Accuracy;
	}
	
	// Calculate FMeasure
	public double calculateFMeasure(double precision, double recall)
	{
		// Formula to calculate FMeasure
		FMeasure = 2 * ((precision * recall) / (precision + recall));
		return FMeasure;
	}

}
