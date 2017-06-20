package TreeGeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;

import util.MapUtil;
import util.Normalization;
import util.PrecisionAndRecallCalculator;
import util.Statistics;

public class CreateWeightVector 
{
	private double threshold;
	private  Global.HeuristicType heuristic;
	private String strFileDistinctPaths;
	
	private static final Logger LOG = Logger.getLogger(EvaluateHeuristicFunctions.class.getCanonicalName());
	
	public CreateWeightVector(final String strFileDistinct,
			final Double db_threshold, final Global.HeuristicType heu)
	{
		this.heuristic= heu;
		this.strFileDistinctPaths=strFileDistinct;
		this.threshold = db_threshold;
		
	}
	public void main() throws Exception {
		
		/*
		 * 
		 */
		final Map<String, HashMap<String, Double>> hmap_weightVectorLinks = WriteReadFromFile
				.readTestSet_tab(strFileDistinctPaths);
		
		System.out.println("finished readingValues "+  " size" + hmap_weightVectorLinks.size());
		HashSet<String> entities = new HashSet<>(MapUtil.getKeySetFromMap(hmap_weightVectorLinks));
		System.out.println("Unique entity size "+ entities.size());
		
		System.out.println("Map Size Should be " + entities.size()*7+ "nbut "+ hmap_weightVectorLinks.size());
		final Map<String, HashMap<String, Double>> hmap_FixedweightVectorLinks = WriteReadFromFile.fixSpecialCharProblem(hmap_weightVectorLinks, entities);
		
		final HeurisitcFunctions heurisitcFun = new HeurisitcFunctions(hmap_FixedweightVectorLinks, getHeuristic(),
				hmap_weightVectorLinks.size()/Global.levelOfTheTree);
		
//		final HeurisitcFunctions heurisitcFun = new HeurisitcFunctions(hmap_weightVectorLinks, getHeuristic(),
//				hmap_weightVectorLinks.size()/Global.levelOfTheTree);
//		
		final Map<String, HashMap<String, Double>> hmap_heuResult = new HashMap<>(heurisitcFun.callHeuristic());
	
		System.out.println("finished heuristic Function "+  " size" + hmap_heuResult.size());
		
		final Map<String, HashMap<String, Double>> hmap_addCatValuesTillDepth = aggregateCategoryValues(
				hmap_heuResult);
		
		System.out.println("finished aggregateCategoryValues "+  " size" + hmap_addCatValuesTillDepth.size());
		
		Map<String, HashMap<String, Double>> mapAggregatedTillDepth= new HashMap<>(MapUtil.getAsMapCertainLevel(7,hmap_addCatValuesTillDepth));
		
		System.out.println("Only 7th layer size "+ mapAggregatedTillDepth.size());
		
		WriteReadFromFile.writeMapToAFile(mapAggregatedTillDepth,"aggregatedOnly7level");
		
		System.out.println("Finished writing");
		
		final Map<String, HashMap<String, Double>> hmap_normalizedDepthBased = Normalization
				.normalize_LevelBased(mapAggregatedTillDepth);
		
//		final Map<String, HashMap<String, Double>> hmap_normalizedDepthBased = Normalization
//				.normalize_LevelBased(hmap_addCatValuesTillDepth);
//		
		final Map<String, HashMap<String, Double>> hmap_filteredResults = filterHeuResults(
				hmap_normalizedDepthBased, getThreshold());
		
//		System.out.println("finished filtering "+  " size" + hmap_filteredResults.size());
//		
		WriteReadFromFile.writeMapToAFile(hmap_filteredResults, "MapFormatedWeightVector_7");
		System.out.println("Finished");

	}
	 
