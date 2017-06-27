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
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.junit.experimental.theories.Theories;

import TreeGeneration.Global.HeuristicType;
import util.ComparisonFunctions;
import util.MapUtil;
import util.Normalization;
import util.PrecisionAndRecallCalculator;
import util.Statistics;

public class EvaluateHeuristicFunctions {

	private double threshold;
	private  Global.HeuristicType heuristic;
	private String str_fileNameGroundTruthList;
	final private int int_topElementCount =1;
	private String strTestFileName;
	private static final Logger LOG = Logger.getLogger(EvaluateHeuristicFunctions.class.getCanonicalName());

	private final Map<String, ArrayList<Double>> hmap_subCategoryCount = new HashMap<>();
	//private Map<String, String> hmap_groundTruth;
	private Map<String, HashSet<String>> hmap_groundTruthlist ;
	
	public static final Map<String, Double> hmap_fmeasureAll = new HashMap<>();
	public static final  Map<String, Double> hmapPrecisionAll = new HashMap<>();
	public static final  Map<String, Double> hmapRecallAll = new HashMap<>();
	
	public static final Set<Double> hset_fmeasure = new HashSet<>();
	
	private Map<String, HashMap<String, Double>> hmap_filteredResults;
	private Map<String, HashMap<String, Double>> hmap_testSetDistinctPaths;
	
	public EvaluateHeuristicFunctions()
	{
		
		
	}

	public EvaluateHeuristicFunctions(final String str_fileNameGroundTruthList,String strTestFileName,
			final Double db_threshold, final Global.HeuristicType heu)
	{
		this.heuristic= heu;
		this.str_fileNameGroundTruthList=str_fileNameGroundTruthList;
		this.threshold = db_threshold;
		this.strTestFileName= strTestFileName;
		this.hmap_groundTruthlist = new LinkedHashMap<>(initializeGroundTruthList(this.str_fileNameGroundTruthList));
		
	}
	

