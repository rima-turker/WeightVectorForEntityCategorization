package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import TreeGeneration.FindQuartile;
import TreeGeneration.Global;

public class Normalization 
{
	
	public static Map<String, HashMap<String, Double>>  normalize_LevelBasedMeanAndDeviation(Map<String, HashMap<String, Double>> hmap_addCatValuesTillDepth) 
	{
		Map<String, HashMap<String, Double>> hmap_heuResultNormalized = new LinkedHashMap<>();
		for (Integer i = 1; i <= Global.levelOfTheTree; i++) 
		{
			HashSet<Double> hset_levelallValues= new HashSet<>();
			for (Entry<String, HashMap<String, Double>> entry :hmap_addCatValuesTillDepth.entrySet()) 
			{
				if (entry.getKey().contains(Global.str_depthSeparator+i.toString())) 
				{
					HashMap<String, Double> hmap_entAndCat = new HashMap<>(entry.getValue());
					
					hset_levelallValues.addAll(hmap_entAndCat.values());
					
				}
			}
			Double[] arr = new Double[hset_levelallValues.size()];
			arr = hset_levelallValues.toArray(arr);
			Statistics stat = new Statistics(arr);
			Double mean = stat.getMean();
			Double std = stat.getStdDev();
			Double var= stat.getVariance();
			if (i==6) 
			{
				System.out.println("Max Value: "+Collections.max(hset_levelallValues));
				System.out.println("Min Value: "+Collections.min(hset_levelallValues));
				
				System.out.println("Mean Value "+mean);
				System.out.println("Standard Daviation  "+std);
				System.out.println("Variance "+var);
			}
			if (!hset_levelallValues.isEmpty()) 
			{
				
				Double max = Collections.max(hset_levelallValues);
				//System.out.println(i.toString()+":"+max);
				for (Entry<String, HashMap<String, Double>> entry :hmap_addCatValuesTillDepth.entrySet()) 
				{
					if (entry.getKey().contains(Global.str_depthSeparator+i.toString())) 
					{
						HashMap<String, Double> hmap_CatAndVal = new HashMap<>(entry.getValue());
						
						HashMap<String, Double> hmap_resultCatAndVal= new LinkedHashMap<>();
						
						for (Entry<String, Double> entry_CatAndVal :hmap_CatAndVal.entrySet()) 
						{
							hmap_resultCatAndVal.put(entry_CatAndVal.getKey(), (double)(entry_CatAndVal.getValue()/max));
						}
						
						hmap_heuResultNormalized.put(entry.getKey(), hmap_resultCatAndVal);
					}
				}
			}
			else
			{
				int count=0;
				for (Entry<String, HashMap<String, Double>> entry :hmap_addCatValuesTillDepth.entrySet()) 
				{
					if (entry.getKey().contains(Global.str_depthSeparator+i.toString())) 
					{
						hmap_heuResultNormalized.put(entry.getKey(), entry.getValue());
						count++;
					}
				}
			}
		}
		return hmap_heuResultNormalized;
	}
	/*
	 * This function normalize WV based on its level after detecting outliers
	 */
	public static Map<String, HashMap<String, Double>>  normalize_LevelBasedDetectingOutliers(Map<String, HashMap<String, Double>> mapAggregatedTillDepth,int depth) 
	{
		
		Map<String, HashMap<String, Double>> mapResultNormalized = new LinkedHashMap<>();
		ArrayList<Double> arrList = new ArrayList<>();
			//HashSet<Double> hset_levelallValues= new HashSet<>();
		for (Entry<String, HashMap<String, Double>> entry :mapAggregatedTillDepth.entrySet()) 
		{
			HashMap<String, Double> hmap_entAndCat = new HashMap<>(entry.getValue());
			for (Double db :hmap_entAndCat.values()) 
			{
				arrList.add(db);
			}
		}
		Collections.sort(arrList);
		 
		double[] target = new double[arrList.size()];
		 for (int i = 0; i < target.length; i++) {
		    target[i] = arrList.get(i);                // java 1.5+ style (outboxing)
		 }
		 
		 double threshold = FindQuartile.findOutliers(target);
		 
		  System.out.println("Thereshould value is "+threshold);
			long positive = 0;
			long negative = 0;
			for (int i = 0; i < target.length; i++) {
			    if(target[i]<=threshold) {
			    	positive++;
			    }else {
			    	negative++;
			    }
			 }
			
			double keepPercentage = (100.*positive/target.length);
			
			System.out.println("keep percenatge "+keepPercentage);
			System.out.println("remove percentage "+(100-keepPercentage));

			int countValuesAboveT = 0;
			int countValuesBlowT = 0;
			

			for (Entry<String, HashMap<String, Double>> entry :mapAggregatedTillDepth.entrySet()) 
			{
				if (entry.getKey().contains(Global.str_depthSeparator+depth)) 
				{
					HashMap<String, Double> hmap_CatAndVal = new HashMap<>(entry.getValue());
					
					HashMap<String, Double> hmap_resultCatAndVal= new LinkedHashMap<>();
					
					for (Entry<String, Double> entry_CatAndVal :hmap_CatAndVal.entrySet()) 
					{
						if (entry_CatAndVal.getValue()>threshold) 
						{
							hmap_resultCatAndVal.put(entry_CatAndVal.getKey(), 1.0);
							countValuesAboveT++;
						}
						else
						{
							hmap_resultCatAndVal.put(entry_CatAndVal.getKey(), (double)(entry_CatAndVal.getValue()*1.0)/threshold);
							countValuesBlowT++;
						}
						
						
					}
					
					mapResultNormalized.put(entry.getKey(), hmap_resultCatAndVal);
				}
			}
			System.out.println("countValuesAboveT  "+countValuesAboveT);
			System.out.println("countValuesBlowT "+countValuesBlowT);
		return mapResultNormalized;
	}
	public static Map<String, HashMap<String, Double>>  normalize_LevelBased(Map<String, HashMap<String, Double>> hmap_addCatValuesTillDepth) 
	{
		Map<String, HashMap<String, Double>> hmap_heuResultNormalized = new LinkedHashMap<>();
		for (Integer i = 1; i <= Global.levelOfTheTree; i++) 
		{
			HashSet<Double> hset_levelallValues= new HashSet<>();
			for (Entry<String, HashMap<String, Double>> entry :hmap_addCatValuesTillDepth.entrySet()) 
			{
				if (entry.getKey().contains(Global.str_depthSeparator+i.toString())) 
				{
					HashMap<String, Double> hmap_entAndCat = new HashMap<>(entry.getValue());
					
					hset_levelallValues.addAll(hmap_entAndCat.values());
					
				}
			}
			
			if (i==6) 
			{
				System.out.println("Max Value: "+Collections.max(hset_levelallValues));
				System.out.println("Min Value: "+Collections.min(hset_levelallValues));
				Double[] arr = new Double[hset_levelallValues.size()];
				arr = hset_levelallValues.toArray(arr);
				double[] dbArr = new double[hset_levelallValues.size()];
				for (int j = 0; j < dbArr.length; j++) 
				{
					dbArr[j]=arr[j];
				}
				FindQuartile fq = new FindQuartile();
				fq.findOutliers(dbArr);
//				Statistics stat = new Statistics(arr);
//				System.out.println("Mean Value "+stat.getMean());
//				System.out.println("Standard Daviation  "+stat.getStdDev());
//				System.out.println("Variance "+stat.getVariance());
//				WriteReadFromFile.writeSetFile_db(hset_levelallValues,"AllNumbersAtLevel_6"); 
			}
			if (!hset_levelallValues.isEmpty()) 
			{
				
				Double max = Collections.max(hset_levelallValues);
				//System.out.println(i.toString()+":"+max);
				for (Entry<String, HashMap<String, Double>> entry :hmap_addCatValuesTillDepth.entrySet()) 
				{
					if (entry.getKey().contains(Global.str_depthSeparator+i.toString())) 
					{
						HashMap<String, Double> hmap_CatAndVal = new HashMap<>(entry.getValue());
						
						HashMap<String, Double> hmap_resultCatAndVal= new LinkedHashMap<>();
						
						for (Entry<String, Double> entry_CatAndVal :hmap_CatAndVal.entrySet()) 
						{
							hmap_resultCatAndVal.put(entry_CatAndVal.getKey(), (double)(entry_CatAndVal.getValue()/max));
						}
						
						hmap_heuResultNormalized.put(entry.getKey(), hmap_resultCatAndVal);
					}
				}
			}
			else
			{
				int count=0;
				for (Entry<String, HashMap<String, Double>> entry :hmap_addCatValuesTillDepth.entrySet()) 
				{
					if (entry.getKey().contains(Global.str_depthSeparator+i.toString())) 
					{
						hmap_heuResultNormalized.put(entry.getKey(), entry.getValue());
						count++;
					}
				}
			}
		}
		return hmap_heuResultNormalized;
	}
	public static Map<String, HashMap<String, Double>>  normalize_entityAndDepthBasedNormalization(Map<String, HashMap<String, Double>> hmap_addCatValuesTillDepth) 
	{
		Map<String, HashMap<String, Double>> hmap_heuResultNormalized = new LinkedHashMap<String, HashMap<String, Double>>();
		for (Entry<String, HashMap<String, Double>> entry :hmap_addCatValuesTillDepth.entrySet()) 
		{
			if (entry.getValue().size()>0) 
			{
				hmap_heuResultNormalized.put(entry.getKey(), NormalizeMap(entry.getValue()));
			}
			else
				hmap_heuResultNormalized.put(entry.getKey(), new LinkedHashMap<>());
		}
		return hmap_heuResultNormalized;
	}
	
