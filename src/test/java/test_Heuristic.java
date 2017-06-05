


import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import TreeGeneration.Global;
import TreeGeneration.Global.HeuristicType;
import TreeGeneration.HeurisitcFunctions;
import TreeGeneration.Print;
import TreeGeneration.WriteReadFromFile;


public class test_Heuristic {

	@Test
	public void test() 
	{
		Map<String, HashMap<String, Double>> hmap_testSet = new HashMap<String, HashMap<String,Double>>();
		hmap_testSet=WriteReadFromFile.readTestSet("test_TestSetDistinctpaths.csv");
		Print.printMap(hmap_testSet);
		
		int int_startingLevelForAnne = 4;
		int int_startingLevelForVlad = 3;
		
		for (Global.HeuristicType heu : Global.HeuristicType.values()) {
			HeurisitcFunctions obj_heuristic = new HeurisitcFunctions(hmap_testSet, heu, hmap_testSet.size()/Global.levelOfTheTree);
			Map<String, HashMap<String, Double>> hmap_heuResult = obj_heuristic.callHeuristic();
			
			if (heu.equals(HeuristicType.HEURISTIC_FIRSTFINDFIRSTDEPTH)) 
			{
				Map<String, HashMap<String, Double>> hmap_expectedValue = new HashMap<String, HashMap<String,Double>>();
				
				for (Entry<String, HashMap<String, Double>> entry:hmap_testSet.entrySet()) 
				{
					HashMap<String, Double> hmap_catAndVal = new HashMap<String, Double>(entry.getValue());
					Integer int_depth = Integer.parseInt(entry.getKey().substring(
							entry.getKey().indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(), entry.getKey().length()));
					
					HashMap<String, Double> hmap_resultCatAndVal = new HashMap<String, Double>();
					for (Entry<String, Double> entry_catAndVal:hmap_catAndVal.entrySet()) 
					{
						if (entry.getKey().contains("anne_claude_de_caylus")) 
						{
							hmap_resultCatAndVal.put(entry_catAndVal.getKey(), ((double)entry_catAndVal.getValue()/(int_depth+1-int_startingLevelForAnne)));
							
						}
						else if (entry.getKey().contains("vladimir_shukhov")) 
						{
							hmap_resultCatAndVal.put(entry_catAndVal.getKey(), ((double)entry_catAndVal.getValue()/(int_depth+1-int_startingLevelForVlad)));
							
						}
					}
					
					hmap_expectedValue.put(entry.getKey(), hmap_resultCatAndVal);
				}
				assertEquals(hmap_heuResult,hmap_expectedValue);
			}
			else if (heu.equals(HeuristicType.HEURISTIC_FIRSTFINDFIRSTDEPTHEXPONENTIAL)) 
			{
				Map<String, HashMap<String, Double>> hmap_expectedValue = new HashMap<String, HashMap<String,Double>>();
				
				for (Entry<String, HashMap<String, Double>> entry:hmap_testSet.entrySet()) 
				{
					HashMap<String, Double> hmap_catAndVal = new HashMap<String, Double>(entry.getValue());
					Integer int_depth = Integer.parseInt(entry.getKey().substring(
							entry.getKey().indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(), entry.getKey().length()));
					
					HashMap<String, Double> hmap_resultCatAndVal = new HashMap<String, Double>();
					for (Entry<String, Double> entry_catAndVal:hmap_catAndVal.entrySet()) 
					{
						if (entry.getKey().contains("anne_claude_de_caylus")) 
						{
							hmap_resultCatAndVal.put(entry_catAndVal.getKey(), ((double)entry_catAndVal.getValue()/Math.pow(2.0, int_depth-int_startingLevelForAnne)));
							
						}
						else if (entry.getKey().contains("vladimir_shukhov")) 
						{
							hmap_resultCatAndVal.put(entry_catAndVal.getKey(), ((double)entry_catAndVal.getValue()/Math.pow(2.0, int_depth-int_startingLevelForVlad)));
							
						}
					}
					
					hmap_expectedValue.put(entry.getKey(), hmap_resultCatAndVal);
				}
				//Print.printMap(hmap_heuResult);
				assertEquals(hmap_heuResult,hmap_expectedValue);
			}
			else if (heu.equals(HeuristicType.HEURISTIC_TFIDF)) 
			{
				Map<String, HashMap<String, Double>> hmap_expectedValue = new HashMap<String, HashMap<String,Double>>();
				HashMap<String , Double> hmap_resultCatAndVal = new HashMap<>();
				hmap_resultCatAndVal.put("politics", 0.);
				hmap_resultCatAndVal.put("philosophy", 0.);
				hmap_resultCatAndVal.put("psychology", 0.);
				hmap_expectedValue.put("anne_claude_de_caylus__7", hmap_resultCatAndVal);
				
				hmap_resultCatAndVal = new HashMap<>();
				
				hmap_resultCatAndVal.put("history", (0.3010299956639812)*1.);
				hmap_expectedValue.put("anne_claude_de_caylus__5", hmap_resultCatAndVal);
				
				hmap_resultCatAndVal = new HashMap<>();
				
				hmap_resultCatAndVal.put("inventions", (0.3010299956639812)*1.);
				hmap_resultCatAndVal.put("engineering", (0.3010299956639812)*3.);
				hmap_resultCatAndVal.put("photography", (0.3010299956639812)*2.);
				hmap_resultCatAndVal.put("architecture", (0.3010299956639812)*7.);
				
				hmap_expectedValue.put("vladimir_shukhov__4", hmap_resultCatAndVal);
				
				assertEquals(hmap_expectedValue.get("anne_claude_de_caylus__7"), hmap_heuResult.get("anne_claude_de_caylus__7"));
				assertEquals(hmap_expectedValue.get("vladimir_shukhov__4"), hmap_heuResult.get("vladimir_shukhov__4"));
			}
			
			else if (heu.equals(HeuristicType.HEURISTIC_COMBINATION4TH5TH)) 
			{
				Map<String, HashMap<String, Double>> hmap_expectedValue = new HashMap<String, HashMap<String,Double>>();
				HashMap<String , Double> hmap_resultCatAndVal = new HashMap<>();
				hmap_resultCatAndVal.put("politics", 0.);
				hmap_resultCatAndVal.put("philosophy", 0.);
				hmap_resultCatAndVal.put("psychology", 0.);
				hmap_expectedValue.put("anne_claude_de_caylus__7", hmap_resultCatAndVal);
				
				hmap_resultCatAndVal = new HashMap<>();
				
				hmap_resultCatAndVal.put("history", (0.3010299956639812)*0.5);
				hmap_expectedValue.put("anne_claude_de_caylus__5", hmap_resultCatAndVal);
				
				hmap_resultCatAndVal = new HashMap<>();
				
				hmap_resultCatAndVal.put("inventions", (0.3010299956639812)*0.5);
				hmap_resultCatAndVal.put("engineering", (0.3010299956639812)*1.5);
				hmap_resultCatAndVal.put("photography", (0.3010299956639812)*1.);
				hmap_resultCatAndVal.put("architecture", (0.3010299956639812)*3.5);
				
				hmap_expectedValue.put("vladimir_shukhov__4", hmap_resultCatAndVal);
				
				assertEquals(hmap_expectedValue.get("anne_claude_de_caylus__7"), hmap_heuResult.get("anne_claude_de_caylus__7"));
				assertEquals(hmap_expectedValue.get("vladimir_shukhov__4"), hmap_heuResult.get("vladimir_shukhov__4"));
				assertEquals(hmap_expectedValue.get("anne_claude_de_caylus__5"), hmap_heuResult.get("anne_claude_de_caylus__5"));
			}
		}
	}

}