	/**
	 * 
	 */
	public void main()  {
		
		try {
			//hmap_groundTruth = new LinkedHashMap<>(initializeGroundTruthAndList(str_fileNameGroundTruthList));
			hmap_testSetDistinctPaths = WriteReadFromFile
					.readTestSet_tab(strTestFileName);
			HashSet<String> setTestSetEntities = new HashSet<>(MapUtil.getKeySetFromMap(hmap_testSetDistinctPaths));
			HashSet<String> setGTEntities = new HashSet<>(MapUtil.getKeySetFromMap_2(hmap_groundTruthlist));
			
			ComparisonFunctions.compareTwoHashSet(setTestSetEntities, setGTEntities);
////			
//			final Map<String, HashMap<String, Double>> hmap_Testsecond = WriteReadFromFile
//					.readTestSet_coma(Global.pathTestFile_coma);
//			
//			ComparisonFunctions.compareMaps(hmap_testSetDistinctPaths, hmap_Testsecond);
			
//			final Map<String, HashSet<String>> mapMeIncluded = WriteReadFromFile
//					.readFileToMap(Global.goalSetFileMe);
//			ComparisonFunctions.compareMaps_(hmap_groundTruthlist, mapMeIncluded);

//			
			HeurisitcFunctions heurisitcFun = new HeurisitcFunctions(hmap_testSetDistinctPaths, getHeuristic(),
					hmap_testSetDistinctPaths.size()/Global.levelOfTheTree);
			final Map<String, HashMap<String, Double>> hmap_heuResult = new LinkedHashMap<>(heurisitcFun.callHeuristic());
			
			final Map<String, HashMap<String, Double>> hmap_addCatValuesTillDepth = aggregateCategoryValues(
					hmap_heuResult);
			Print.printMap(hmap_addCatValuesTillDepth);
			final Map<String, HashMap<String, Double>> hmap_normalizedDepthBased = Normalization
					.normalize_LevelBased(hmap_addCatValuesTillDepth);

			this.hmap_filteredResults = new HashMap<>(filterHeuResults(hmap_normalizedDepthBased, getThreshold()));
			calculatePreRcallFscore_levelBased(hmap_filteredResults,getHmap_groundTruthlist());
		} 
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	public static Map<String, HashMap<String, Double>> aggregateCategoryValues(
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
					Map<String, Double> lhmap_temp = new LinkedHashMap<>(
							hmap_heuResult.get(str_entityName + Global.str_depthSeparator + i.toString()));

					if (lhmap_temp.containsKey(str_cat)) {
						db_catVal += lhmap_temp.get(str_cat);
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
					if (ll_nextCatAndVal==null) 
					{
						System.out.println(str_entity+" Null exception");
					}
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
		//Print.printHashMapFormated(hmap_resultAddCat);
		return hmap_resultAddCat;
		
		
		/*
		 * Only for sorting
		 */
//		for (String str:MapUtil.getKeySetFromMap(hmap_heuResult)) {
//			 String str_entity = str;
//			for (Integer i = Global.levelOfTheTree; i >= 1; i--) {
//				hmap_resultAddCat_sort.put(str_entity + str_depthSeparator + i.toString(),
//						hmap_resultAddCat.get(str_entity + str_depthSeparator + i.toString()));
//			}
//		}
//		
//		System.out.println("-*----------------------------------");
//		Print.printMap(hmap_resultAddCat_sort);
//		return hmap_resultAddCat_sort;
	}

	public Map<String, Double> calculatePreRcallFscore_levelBased(
			final Map<String, HashMap<String, Double>> hmap_filteredEntDepthBased,Map<String, HashSet<String>> hmap_groundTruthlist) throws Exception {
		
		Map<String, HashMap<String, Double>> hmap_precisionRecall = new HashMap<>();
		Map<String, Double> hmap_fmeasure = new HashMap<>();
		int count = 0;
		
		for (Entry<String, HashMap<String, Double>> entry : hmap_filteredEntDepthBased.entrySet()) {
			
			String str_entityNameAndDepth = entry.getKey();
			String str_entityName=str_entityNameAndDepth.split("\t")[0];
			String str_depth =str_entityNameAndDepth.split("\t")[1];

		//	System.out.print(str_entityNameAndDepth+"\t");
			
			if (hmap_groundTruthlist.containsKey(str_entityName))
			{
				HashSet<String> hset_temp = new HashSet<>();
				HashMap<String, Double> hmap_temp = entry.getValue();
				for (Entry<String, Double> entry_CatAndVal : hmap_temp.entrySet()) {
					hset_temp.add(entry_CatAndVal.getKey());
				}
				HashSet<String> hset_goal = new HashSet<>(hmap_groundTruthlist.get(str_entityName));
				hmap_precisionRecall.put(entry.getKey(),PrecisionAndRecallCalculator.calculatePrecisionRecall
						(hset_goal, entry.getValue().keySet()));
			}
			else
			{
				
				System.err.println("--------------------------"+str_entityNameAndDepth+" not in the ground truth System Exit"+"--------------------------");
				System.exit(1);
			}
		}
	
		String str_Pre = "=SPLIT(\"";
		String str_Rec = "=SPLIT(\"";
		String str_Fsco = "=SPLIT(\"";

		for (Integer int_depth = Global.levelOfTheTree; int_depth > 0; int_depth--) {
			int int_NumberOfEntities = 0;
			Double[] arr_Pre = new Double[Global.levelOfTheTree];
			Arrays.fill(arr_Pre, 0.);
			Double[] arr_Rec = new Double[Global.levelOfTheTree];
			Arrays.fill(arr_Rec, 0.);

			for (Entry<String, HashMap<String, Double>> entry : hmap_precisionRecall.entrySet()) {
				String str_entityNameAndDepth = entry.getKey();
				String str_entityName=str_entityNameAndDepth.split("\t")[0];
				String str_depth =str_entityNameAndDepth.split("\t")[1];
				
				if (Integer.parseInt(str_depth) == int_depth) 
				{
					int_NumberOfEntities++;
					HashMap<String, Double> hmap_preRcalFsco = entry.getValue();

					arr_Pre[int_depth - 1] += hmap_preRcalFsco.get("Precision");
					// System.out.println("precision:"+ (int_depth - 1) +"
					// "+arr_Pre[int_depth - 1]);

					arr_Rec[int_depth - 1] += hmap_preRcalFsco.get("Recall");
					// System.out.println("recall:"+ (int_depth - 1)+"
					// "+arr_Rec[int_depth - 1]);

				}
				
			}
			
			Locale.setDefault(Locale.US);
			DecimalFormat df = new DecimalFormat("0.00000");

			final String averagePrecision = df.format(arr_Pre[int_depth - 1] / int_NumberOfEntities);
			final String averageRecall = df.format(arr_Rec[int_depth - 1] / int_NumberOfEntities);
			
			hmapPrecisionAll.put(getHeuristic()+" "+ getThreshold()+" Precision"+Global.str_depthSeparator+int_depth.toString()
			, Double.valueOf(averagePrecision));
			
			hmapRecallAll.put(getHeuristic()+" "+ getThreshold()+" Recall"+Global.str_depthSeparator+int_depth.toString()
			, Double.valueOf(averageRecall));
			
			double averageFScore = 0;
			if (Double.parseDouble(averageRecall) + Double.parseDouble(averagePrecision) != 0) {
//				averageFScore = 2 * (Double.parseDouble(averagePrecision) * Double.parseDouble(averageRecall))
//						/ (Double.parseDouble(averagePrecision) + Double.parseDouble(averageRecall));
				averageFScore = PrecisionAndRecallCalculator.FmeasureCalculate(Double.parseDouble(averagePrecision), Double.parseDouble(averageRecall));
				hset_fmeasure.add(averageFScore);
			}
			hmap_fmeasure.put(getHeuristic()+" Fmeasure"+Global.str_depthSeparator+int_depth.toString()
					, averageFScore);
			hmap_fmeasureAll.put(getHeuristic()+" "+ getThreshold()+" Fmeasure"+Global.str_depthSeparator+int_depth.toString()
			, averageFScore);
			str_Pre = str_Pre + " ," + averagePrecision;
			str_Rec = str_Rec + " ," + averageRecall;
			str_Fsco = str_Fsco + " ," + df.format(averageFScore);
		}
		str_Pre += "\",\",\")";
		str_Rec += "\",\",\")";
		str_Fsco += "\",\",\")";

		System.out.println(str_Pre.replace("=SPLIT(\" ,", "=SPLIT(\"Precision ,"));
		System.out.println(str_Rec.replace("=SPLIT(\" ,", "=SPLIT(\"Recall ,"));
		System.out.println(str_Fsco.replace("=SPLIT(\" ,", "=SPLIT(\"Fmeasure ,"));
		
		return hmap_fmeasure;
	}
	public static double GetAverageArray(Double[] arr) {
		double sum = 0;
		double size = arr.length;

		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}

		return sum / size;
	}

		public static Map<String, String> initializeGroundTruth(String fileName) {
		
		Map<String, String> hmap_groundTruth = new LinkedHashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(Global.pathLocal + fileName));) {

			String str_entity = null, str_mainCat = null;
			String line;
			while ((line = br.readLine()) != null) {
				line = line.toLowerCase();
				if (line == null) {
					// System.out.println("--------------------------------------");
				}
				HashSet<String> ll_goalSet = new HashSet<>();
				String[] str_split = line.split("\t");
				for (int i = 0; i < str_split.length; i++) {
					str_entity = str_split[0];
					str_mainCat = str_split[1];

					if (i != 0) {
						ll_goalSet.add(str_split[i]);
					}
				}

				hmap_groundTruth.put(str_entity, str_mainCat);
				if (hmap_groundTruth.size() > 100) {
					// System.out.println("--------------------------------------");
				}
			}

		} catch (IOException e) {

			System.out.println(e.getMessage());
			e.printStackTrace();

		}
		return hmap_groundTruth;
	}
	
	public static Map<String, HashSet<String>> initializeGroundTruthList(String fileName) {
		
		Map<String, String> hmap_groundTruth = new LinkedHashMap<>();
		Map<String, HashSet<String>> hmap_groundTruthlist = new LinkedHashMap<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName));) {

			String str_entity = null, str_mainCat = null;
			String line;
			while ((line = br.readLine()) != null) {
				line = line.toLowerCase();
				if (line == null) {
					// System.out.println("--------------------------------------");
				}
				HashSet<String> ll_goalSet = new HashSet<>();
				String[] str_split = line.split("\t");
				for (int i = 0; i < str_split.length; i++) {
					str_entity = str_split[0];
					str_mainCat = str_split[1];

					if (i != 0) {
						ll_goalSet.add(str_split[i]);
					}
				}

				hmap_groundTruth.put(str_entity, str_mainCat);
				hmap_groundTruthlist.put(str_entity, ll_goalSet);
				if (hmap_groundTruth.size() > 100) {
					// System.out.println("--------------------------------------");
				}
			}

			for (Entry<String, HashSet<String>> entry : hmap_groundTruthlist.entrySet()) {
				str_entity = entry.getKey();
				HashSet<String> str_categories = entry.getValue();

				// System.out.println(str_entity+ " "+ str_categories);
			}
		} catch (IOException e) {

			System.out.println(e.getMessage());
			e.printStackTrace();

		}
		return hmap_groundTruthlist;
	}

	public static LinkedHashMap<String, HashMap<String, Double>> testSetAddResourcePaths(
			LinkedHashMap<String, HashMap<String, Double>> hmap_testSet, String str_fileName) {
		int int_entityCountAddPath = 0;
		HashMap<String, HashMap<String, Double>> hmap_pathsFromOthers = ReadResults
				.ReadResultFromDifferentFileEntAndCat("EntityAndCatFromOtherFiles" + File.separator + str_fileName);
		// printMap(hmap_pathsFromOthers);
		LinkedHashMap<String, HashMap<String, Double>> lhmap_testSetAdded = new LinkedHashMap<>();

		for (Entry<String, HashMap<String, Double>> entry : hmap_testSet.entrySet()) {
			String str_entityName = entry.getKey().substring(0, entry.getKey().indexOf(Global.str_depthSeparator));
			if (entry.getKey().contains("1") && hmap_pathsFromOthers.containsKey(str_entityName)) {

				HashMap<String, Double> map_catAndVal = hmap_pathsFromOthers.get(str_entityName);
				HashMap<String, Double> map_catAndVal_original = hmap_testSet.get(entry.getKey());
				for (Entry<String, Double> entry_CatAndVal : map_catAndVal_original.entrySet()) {
					if (map_catAndVal.containsKey(entry_CatAndVal.getKey())) {
						map_catAndVal.put(entry_CatAndVal.getKey(), map_catAndVal.get(entry_CatAndVal.getKey())
								+ map_catAndVal_original.get(entry_CatAndVal.getKey()));
					} else {
						map_catAndVal.put(entry_CatAndVal.getKey(),
								map_catAndVal_original.get(entry_CatAndVal.getKey()));
					}

				}
				lhmap_testSetAdded.put(entry.getKey(), map_catAndVal);
				int_entityCountAddPath++;
			} else {
				lhmap_testSetAdded.put(entry.getKey(), entry.getValue());
			}
		}
		for (Entry<String, HashMap<String, Double>> entry : lhmap_testSetAdded.entrySet()) {

			// System.out.println(entry.getKey()+" "+entry.getValue());
		}

		return lhmap_testSetAdded;
	}

	public static Map<String, HashMap<String, Double>> filterHeuResults(
			Map<String, HashMap<String, Double>> hmap_normalizedDepthBased, Double threshold) {
		//Print.printHashMapFormated(hmap_normalizedDepthBased);
		Map<String, HashMap<String, Double>> hmap_resultNormalizedFiltered = new LinkedHashMap<>();
		int int_catNumberBeforeFilter = 0;
		int int_catNumberFiltered = 0;
		
		int[] arrNumberOfFilteredCats = new int[Global.levelOfTheTree];

		for (Entry<String, HashMap<String, Double>> entry : hmap_normalizedDepthBased.entrySet()) {
			// for (Entry<String, LinkedHashMap<String, Double>> entry :
			// hmap_heuResultNormalizedSorted.entrySet()) {
			String str_entityName = entry.getKey();
			final String str_depth = entry.getKey().substring(
					entry.getKey().indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(),
					entry.getKey().length());
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
					arrNumberOfFilteredCats[Integer.parseInt(str_depth)-1]+=1;
				}
			}
			hmap_resultNormalizedFiltered.put(str_entityName, hmap_catAndValFiletered);
		}
