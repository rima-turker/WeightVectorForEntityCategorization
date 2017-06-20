package TreeGeneration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import TreeGeneration.Global.HeuristicType;


public class HeurisitcFunctions
{
	private static final Logger LOG = Logger.getLogger(HeurisitcFunctions.class.getCanonicalName());
	private Map<String, HashMap<String, Double>> hmap_testSet;
	//private HashSet<String> hset_entityList;
	private final HeuristicType heuristicType;
	private final int intTotalEntitiyCount;
	public HeurisitcFunctions(Map<String, HashMap<String, Double>> hmap_testSetDistinctPaths, Global.HeuristicType heuristic,
			int int_totalEntityCount)
	{
		this.hmap_testSet=new LinkedHashMap<>(hmap_testSetDistinctPaths);
		this.intTotalEntitiyCount= int_totalEntityCount;
		this.heuristicType = heuristic;
	}
	
	public Map<String, HashMap<String, Double>> callHeuristic() 
	{

		Map<String, HashMap<String, Double>> hmap_heuResult = new LinkedHashMap<>();
		Map<String, Integer> hmap_tfidfCatDeptVal = new HashMap<>(initializeMapForTFIDF(hmap_testSet));
		
		for (Entry<String, HashMap<String, Double>> entry : hmap_testSet.entrySet()) {
			String str_entityNameAndDepth = entry.getKey();
			String str_entityName=str_entityNameAndDepth.split("\t")[0];
			String str_depth =str_entityNameAndDepth.split("\t")[1];
//			String str_entityName = str_entityNameAndDepth.substring(0, str_entityNameAndDepth.indexOf(Global.str_depthSeparator));
//			String str_depth = str_entityNameAndDepth.substring(
//					str_entityNameAndDepth.indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(), str_entityNameAndDepth.length());
			Map<String, Double> hmap_Values = new HashMap<>(entry.getValue());
			HashMap<String, Double> lhmap_Results = new LinkedHashMap<>();

			for (Entry<String, Double> entry_hmapValues : hmap_Values.entrySet()) {
				String str_catName = entry_hmapValues.getKey();
				Double db_pathCount = entry_hmapValues.getValue();
				Double db_heuValue = 0.0;
		
				if (getHeuristicType().equals(Global.HeuristicType.HEURISTIC_NUMBEROFPATHS)) {
					db_heuValue = Heuristic_NumberOfPaths(db_pathCount);
				} else if (getHeuristicType().equals(Global.HeuristicType.HEURISTIC_NUMBEROFPATHSANDDEPTH)) {
					db_heuValue = Heuristic_NumberOfPathsAndDepth(db_pathCount, Integer.parseInt(str_depth));
				}
				else if(getHeuristicType().equals(Global.HeuristicType.HEURISTIC_FIRSTFINDFIRSTDEPTH))
				{
					db_heuValue = Heuristic_FirstPathsAndDepth(str_entityName,db_pathCount, Integer.parseInt(str_depth)) ;
				}
				else  
					
					if(getHeuristicType().equals(Global.HeuristicType.HEURISTIC_FIRSTFINDFIRSTDEPTHEXPONENTIAL))
				{
					db_heuValue = Heuristic_FirstPathsAndDepthExp(str_entityName,db_pathCount, Integer.parseInt(str_depth)) ;
				} else if(getHeuristicType().equals(Global.HeuristicType.HEURISTIC_TFIDF))
				{
					db_heuValue = Heuristic_tfidf(str_catName,db_pathCount,str_depth,hmap_tfidfCatDeptVal) ;
				}
				else if(getHeuristicType().equals(Global.HeuristicType.HEURISTIC_COMBINATION4TH5TH))
				{
					db_heuValue = Heuristic_combination4th5th(str_entityName,str_catName,db_pathCount,str_depth,hmap_tfidfCatDeptVal) ;
				}

				lhmap_Results.put(str_catName, db_heuValue);
			}
			hmap_heuResult.put(str_entityNameAndDepth, lhmap_Results);
		}

		return hmap_heuResult;
	}