	public static Map<String, HashMap<String, Double>> filterHeuResults(
			Map<String, HashMap<String, Double>> hmap_normalizedDepthBased, Double threshold) {

		Map<String, HashMap<String, Double>> hmap_resultNormalizedFiltered = new LinkedHashMap<>();
		int int_catNumberBeforeFilter = 0;
		int int_catNumberFiltered = 0;

		for (Entry<String, HashMap<String, Double>> entry : hmap_normalizedDepthBased.entrySet()) {
			// for (Entry<String, LinkedHashMap<String, Double>> entry :
			// hmap_heuResultNormalizedSorted.entrySet()) {
			String str_entityName = entry.getKey();

			LinkedHashMap<String, Double> hmap_Values = (LinkedHashMap<String, Double>) entry.getValue();
			LinkedHashMap<String, Double> hmap_catAndValFiletered = new LinkedHashMap<>();

			for (Entry<String, Double> entry_hmapValues : hmap_Values.entrySet()) {
				String str_catName = entry_hmapValues.getKey();
				Double db_value = entry_hmapValues.getValue();
				int_catNumberBeforeFilter++;
				if (db_value >= threshold) {
					hmap_catAndValFiletered.put(str_catName, db_value);
				} else {
					int_catNumberFiltered++;
				}
			}
			hmap_resultNormalizedFiltered.put(str_entityName, hmap_catAndValFiletered);
		}
		return hmap_resultNormalizedFiltered;
	}

	
	public Map<String, HashMap<String, Double>> aggregateCategoryValues(
			Map<String, HashMap<String, Double>> hmap_heuResult) {
	
		final Map<String, LinkedHashMap<String, Double>> hmap_result = new LinkedHashMap<>();
		/*
		 * 	First we aggregate the paths such as 
		 *  anne_claude_de_caylus__4 = {archaeology=1.0}
			anne_claude_de_caylus__3 = {history=1.0}
			anne_claude_de_caylus__5 = {politics=1.0, archaeology=1.0, history=1.0}
			-*----------------------------------
			anne_claude_de_caylus__4 = {archaeology=1.0}
			anne_claude_de_caylus__3 = {history=1.0}
			anne_claude_de_caylus__5 = {politics=1.0, archaeology=2.0, history=2.0}
		 */
		for (Entry<String, HashMap<String, Double>> entry : hmap_heuResult.entrySet()) {
			
			String str_entityNameAndDepth = entry.getKey();
			String str_entityName=str_entityNameAndDepth.split("\t")[0];
			String str_depth =str_entityNameAndDepth.split("\t")[1];
			
			final Map<String, Double> lhmap_catAnVal = new HashMap<>(entry.getValue());
			final LinkedHashMap<String, Double> lhmap_resultcatAnVal = new LinkedHashMap<>(entry.getValue());

			for (final Entry<String, Double> entry_cat : lhmap_catAnVal.entrySet()) {
				String str_cat = entry_cat.getKey();
				Double db_catVal = entry_cat.getValue();
				for (Integer i = 1; i < Integer.parseInt(str_depth); i++) {
					
					if (!hmap_heuResult.containsKey(str_entityName + Global.str_depthSeparator + i.toString())) 
					{
						System.out.println("Does not contain entitiy "+str_entityName + Global.str_depthSeparator + i.toString());
					}
					else
					{
						Map<String, Double> lhmap_temp = new LinkedHashMap<>(
								
								hmap_heuResult.get(str_entityName + Global.str_depthSeparator + i.toString()));
								
						if (lhmap_temp.containsKey(str_cat)) {
							db_catVal += lhmap_temp.get(str_cat);
						}
					}
					
				}
				lhmap_resultcatAnVal.put(str_cat, db_catVal);
			}
			hmap_result.put(str_entityNameAndDepth, lhmap_resultcatAnVal);
		}
		//Print.printMap(hmap_result);
		
		Map<String, HashMap<String, Double>> hmap_resultAddCat = new LinkedHashMap<>();
		Map<String, HashMap<String, Double>> hmap_resultAddCat_sort = new LinkedHashMap<>();
		
//		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
//			String str_entity = entry.getKey();
		
		for (String str:MapUtil.getKeySetFromMap(hmap_heuResult)) {
			 String str_entity = str;
		
			for (int i = 0; i < Global.levelOfTheTree; i++) {
				LinkedHashMap<String, Double> ll_currCatAndVal = hmap_result
						.get(str_entity + Global.str_depthSeparator + String.valueOf(i));

				final int int_indexNext = i + 1;

				if (i == 0) 
				{
					hmap_resultAddCat.put(str_entity + Global.str_depthSeparator + String.valueOf(int_indexNext),
							hmap_result.get(str_entity + Global.str_depthSeparator + String.valueOf(int_indexNext)));
				} 
				else {
					
					LinkedHashMap<String, Double> ll_nextCatAndVal;
					if (hmap_result.containsKey(str_entity + Global.str_depthSeparator + String.valueOf(int_indexNext))) 
					{
						ll_nextCatAndVal = hmap_result
								.get(str_entity + Global.str_depthSeparator + String.valueOf(int_indexNext));
						
						if (ll_nextCatAndVal!=null && ll_currCatAndVal!=null) 
						{
							
							for (Entry<String, Double> entry_currcatAndVal : ll_currCatAndVal.entrySet()) {
								String str_cat = entry_currcatAndVal.getKey();

								if (!ll_nextCatAndVal.containsKey(str_cat)) {
									ll_nextCatAndVal.put(entry_currcatAndVal.getKey(), entry_currcatAndVal.getValue());
								}
							}
							
						}
					}
					
					else
					{
						System.out.println(str_entity+" Null aggregation");
						ll_nextCatAndVal = new LinkedHashMap<>();
					}
					

					hmap_resultAddCat.put(str_entity + Global.str_depthSeparator + String.valueOf(int_indexNext),
							ll_nextCatAndVal);
				}
			}
		}
		return hmap_resultAddCat;
	}

	public static double GetAverageArray(Double[] arr) {
		double sum = 0;
		double size = arr.length;

		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}

		return sum / size;
	}


	public Map<String, String> initializeGroundTruth(String fileName) {

		Map<String, String> hmap_groundTruth = new LinkedHashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName));) {

			String line;
			while ((line = br.readLine()) != null) {
				line = line.toLowerCase();
				
				if (line != null) {
					hmap_groundTruth.put(line, "");
				}
			}
		}
		catch (IOException e) {

			System.out.println(e.getMessage());
			e.printStackTrace();

		}
		return hmap_groundTruth;
	}
	public static void test_CompareTwoLists(Map<String, LinkedHashMap<String, Double>> hmap_1,
			Map<String, LinkedHashMap<String, Double>> hmap_2) {
		int int_count = 0;
		for (Entry<String, LinkedHashMap<String, Double>> entry_1 : hmap_1.entrySet()) {
			String str_entityName = entry_1.getKey();
			LinkedHashMap<String, Double> lhmap_CatAndVal = entry_1.getValue();

			if (!lhmap_CatAndVal.equals(hmap_2.get(str_entityName))) {
				LOG.error("Two maps are not identical");
				System.out.println("ERROR");
			}
			int_count++;
		}
		System.out.println("Entities are tested:" + int_count);
	}

	

	public double getThreshold() {
		return threshold;
	}

	public Global.HeuristicType getHeuristic() {
		return heuristic;
	}
	

}
