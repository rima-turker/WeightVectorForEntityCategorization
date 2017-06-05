package TreeGeneration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Trash 
{
	/*
	 * private Map<String, HashMap<String, Double>> initializeTestSetDifferentType(String fileName) 
	{
		Map<String, HashMap<String, Double>> hmap_distinctTest = new HashMap<String, HashMap<String,Double>>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName));) 
		{
			String line = null;
			//carl_hagenbeck={3={zoology=2, history=1}, 4={biology=1}, 5={biology=2, philosophy=1, arts=2, zoology=1}, 6={botany=2, archaeology=1, biology=2}, 7={archaeology=4, history=1, physics=1}}
			while ((line = br.readLine()) != null) 
			{
				line = line.toLowerCase().replace(" ", "");
				System.out.println(line);
				String str_entName = line.substring(0,line.indexOf("="));
				String str_DepthCats = line.substring(line.indexOf("={")+("={").length(),line.length()).replace(" ", "");
				
				String[] str_split = str_DepthCats.split("},");
				
				
				for (int i = 0; i < str_split.length; i++) 
				{
					String str_depth = str_split[i].substring(0, 1);
					System.out.println("depth  "+str_depth);
					HashMap<String, Double> hmap_CatAndVal = new HashMap<>();
					String[] catAndVal = str_split[i].substring(1, str_split[i].length()).replace("={","").replace("}","").replaceAll(" ", "").split(",");
					System.out.println("str_split[i]   "+str_split[i]);
					for (int j = 0; j < catAndVal.length; j++) 
					{
						hmap_CatAndVal.put(catAndVal[j].substring(0, catAndVal[j].indexOf("=")), Double.valueOf(catAndVal[j].split("=")[1]));
					}
					hmap_distinctTest.put(str_entName+GlobalVariables.str_depthSeparator+str_depth, hmap_CatAndVal);
				}
				Print.printMap(hmap_distinctTest);
				for (int i = 1; i <= GlobalVariables.levelOfTheTree; i++) 
				{
					if (!hmap_distinctTest.containsKey(str_entName+GlobalVariables.str_depthSeparator+i)) 
					{
						hmap_distinctTest.put(str_entName+GlobalVariables.str_depthSeparator+i, new HashMap<>());
					}
				}
				
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		Print.printMap(hmap_distinctTest);
		return hmap_distinctTest;
		
	}
	 * private HashMap<String, Double> calculatePrecisionRecall(String str_entity, String str_depth,
			HashSet<String> hset_retreived) throws Exception {

		LinkedHashMap<String, Double> hmap_preRcall = new LinkedHashMap<>();

		double db_relevantElements, db_retrievaledElements, int_truePositive = 0;

		if (hmap_groundTruthlist.get(str_entity) == null) {
			LOG.error("entity does not exist " + str_entity);
			throw new Exception("grund truth does not contain entity: "+str_entity);
		}
		db_relevantElements = hmap_groundTruthlist.get(str_entity).size();

		double precision = 0.0;
		double recall = 0.0;

		final List<String> llist_groundTruth = new LinkedList<>(hmap_groundTruthlist.get(str_entity));

		// System.out.println(llist_groundTruth);
		// System.out.println(hset_retreived);

		db_retrievaledElements = hset_retreived.size();

		for (String str_cat : hset_retreived) {
			if (llist_groundTruth.contains(str_cat)) {
				int_truePositive += 1;
			}

		}
		if (int_truePositive != 0) {
			precision = int_truePositive / db_retrievaledElements;
			recall = int_truePositive / db_relevantElements;

			hmap_preRcall.put("Precision", precision);
			hmap_preRcall.put("Recall", recall);
		} else {
			hmap_preRcall.put("Precision", 0.);
			hmap_preRcall.put("Recall", 0.);
		}

		return hmap_preRcall;
	}
	 * public static LinkedHashMap<String, Double> calculatePrecisionRecall(String str_entity, String str_depth,
			Map<String, LinkedHashMap<String, Double>> hmap_filteredEntDepthBased) {

		LinkedHashMap<String, Double> hmap_preRcall = new LinkedHashMap<>();

		double db_relevantElements, db_retrievaledElements, int_truePositive = 0;

		if (hmap_groundTruthlist.get(str_entity) == null) {
			LOG.error("entity does not exist " + str_entity);
		}
		db_relevantElements = hmap_groundTruthlist.get(str_entity).size();

		double precision = 0.0, recall = 0.0;

		final LinkedHashMap<String, Double> lhmap_depthElements = hmap_filteredEntDepthBased
				.get(str_entity + str_depthSeparator + str_depth);
		final LinkedList<String> llist_groundTruth = hmap_groundTruthlist.get(str_entity);

		db_retrievaledElements = lhmap_depthElements.size();

		for (Entry<String, Double> entry : lhmap_depthElements.entrySet()) {
			String str_Cat = entry.getKey();

			if (llist_groundTruth.contains(str_Cat)) {
				int_truePositive += 1;
			}
			// int_truePositive=0;
		}

		if (int_truePositive != 0) {
			precision = int_truePositive / db_retrievaledElements;
			recall = int_truePositive / db_relevantElements;

			hmap_preRcall.put("Precision", precision);
			hmap_preRcall.put("Recall", recall);
		} else {
			hmap_preRcall.put("Precision", 0.);
			hmap_preRcall.put("Recall", 0.);
		}
		return hmap_preRcall;
	}
	 */
	private static LinkedHashMap<String, HashMap<String, Double>> testSetDistinctPaths(Map<String, HashMap<String, Double>> map)
	{
		LinkedHashMap<String, HashMap<String, Double>> hmap_entityAndPaths_ordered = new LinkedHashMap<>();
		LinkedHashMap<String, HashMap<String, Double>> hmap_finaltestSet = new LinkedHashMap<>();

//		for (Entry<String, String> entry_entityCatAndPath : hmap_groundTruth.entrySet()) 
//		{
//			String str_entityName = entry_entityCatAndPath.getKey();
//			for (Integer i = GlobalVariables.levelOfTheTree; i >=1 ; i--) 
//			{
//				String str_entityAndDept = str_entityName+GlobalVariables.str_depthSeparator+i.toString();
//				HashMap<String, Double> hmap_tempNewValues = new HashMap<>(); 
//				HashMap<String, Double> hmap_currentList = map.get(str_entityAndDept);
//
//				if (i == 1) 
//				{
//					hmap_tempNewValues = map.get(str_entityAndDept);
//				}
//				else
//				{
//					Integer int_indexBefore = i-1;
//					HashMap<String, Double> hmap_beforeList = map.get(str_entityName+GlobalVariables.str_depthSeparator+int_indexBefore.toString());
//					for (Entry<String, Double> entry_catAndPath : hmap_currentList.entrySet()) 
//					{
//						if (hmap_beforeList.containsKey(entry_catAndPath.getKey())) 
//						{
//							if ((entry_catAndPath.getValue()-hmap_beforeList.get(entry_catAndPath.getKey())>0)) 
//							{
//								hmap_tempNewValues.put(entry_catAndPath.getKey(), (entry_catAndPath.getValue()-hmap_beforeList.get(entry_catAndPath.getKey())));
//							}
//						}
//						else
//						{
//							hmap_tempNewValues.put(entry_catAndPath.getKey(), entry_catAndPath.getValue());
//						}
//					}
//				}
//				hmap_finaltestSet.put(str_entityAndDept, hmap_tempNewValues);
//			}
//		}
		for(Entry<String,HashMap<String, Double>> entry:map.entrySet())
		{
			//System.out.println( entry.getKey() + " = " + hmap_finaltestSet.get(entry.getKey()));
			hmap_entityAndPaths_ordered.put(entry.getKey(), hmap_finaltestSet.get(entry.getKey()));
		}
		//	printMap(hmap_entityAndPaths_ordered);
		//	printMap(hmap_testSet);
		return hmap_entityAndPaths_ordered;
	}
	
//	private static void discoverIrrelaventPaths(Map<String, LinkedHashMap<String, Double>> hmap_heuristic)
//	{
//		for (Entry<String, LinkedHashMap<String, Double>> entry : hmap_heuristic.entrySet()) 
//		{
//			String str_entityNameAndDepth = entry.getKey();
//			String str_depth = str_entityNameAndDepth.substring(str_entityNameAndDepth.indexOf(str_depthSeparator) + str_depthSeparator.length(),
//					str_entityNameAndDepth.length());
//			String str_entityName = str_entityNameAndDepth.substring(0,str_entityNameAndDepth.indexOf(str_depthSeparator));
//
//			LinkedHashMap<String, Double> lhmap_catAnVal = entry.getValue();
//			HashSet<String> hset_retreived = new HashSet<>();
//
//			for (Entry<String, Double> entry_cat : lhmap_catAnVal.entrySet()) 
//			{
//				hset_retreived.add(entry_cat.getKey());
//			}
//			final LinkedList<String> llist_groundTruth = hmap_groundTruthlist.get(str_entityName);
//
//			//	System.out.println(llist_groundTruth);
//			//	System.out.println(hset_retreived);
//
//
//			for (String str_cat: hset_retreived) 
//			{
//				if (!llist_groundTruth.contains(str_cat)) 
//				{
//					//System.out.println(str_entityName+","+str_depth+","+str_cat+",");
//				}
//
//			}
//
//		}
//
//	}
}