	public static Map<String, Integer> initializeMapForTFIDF(Map<String, HashMap<String, Double>> hmap_testSet)
	{
		Map<String ,Integer> hmap_resultCatDepVal = new LinkedHashMap<>();
		
		for (Entry<String, HashMap<String, Double>> entry_EntDeptCatVal : hmap_testSet.entrySet()) 
		{
			String str_entityNameAndDepth = entry_EntDeptCatVal.getKey();
			String str_entityName=str_entityNameAndDepth.split("\t")[0];
			String str_depth =str_entityNameAndDepth.split("\t")[1];
			
			Integer int_dept = Integer.parseInt(str_depth);
			HashMap<String, Double> hmap_CatVal = entry_EntDeptCatVal.getValue();

			//	LinkedHashMap<String, Double> lhmap_Results = (LinkedHashMap<String, Double>) entry_EntDeptCatVal.getValue();

			for (Entry<String, Double> entry_hmapValues : hmap_CatVal.entrySet()) 
			{
				String str_catName = entry_hmapValues.getKey();

				String str_catDepth = entry_hmapValues.getKey()+Global.str_depthSeparator+str_depth;

				if (hmap_resultCatDepVal.containsKey(str_catDepth)) 
				{
					Integer intVal = hmap_resultCatDepVal.get(str_catDepth)+1;
					hmap_resultCatDepVal.put(str_catDepth, intVal);
				} 
				else 
				{
					hmap_resultCatDepVal.put(str_catDepth, 1);
				}
			}
		}
		return hmap_resultCatDepVal;
	}

	private static double Heuristic_NanHeuristic(double db_Value) {
		return 1.0;
	}

	private static double Heuristic_NumberOfPaths(double db_Value) {
		return db_Value;
	}

	private static double Heuristic_NumberOfPathsAndDepth(double db_Value, int int_depth) {
		return (double) (db_Value / (double) int_depth);
	}

