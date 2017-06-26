package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PrecisionAndRecallCalculator 
{

	public static HashMap<String, Double> calculatePrecisionRecall(final HashSet<String> hset_goalSet, final Set<String> set)  {

		HashMap<String, Double> hmap_preRcall = new HashMap<>();

		int int_relevantElements, int_retrievaledElements, int_truePositive = 0;

		int_relevantElements = hset_goalSet.size();

		double precision = 0.0;
		double recall = 0.0;
		int_retrievaledElements = set.size();
		
		
		for (String str_cat : set) 
		{
			if (hset_goalSet.contains(str_cat)) {
				int_truePositive += 1;
				System.out.print("\t"+"TP"+str_cat);
			}
			else
			{
				//System.out.print("\t"+"FP"+str_cat);
			}
		}
		
		for (String str_cat : hset_goalSet) 
		{
			if (!set.contains(str_cat)) {
				
				//System.out.print("\t"+"FN "+str_cat);
			}
		}
		System.out.println();
		if (int_truePositive != 0) {
			precision = (double)int_truePositive /(double) int_retrievaledElements;
			recall = (double)int_truePositive / (double)int_relevantElements;

			hmap_preRcall.put("Precision", precision);
			hmap_preRcall.put("Recall", recall);
		} else {
			hmap_preRcall.put("Precision", 0.);
			hmap_preRcall.put("Recall", 0.);
		}

		return hmap_preRcall;
	}
	
	public static double FmeasureCalculate(final double  precision, final double recall) 
	{
		return (double)(2*precision*recall)/(precision+recall);
	}
	
	public static HashMap<String, Double> printCalculatePrecisionRecall(final HashSet<String> hset_goalSet, final Set<String> set)  {

		HashMap<String, Double> hmap_preRcall = new HashMap<>();

		int int_relevantElements, int_retrievaledElements, int_truePositive = 0;

		int_relevantElements = hset_goalSet.size();

		double precision = 0.0;
		double recall = 0.0;
		int_retrievaledElements = set.size();
		
		
		for (String str_cat : set) 
		{
			if (hset_goalSet.contains(str_cat)) {
				int_truePositive += 1;
			}
			else
			{
				System.out.print("\t"+"FP\t"+str_cat);
			}
		}
		System.out.println();
		
		for (String str_cat : hset_goalSet) 
		{
			if (!set.contains(str_cat)) {
				
				System.out.print("\t"+"\tFN\t"+str_cat);
			}
		}
		System.out.println();
		if (int_truePositive != 0) {
			precision = (double)int_truePositive /(double) int_retrievaledElements;
			recall = (double)int_truePositive / (double)int_relevantElements;

			hmap_preRcall.put("Precision", precision);
			hmap_preRcall.put("Recall", recall);
		} else {
			hmap_preRcall.put("Precision", 0.);
			hmap_preRcall.put("Recall", 0.);
		}

		return hmap_preRcall;
	}
}
