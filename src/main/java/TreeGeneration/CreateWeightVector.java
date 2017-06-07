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
	private String strEntityList;
	private String strFileDistinctPaths;
	
	private String strFileName;
	final private int int_topElementCount =1;
	
	private static final Logger LOG = Logger.getLogger(EvaluateHeuristicFunctions.class.getCanonicalName());

	private Map<String, String> hmap_groundTruth;
	private static final Map<String, HashSet<String>> hmap_groundTruthlist = new LinkedHashMap<>();
	public static final Map<String, Double> hmap_fmeasureAll = new HashMap<>();
	public static final Set<Double> hset_fmeasure = new HashSet<>();
	
	
	public CreateWeightVector(final String str_fileNameGroundTruthList,final String strFileDistinct,
			final Double db_threshold, final Global.HeuristicType heu)
	{
		this.heuristic= heu;
		this.strEntityList=str_fileNameGroundTruthList;
		this.strFileDistinctPaths=strFileDistinct;
		this.threshold = db_threshold;
		
	}
	
	public CreateWeightVector(final String str_fileName,
			final Double db_threshold, final Global.HeuristicType heu)
	{
		this.heuristic= heu;
		this.threshold = db_threshold;
		this.strFileName= str_fileName;
		
	}
	public void main() throws Exception {
		
		final Map<String, HashMap<String, Double>> hmap_weightVectorLinks = WriteReadFromFile
				.readTestSet(strFileDistinctPaths);
		//WriteReadFromFile.writeMapToAFile(hmap_weightVectorLinks, "pageLinks");
		
		hmap_groundTruth = new LinkedHashMap<>(initializeGroundTruth(strEntityList));
		//WriteReadFromFile.writeMapToAFile(hmap_groundTruth, "hmap_groundTruth");
		
		final HeurisitcFunctions heurisitcFun = new HeurisitcFunctions(hmap_weightVectorLinks, getHeuristic(),
				hmap_groundTruth.size());
		
		final Map<String, HashMap<String, Double>> hmap_heuResult = new HashMap<>(heurisitcFun.callHeuristic());
		//WriteReadFromFile.writeMapToAFile(hmap_heuResult, "Heuristic_Function_Result");
		
		final Map<String, HashMap<String, Double>> hmap_addCatValuesTillDepth = aggregateCategoryValues(
				hmap_heuResult);
		
		System.out.println("finished aggregateCategoryValues "+  " size" + hmap_addCatValuesTillDepth.size());
//		
		final Map<String, HashMap<String, Double>> hmap_normalizedDepthBased = Normalization
				.normalize_LevelBased(hmap_addCatValuesTillDepth);
		
		final Map<String, HashMap<String, Double>> hmap_filteredResults = filterHeuResults(
				hmap_normalizedDepthBased, getThreshold());
		System.out.println("finished filtering "+  " size" + hmap_filteredResults.size());
		
		WriteReadFromFile.writeMapToAFile(hmap_filteredResults, "MapFormatedWeightVector_7");
//		
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
		
		System.out.println("started aggregateCategoryValues()");
		
		final Map<String, LinkedHashMap<String, Double>> hmap_result = new LinkedHashMap<>();
		
		for (Entry<String, HashMap<String, Double>> entry : hmap_heuResult.entrySet()) {
			final String str_entityNameAndDepth = entry.getKey();

			String str_depth = str_entityNameAndDepth.substring(
					str_entityNameAndDepth.indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(),
					str_entityNameAndDepth.length());
			if (str_depth.contains("_"))
			{
				str_depth = str_depth.replace("_", "");
			}
			final String str_entityName = str_entityNameAndDepth.substring(0,
					str_entityNameAndDepth.indexOf(Global.str_depthSeparator));

			final Map<String, Double> lhmap_catAnVal = new HashMap<>(entry.getValue());
			final LinkedHashMap<String, Double> lhmap_resultcatAnVal = new LinkedHashMap<>(entry.getValue());

			for (final Entry<String, Double> entry_cat : lhmap_catAnVal.entrySet()) {
				String str_cat = entry_cat.getKey();
				Double db_catVal = entry_cat.getValue();
				for (Integer i = 1; i < Integer.parseInt(str_depth); i++) {
					//System.out.println(str_entityName + Global.str_depthSeparator + i.toString());
					
					/*
					 * CHECK!!!!!!!!!!!!!!!!
					 * 
					 */
					if (hmap_heuResult.containsKey(str_entityName + Global.str_depthSeparator + i.toString())) 
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
		Map<String, LinkedHashMap<String, Double>> hmap_resultAddCat = new LinkedHashMap<>();
		Map<String, HashMap<String, Double>> hmap_resultAddCat_sort = new LinkedHashMap<>();

		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
			String str_entity = entry.getKey();

			for (int i = 0; i < Global.levelOfTheTree; i++) {
				LinkedHashMap<String, Double> ll_currCatAndVal = hmap_result
						.get(str_entity + Global.str_depthSeparator + String.valueOf(i));

				final int int_indexNext = i + 1;

				if (i == 0) {
					hmap_resultAddCat.put(str_entity + Global.str_depthSeparator + String.valueOf(int_indexNext),
							hmap_result.get(str_entity + Global.str_depthSeparator + String.valueOf(int_indexNext)));
				} else {
					LinkedHashMap<String, Double> ll_nextCatAndVal = hmap_result
							.get(str_entity + Global.str_depthSeparator + String.valueOf(int_indexNext));
					// if (ll_currCatAndVal.isEmpty())
					// {
					// hmap_resultAddCat.put(str_entity + str_depthSeparator +
					// i.toString(), ll_currCatAndVal);
					// }
					for (Entry<String, Double> entry_currcatAndVal : ll_currCatAndVal.entrySet()) {
						String str_cat = entry_currcatAndVal.getKey();

						if (!ll_nextCatAndVal.containsKey(str_cat)) {
							ll_nextCatAndVal.put(entry_currcatAndVal.getKey(), entry_currcatAndVal.getValue());
						}
					}

					hmap_resultAddCat.put(str_entity + Global.str_depthSeparator + String.valueOf(int_indexNext),
							ll_nextCatAndVal);
				}
			}
		}
		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
			String str_entity = entry.getKey();
			for (Integer i = Global.levelOfTheTree; i >= 1; i--) {
				hmap_resultAddCat_sort.put(str_entity + Global.str_depthSeparator + i.toString(),
						hmap_resultAddCat.get(str_entity + Global.str_depthSeparator + i.toString()));
			}
		}

		//ComparisonFunctions.compareMaps(hmap_heuResult, hmap_resultAddCat_sort)
		return hmap_resultAddCat_sort;
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

	public Map<String, HashMap<String, Double>> initializeTestSet(String fileName) {

		Map<String, HashMap<String, Double>> hmap_testSet = new LinkedHashMap<>();
		String str_entityName = null;
		String str_catName = null;
		Integer int_count_ = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(Global.pathLocal + fileName));) {
			String line = null;
			int depth = Global.levelOfTheTree;

			ArrayList<String> arrList_paths = new ArrayList<>();

			ArrayList<Integer> numberOfPaths = new ArrayList<>();
			LinkedHashMap<String, Double> hmap_catAndValue = new LinkedHashMap<>();
			while ((line = br.readLine()) != null) {
				line = line.toLowerCase();
				if (line.contains(",") && !line.contains("\",\"")) {

					str_entityName = line.split(",")[0].toLowerCase();
					str_catName = line.split(",")[1].toLowerCase();
					// hmap_groundTruth.put(str_entityName, str_catName);

				} else if (line.length() < 1) {
					hmap_testSet.put(str_entityName + "__" + depth, hmap_catAndValue);
					// System.out.println("WWW "+str_entityName + "__" + depth +
					// " " +hmap_catAndValue);
					hmap_catAndValue = new LinkedHashMap<>();
					depth--;
					numberOfPaths.clear();
					arrList_paths.clear();
				} else {
					if (line.contains(":")) {

						hmap_catAndValue.put(line.substring(0, line.indexOf(":")),
								Double.parseDouble(line.substring(line.indexOf(":") + 1, line.length())));
					} else if (line.contains("-")) {
						int_count_++;
					}
				}
				if (depth == 0) {
					depth = Global.levelOfTheTree;
					// hmap_entityStartingCat.put(str_entityName, ++int_count_);
					int_count_ = 0;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();

		}

		for (Integer i = 1; i <= 7; i++) {
			for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
				if (!hmap_testSet.containsKey(entry.getKey() + Global.str_depthSeparator + i.toString())) {
					System.out.println(entry);
				}
			}
		}
		return hmap_testSet;
	}

	public double getThreshold() {
		return threshold;
	}

	public Global.HeuristicType getHeuristic() {
		return heuristic;
	}
	public int getInt_topElementCount() {
		return int_topElementCount;
	}
	public Map<String, String> getHmap_groundTruth() {
		return hmap_groundTruth;
	}

}