	private double Heuristic_FirstPathsAndDepth(String str_entity,double db_Value, int int_depth) 
	{
		int int_entitystartingDepth= findLevelFirstPathDiscovered(getHmap_testSet(), str_entity);
		if (!(int_entitystartingDepth>=0)) 
		{
			LOG.error("Entity not found hmap_entityStartingCat"+ str_entity);
		}
		if (int_depth<int_entitystartingDepth) 
		{
			return 0;
		}
		return (double) (db_Value / (double) (int_depth-(int_entitystartingDepth-1)));
	}
	private double Heuristic_tfidf(String str_catName,double db_Value, String str_depth,Map<String, Integer> hmap_tfidfCatDeptVal) 
	{
		if (hmap_tfidfCatDeptVal.containsKey(str_catName+Global.str_depthSeparator+str_depth))
		{
			return (double) (db_Value * (double) Math.log10(this.intTotalEntitiyCount*1.0 /(double) hmap_tfidfCatDeptVal.get(str_catName+Global.str_depthSeparator+str_depth)));
		}
		else
		{
			//System.out.println(str_catName+str_depthSeparator+str_depth);
			System.err.println("ERROR");
			return 0.;
		}
	}
	private double Heuristic_combination4th5th_smooth(String str_entity,String str_catName,double db_Value, String str_depth,Map<String, Integer> hmap_tfidfCatDeptVal) 
	{
		int int_depth= Integer.parseInt(str_depth);
		int int_entitystartingDepth=  findLevelFirstPathDiscovered(getHmap_testSet(), str_entity);;
		if (hmap_tfidfCatDeptVal.containsKey(str_catName+Global.str_depthSeparator+str_depth))
		{
			return (double) ((db_Value / Math.pow(2.0,(double) (int_depth-int_entitystartingDepth))))* (double) Math.log10(1+(this.intTotalEntitiyCount *1.0 / hmap_tfidfCatDeptVal.get(str_catName+Global.str_depthSeparator+str_depth)));
		}																							
		else
		{
			System.out.println(str_catName+Global.str_depthSeparator+str_depth);
			System.err.println("ERROR");
			return 0.;
		}
	}
	private double Heuristic_combination4th5th(String str_entity,String str_catName,double db_Value, String str_depth,Map<String, Integer> hmap_tfidfCatDeptVal) 
	{
		int int_depth= Integer.parseInt(str_depth);
		int int_entitystartingDepth=  findLevelFirstPathDiscovered(getHmap_testSet(), str_entity);;
		if (hmap_tfidfCatDeptVal.containsKey(str_catName+Global.str_depthSeparator+str_depth))
		{
			return (double) ((db_Value / Math.pow(2.0,(double) (int_depth-int_entitystartingDepth))))* (double) Math.log10(((this.intTotalEntitiyCount-hmap_tfidfCatDeptVal.get(str_catName+Global.str_depthSeparator+str_depth)) *1.0) / hmap_tfidfCatDeptVal.get(str_catName+Global.str_depthSeparator+str_depth));
		}																							
		else
		{
			System.out.println(str_catName+Global.str_depthSeparator+str_depth);
			System.err.println("ERROR");
			return 0.;
		}
	}
	private double Heuristic_FirstPathsAndDepthExp(String str_entity,double db_Value, int int_depth) 
	{
		int int_entitystartingDepth=  findLevelFirstPathDiscovered(getHmap_testSet(), str_entity);
		if (!(int_entitystartingDepth>=0)) 
		{
			LOG.error("Entity not found hmap_entityStartingCat"+ str_entity);
		}
		if (int_depth<int_entitystartingDepth) 
		{
			return 0;
		}
		return (double) (db_Value / Math.pow(2.0,(double)(int_depth-(int_entitystartingDepth))));
	}

	public static int findLevelFirstPathDiscovered(Map<String, HashMap<String, Double>> hmap_testSet,String str_entity) {

		for (Integer i = 1; i <= Global.levelOfTheTree; i++) 
		{
			
			if (!hmap_testSet.containsKey(str_entity+Global.str_depthSeparator+i.toString())) 
			{
				LOG.error("Entity not found  :findLevelFirstPathDiscovered "+ str_entity);
				System.out.println("Entity not found  :findLevelFirstPathDiscovered "+ str_entity+Global.str_depthSeparator+i.toString());
			}
			else
			{
				HashMap<String, Double> hmap_catAndVal = hmap_testSet.get(str_entity+Global.str_depthSeparator+i.toString());
				if (!hmap_catAndVal.isEmpty()) 
				{
					return i;
				}
			}
			
		}
		LOG.error("Entity Does not have any path to any category:findLevelFirstPathDiscovered  "+ str_entity);
		return -1;
	}
	public Map<String, HashMap<String, Double>> getHmap_testSet() {
		return hmap_testSet;
	}

	public HeuristicType getHeuristicType() {
		return heuristicType;
	}
}