//		System.out.println("int_catNumberBeforeFilter "+ int_catNumberBeforeFilter);
//		System.out.println("int_catNumberFiltered "+ int_catNumberFiltered);
//		System.out.println(Arrays.toString(arrNumberOfFilteredCats));
		return hmap_resultNormalizedFiltered;
	}

//	private void compareResultsWithGroundTruth(Map<String, LinkedHashMap<String, Double>> hmap_heuResult) {
//		int[] arr_FoundDepth = new int[Global.levelOfTheTree];
//		int count_Cat = 0;
//		int count_NotFoundCat = 0;
//
//		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
//			String str_entity = entry.getKey();
//			String str_category = entry.getValue();
//
//			boolean changed = false;
//			for (int i = 0; i < 7; i++) {
//				Integer int_index = i + 1;
//				LinkedHashMap<String, Double> ll_result = hmap_heuResult
//						.get(str_entity + str_depthSeparator + int_index.toString());
//
//				if (ll_result == null) {
//					continue;
//				}
//				if (ll_result.size() > 0) {
//					// String str_resCat= ll_result.keySet().iterator().next();
//					///////////////////////////////////////////////////////
//					final Double maxNumber = ll_result.values().iterator().next();
//					final List<String> firstElements = ll_result.entrySet().stream()
//							.filter(p -> p.getValue() >= maxNumber).map(p -> p.getKey()).collect(Collectors.toList());
//					// System.err.println(str_entity+"\t"+int_index+"\t"+str_category
//					// +"\t"+firstElements+"\t"+firstElements.contains(str_category));
//					// System.out.println(str_entity+" "+firstElements);
//					if (firstElements.contains(str_category))
//					///////////////////////////////////////////////////////
//					// if (str_resCat.equals(str_category))
//					{
//						if (int_index == 7) {
//							// System.out.println(entry.getValue()+" "+
//							// entry.getKey());
//						}
//						// System.out.println(entry.getValue()+" "+
//						// entry.getKey());
//						changed = true;
//						// System.out.println(int_index+" "+str_entity+"
//						// "+str_category);
//						arr_FoundDepth[i] += 1;
//						// System.out.println(str_entity+" "+firstElements+" "+
//						// firstElements+" "+arr_FoundDepth[i]);
//						count_Cat++;
//						break;
//					}
//				}
//
//			}
//			if (!changed) {
//				// System.out.println("XXXXXXXXXXX "+str_entity);
//				count_NotFoundCat++;
//			}
//
//		}
//		// System.out.println("=SPLIT(\"" + formatResult[i] + "\",\",\")");
//		String str_formated = "=SPLIT(\"";
//		for (int i = arr_FoundDepth.length - 1; i >= 0; i--) {
//
//			str_formated = str_formated + " ," + arr_FoundDepth[i];
//			// System.out.println(str_formated);
//		}
//
//		str_formated += "\",\",\")";
//		System.out.println(str_formated.replace("SPLIT(\" ,", "str_formated=SPLIT(\""));
//		// System.out.println(str_formated.replace("=SPLIT(\" ,", "=SPLIT(\""));
//
//		// System.out.println("Entity Count: "+ hmap_groundTruth.size());
//		// System.out.println("Total Found Category Number: "+ count_Cat );
//		// System.out.println("Total NOT Found Category Number: "+
//		// count_NotFoundCat );
//	}

	private LinkedHashMap<String, Double> sortByValue(Map<String, Double> unsortMap) {
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public void ReadSubCategoryNumber() {
		String[] subCount = null;

		int[] int_subCount;
		double[] double_subCount;
		BufferedReader brC;
		ArrayList<Double> arrListTemp;
		try {
			brC = new BufferedReader(new FileReader(Global.pathLocal + "SubCategory_Count.csv"));
			String lineCategory = null;

			while ((lineCategory = brC.readLine()) != null) {

				lineCategory = lineCategory.toLowerCase();
				arrListTemp = new ArrayList<>();
				// System.out.println(lineCategory);
				subCount = (lineCategory.substring(lineCategory.indexOf(":,") + 2, lineCategory.length()).split(","));
				int_subCount = Arrays.stream(subCount).mapToInt(Integer::parseInt).toArray();

				for (int i = 0; i < int_subCount.length; i++) {
					arrListTemp.add((double) int_subCount[i]);
				}
				hmap_subCategoryCount.put(lineCategory.substring(0, lineCategory.indexOf(":")), arrListTemp);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


//	public void testNormalization(Map<String, LinkedHashMap<String, Double>> hmap_resultNormalizedFiltered) {
//		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
//			String str_entity = entry.getKey();
//			LinkedHashMap<String, Double> lhmap_temp = new LinkedHashMap<>();
//			HashSet<Double> hset_ValuesToNormalize = new HashSet<>();
//			String str_entityNameAndDepth = entry.getKey();
//
//			LinkedList<Double> llist_test = new LinkedList<>();
//			for (Integer i = 1; i <= Global.levelOfTheTree; i++) {
//
//				LinkedHashMap<String, Double> ll_result = hmap_resultNormalizedFiltered
//						.get(str_entity + str_depthSeparator + i.toString());
//
//				if (hmap_resultNormalizedFiltered.get(str_entity + str_depthSeparator + i.toString()) != null) {
//					for (Entry<String, Double> entry_CatAndValue : ll_result.entrySet()) {
//						llist_test.add(entry_CatAndValue.getValue());
//					}
//				}
//			}
//			// LinkedList<Double> ll_de = new LinkedList<>();
//			if (llist_test.size() > 0 && !Collections.max(llist_test).equals(1.)) {
//				System.out.println(str_entity + "HATA");
//			}
//			// if (llist_test.size()==0) {
//			//
//			// System.out.println(str_entity);
//			//
//			// }
//
//		}
//	}

//	public Map<String, HashMap<String, Double>> initializeTestSet(String fileName) {
//
//		Map<String, HashMap<String, Double>> hmap_testSet = new LinkedHashMap<>();
//		String str_entityName = null;
//		String str_catName = null;
//		Integer int_count_ = 0;
//		try (BufferedReader br = new BufferedReader(new FileReader(Global.pathLocal + fileName));) {
//			String line = null;
//			int depth = Global.levelOfTheTree;
//
//			ArrayList<String> arrList_paths = new ArrayList<>();
//
//			ArrayList<Integer> numberOfPaths = new ArrayList<>();
//			LinkedHashMap<String, Double> hmap_catAndValue = new LinkedHashMap<>();
//			while ((line = br.readLine()) != null) {
//				line = line.toLowerCase();
//				if (line.contains(",") && !line.contains("\",\"")) {
//
//					str_entityName = line.split(",")[0].toLowerCase();
//					str_catName = line.split(",")[1].toLowerCase();
//					// hmap_groundTruth.put(str_entityName, str_catName);
//
//				} else if (line.length() < 1) {
//					hmap_testSet.put(str_entityName + "__" + depth, hmap_catAndValue);
//					// System.out.println("WWW "+str_entityName + "__" + depth +
//					// " " +hmap_catAndValue);
//					hmap_catAndValue = new LinkedHashMap<>();
//					depth--;
//					numberOfPaths.clear();
//					arrList_paths.clear();
//				} else {
//					if (line.contains(":")) {
//
//						hmap_catAndValue.put(line.substring(0, line.indexOf(":")),
//								Double.parseDouble(line.substring(line.indexOf(":") + 1, line.length())));
//					} else if (line.contains("-")) {
//						int_count_++;
//					}
//				}
//				if (depth == 0) {
//					depth = Global.levelOfTheTree;
//					// hmap_entityStartingCat.put(str_entityName, ++int_count_);
//					int_count_ = 0;
//				}
//			}
//		} catch (IOException e) {
//
//			e.printStackTrace();
//
//		}
//
//		for (Integer i = 1; i <= 7; i++) {
//			for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
//				if (!hmap_testSet.containsKey(entry.getKey() + str_depthSeparator + i.toString())) {
//					System.out.println(entry);
//				}
//			}
//		}
//		return hmap_testSet;
//	}
	
	public double getThreshold() {
		return threshold;
	}

	public Global.HeuristicType getHeuristic() {
		return heuristic;
	}
	public int getInt_topElementCount() {
		return int_topElementCount;
	}

	public Map<String, HashMap<String, Double>> getHmap_filteredResults() {
		return hmap_filteredResults;
	}
	public  Map<String, HashSet<String>> getHmap_groundTruthlist() {
		return hmap_groundTruthlist;
	}
	public Map<String, HashMap<String, Double>> getHmap_testSetDistinctPaths() {
		return hmap_testSetDistinctPaths;
	}
	public Map<String, Double> getHmapPrecisionAll() {
		return hmapPrecisionAll;
	}



}