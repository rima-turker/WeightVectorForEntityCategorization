package util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import TreeGeneration.GlobalVariables;

public class Normalization 
{
	public static Map<String, HashMap<String, Double>>  normalize_LevelBased(Map<String, HashMap<String, Double>> hmap_addCatValuesTillDepth) 
	{
		Map<String, HashMap<String, Double>> hmap_heuResultNormalized = new LinkedHashMap<>();
		for (Integer i = 1; i <= GlobalVariables.levelOfTheTree; i++) 
		{
			HashSet<Double> hset_levelallValues= new HashSet<>();
			for (Entry<String, HashMap<String, Double>> entry :hmap_addCatValuesTillDepth.entrySet()) 
			{
				if (entry.getKey().contains(GlobalVariables.str_depthSeparator+i.toString())) 
				{
					HashMap<String, Double> hmap_entAndCat = new HashMap<>(entry.getValue());
					
					hset_levelallValues.addAll(hmap_entAndCat.values());
				}
			}
			if (!hset_levelallValues.isEmpty()) 
			{
				
				Double max = Collections.max(hset_levelallValues);
				//System.out.println(i.toString()+":"+max);
				for (Entry<String, HashMap<String, Double>> entry :hmap_addCatValuesTillDepth.entrySet()) 
				{
					if (entry.getKey().contains(GlobalVariables.str_depthSeparator+i.toString())) 
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
					if (entry.getKey().contains(GlobalVariables.str_depthSeparator+i.toString())) 
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

			for (Integer i = 1; i <= GlobalVariables.levelOfTheTree; i++) {

				LinkedHashMap<String, Double> ll_result = hmap_heuResult.get(str_entity + GlobalVariables.str_depthSeparator + i.toString());

				for (Entry<String, Double> entry_CatAndValue : ll_result.entrySet()) {
					hset_ValuesToNormalize.add(entry_CatAndValue.getValue());
				}
			}

			if (hset_ValuesToNormalize.size() > 0) {
				Map<Double, Double> hmap_NormalizationMap = Normalization.normalizeHashSet(hset_ValuesToNormalize);

				for (Integer i = GlobalVariables.levelOfTheTree; i > 0; i--) {
					LinkedHashMap<String, Double> ll_result = hmap_heuResult.get(str_entity + GlobalVariables.str_depthSeparator + i.toString());
					lhmap_temp = new LinkedHashMap<>();
					for (Entry<String, Double> entry_CatAndValue : ll_result.entrySet()) {


						str_entityNameAndDepth = entry_CatAndValue.getKey();
						lhmap_temp.put(entry_CatAndValue.getKey(),
								hmap_NormalizationMap.get(entry_CatAndValue.getValue()));
					}
					hmap_heuResultNormalized.put(str_entity + GlobalVariables.str_depthSeparator + i.toString(), lhmap_temp);

				}
			}
			else
			{
				for (Integer i = GlobalVariables.levelOfTheTree; i > 0; i--) {
					hmap_heuResultNormalized.put(str_entity + GlobalVariables.str_depthSeparator + i.toString(), lhmap_temp);
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