/*
 * private static final Logger LOG = Logger.getLogger(HeurisitcFunctions.class.getCanonicalName());
	private Map<String, HashMap<String, Double>> hmap_testSet;
	//private HashSet<String> hset_entityList;
	private final HeuristicType heuristicType;
	private final int int_totalEntityCount;
	
	public HeurisitcFunctions(Map<String, HashMap<String, Double>> hmap_testSetDistinctPaths, Global.HeuristicType heuristic,
			int int_totalEntityCount)
	{
		this.hmap_testSet=new LinkedHashMap<>(hmap_testSetDistinctPaths);
		//this.setHset_entityList(set);
		this.int_totalEntityCount= int_totalEntityCount;
		this.heuristicType = heuristic;
	}
	
	public Map<String, HashMap<String, Double>> callHeuristic() 
	{
		Map<String, HashMap<String, Double>> hmap_heuResult = new HashMap<>();
		Map<String, Integer> hmap_tfidfCatDeptVal = new HashMap<>(initializeMapForTFIDF(hmap_testSet));
		
		
		for (Entry<String, HashMap<String, Double>> entry : hmap_testSet.entrySet()) 
		{
			String str_entityNameAndDepth = entry.getKey();
			String str_entityName = str_entityNameAndDepth.substring(0, str_entityNameAndDepth.indexOf(Global.str_depthSeparator));
			String str_depth = str_entityNameAndDepth.substring(
					str_entityNameAndDepth.indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(), str_entityNameAndDepth.length());
			
			if (str_depth.contains("_")) 
			{
				str_depth=str_depth.replace("_", "");
			}
			
			
			Map<String, Double> hmap_Values = new HashMap<>(entry.getValue());
			HashMap<String, Double> lhmap_Results = new HashMap<>();

			for (Entry<String, Double> entry_hmapValues : hmap_Values.entrySet()) 
			{
				String str_catName = entry_hmapValues.getKey();
				Double db_pathCount = entry_hmapValues.getValue();
				Double db_heuValue = 0.0;
				
				db_heuValue = Heuristic_combination4th5th(str_entityName,str_catName,db_pathCount,Integer.parseInt(str_depth),hmap_tfidfCatDeptVal) ;
				//System.out.println(str_entityNameAndDepth + str_entityName + str_depth + db_heuValue );
				
				lhmap_Results.put(str_catName, db_heuValue);
			}
			hmap_heuResult.put(str_entityNameAndDepth, lhmap_Results);
		}
		//Print.printMap(hmap_heuResult);
		return hmap_heuResult;
	}

	public static Map<String, Integer> initializeMapForTFIDF(Map<String, HashMap<String, Double>> hmap_testSet)
	{
		Map<String ,Integer> hmap_resultCatDepVal = new LinkedHashMap<>();
		for (Entry<String, HashMap<String, Double>> entry_EntDeptCatVal : hmap_testSet.entrySet()) 
		{
			String str_entityNameAndDepth = entry_EntDeptCatVal.getKey();
			String str_entityName = str_entityNameAndDepth.substring(0, str_entityNameAndDepth.indexOf(Global.str_depthSeparator));
			String str_depth = str_entityNameAndDepth.substring(
					str_entityNameAndDepth.indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(), str_entityNameAndDepth.length());

			if(str_depth.contains("_"))
				str_depth=str_depth.replace("_", "");
			
			//System.out.println(str_entityNameAndDepth+" " +str_entityName+" "+str_depth);
			HashMap<String, Double> hmap_CatVal = entry_EntDeptCatVal.getValue();
			int depth = Integer.parseInt(str_depth);
			//	LinkedHashMap<String, Double> lhmap_Results = (LinkedHashMap<String, Double>) entry_EntDeptCatVal.getValue();

			for (Entry<String, Double> entry_hmapValues : hmap_CatVal.entrySet()) 
			{
				String str_catName = entry_hmapValues.getKey();

				String str_catDepth = entry_hmapValues.getKey()+Global.str_depthSeparator+str_depth;

				if (hmap_resultCatDepVal.containsKey(str_catDepth)) 
				{
					Integer intVal = hmap_resultCatDepVal.get(str_catDepth)+1;
					hmap_resultCatDepVal.put(str_catDepth, intVal);
				} 
				else 
				{
					hmap_resultCatDepVal.put(str_catDepth, 1);
				}
			}
		}
		return hmap_resultCatDepVal;

	}

	private static double Heuristic_NanHeuristic(double db_Value) {
		return 1.0;
	}

	private static double Heuristic_NumberOfPaths(double db_Value) {
		return db_Value;
	}

	private static double Heuristic_NumberOfPathsAndDepth(double db_Value, int int_depth) {
		return (double) (db_Value / (double) int_depth);
	}

	private double Heuristic_FirstPathsAndDepth(String str_entity,double db_Value, int int_depth) 
	{
		int int_entitystartingDepth= findLevelFirstPathDiscovered(getHmap_testSet(), str_entity);
		if (!(int_entitystartingDepth>=0)) 
		{
			LOG.error("Entity not found hmap_entityStartingCat"+ str_entity);
		}
		if (int_depth<int_entitystartingDepth) 
		{
			return 0;
		}
		return (double) (db_Value / (double) (int_depth-(int_entitystartingDepth-1)));
	}
	private double Heuristic_tfidf(String str_catName,double db_Value, String str_depth,Map<String, Integer> hmap_tfidfCatDeptVal) 
	{
		if (hmap_tfidfCatDeptVal.containsKey(str_catName+Global.str_depthSeparator+str_depth))
		{
			return (double) (db_Value * (double) Math.log10(this.int_totalEntityCount*1.0 /(double) hmap_tfidfCatDeptVal.get(str_catName+Global.str_depthSeparator+str_depth)));
		}
		else
		{
			//System.out.println(str_catName+str_depthSeparator+str_depth);
			System.err.println("ERROR");
			return 0.;
		}
	}
	private double Heuristic_combination4th5th(String str_entity,String str_catName,double db_Value, int int_depth,Map<String, Integer> hmap_tfidfCatDeptVal) 
	{
		
		int int_entitystartingDepth=  findLevelFirstPathDiscovered(getHmap_testSet(), str_entity);;
		if (hmap_tfidfCatDeptVal.containsKey(str_catName+Global.str_depthSeparator+int_depth))
		{
			return (double) ((db_Value / Math.pow(2.0,(double) (int_depth-int_entitystartingDepth))))* (double) Math.log10(this.int_totalEntityCount *1.0 / hmap_tfidfCatDeptVal.get(str_catName+Global.str_depthSeparator+int_depth));
		}																							
		else
		{
			System.out.println(str_catName+Global.str_depthSeparator+int_depth);
			System.out.println("ERROR");
			return 0.;
		}
	}
	private double Heuristic_FirstPathsAndDepthExp(String str_entity,double db_Value, int int_depth) 
	{
		int int_entitystartingDepth=  findLevelFirstPathDiscovered(getHmap_testSet(), str_entity);
		if (!(int_entitystartingDepth>=0)) 
		{
			LOG.error("Entity not found hmap_entityStartingCat"+ str_entity);
		}
		if (int_depth<int_entitystartingDepth) 
		{
			return 0;
		}
		return (double) (db_Value / Math.pow(2.0,(double)(int_depth-(int_entitystartingDepth))));
	}

	public static int findLevelFirstPathDiscovered(Map<String, HashMap<String, Double>> hmap_testSet,String str_entity) {

		for (Integer i = 1; i <= Global.levelOfTheTree; i++) 
		{
			if (hmap_testSet.containsKey(str_entity+Global.str_depthSeparator+i.toString())) 
			{
				return i;
			}
//			HashMap<String, Double> hmap_catAndVal = hmap_testSet.get(str_entity+Global.str_depthSeparator+i.toString());
//			
//			if (!hmap_catAndVal.isEmpty()) 
//			{
//				return i;
//			}
		}
		System.out.println("Entity Does not have any path to any category:findLevelFirstPathDiscovered  "+str_entity);
		LOG.error("Entity Does not have any path to any category:findLevelFirstPathDiscovered  ");
		return -1;
	}
//	public HashSet<String> getHset_entityList() {
//		return hset_entityList;
//	}
//	public void setHset_entityList(HashSet<String> hset_entityList) {
//		this.hset_entityList = hset_entityList;
//	}
	public Map<String, HashMap<String, Double>> getHmap_testSet() {
		return hmap_testSet;
	}

	public HeuristicType getHeuristicType() {
		return heuristicType;
	}
 */