	public static LinkedHashMap<String, Double> NormalizeMap(Map<String, Double> hmap_ValuesToNormalize) {

		Map<String, Double> hmap_NormalizationMap = new LinkedHashMap<>();

		double max=1;
		try 
		{
			max = Collections.max(hmap_ValuesToNormalize.values());
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}

		for (Entry<String, Double> entry_CatAndVal : hmap_ValuesToNormalize.entrySet()) 
		{
			hmap_NormalizationMap.put(entry_CatAndVal.getKey(), ((double) ((double) entry_CatAndVal.getValue() / (double) max)));
		}
		return (LinkedHashMap<String, Double>) hmap_NormalizationMap;
	}

	public static  Map<String, LinkedHashMap<String, Double>>  normalization_EntityBased(Map<String, LinkedHashMap<String, Double>> hmap_heuResult,Map<String, String> hmap_groundTruth) {
		Map<String, LinkedHashMap<String, Double>> hmap_heuResultNormalized = new LinkedHashMap<>();
		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
			String str_entity = entry.getKey();
			LinkedHashMap<String, Double> lhmap_temp = new LinkedHashMap<>();
			HashSet<Double> hset_ValuesToNormalize = new HashSet<>();
			String str_entityNameAndDepth = entry.getKey();

			for (Integer i = 1; i <= Global.levelOfTheTree; i++) {

				LinkedHashMap<String, Double> ll_result = hmap_heuResult.get(str_entity + Global.str_depthSeparator + i.toString());

				for (Entry<String, Double> entry_CatAndValue : ll_result.entrySet()) {
					hset_ValuesToNormalize.add(entry_CatAndValue.getValue());
				}
			}

			if (hset_ValuesToNormalize.size() > 0) {
				Map<Double, Double> hmap_NormalizationMap = Normalization.normalizeHashSet(hset_ValuesToNormalize);

				for (Integer i = Global.levelOfTheTree; i > 0; i--) {
					LinkedHashMap<String, Double> ll_result = hmap_heuResult.get(str_entity + Global.str_depthSeparator + i.toString());
					lhmap_temp = new LinkedHashMap<>();
					for (Entry<String, Double> entry_CatAndValue : ll_result.entrySet()) {


						str_entityNameAndDepth = entry_CatAndValue.getKey();
						lhmap_temp.put(entry_CatAndValue.getKey(),
								hmap_NormalizationMap.get(entry_CatAndValue.getValue()));
					}
					hmap_heuResultNormalized.put(str_entity + Global.str_depthSeparator + i.toString(), lhmap_temp);

				}
			}
			else
			{
				for (Integer i = Global.levelOfTheTree; i > 0; i--) {
					hmap_heuResultNormalized.put(str_entity + Global.str_depthSeparator + i.toString(), lhmap_temp);
				}
			}
		}
		return hmap_heuResultNormalized;
	}
	public static Map<Double, Double> normalizeHashSet(HashSet<Double> hset_ValuesToNormalize) {
		Map<Double, Double> hmap_NormalizationMap = new HashMap<>();
		double min = Collections.min(hset_ValuesToNormalize);
		double max = Collections.max(hset_ValuesToNormalize);

		if (min == max) {
			hmap_NormalizationMap.put(min, 1.);
			return hmap_NormalizationMap;
		}

		for (Double db_val : hset_ValuesToNormalize) {
			hmap_NormalizationMap.put(db_val, ((double) ((double) db_val / (double) max)));
		}
		return hmap_NormalizationMap;
	}
}
