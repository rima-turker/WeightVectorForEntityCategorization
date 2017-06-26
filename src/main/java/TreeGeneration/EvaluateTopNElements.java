package TreeGeneration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import TreeGeneration.Global.HeuristicType;
import util.MapUtil;

public class EvaluateTopNElements 
{
	private int topN;
	//private Map<String, Double> map;
	private String pathMainFile;
	public EvaluateTopNElements(int n, String pathMainFile )
	{
		this.topN=n;
		this.pathMainFile = pathMainFile;
	}
	public void calculatePrecisionAndRecallForTopN()
	{
		EvaluateHeuristicFunctions eva = new EvaluateHeuristicFunctions();
		
		String pathGTBlog = Global.goalSetFile_Blog;
		String pathGTMaj = Global.goalSetFile_Majority;
		String pathGTUni = Global.goalSetFile_Uni;

		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add(pathGTBlog);
//		filePaths.add(pathGTMaj);
//		filePaths.add(pathGTUni);
		try 
		{
			final Map<String, HashMap<String, Double>>  maptestSet = new HashMap<String, HashMap<String,Double>>(WriteReadFromFile
					.readTestSet_tab(pathMainFile));
			HeurisitcFunctions heurisitcFun = new HeurisitcFunctions(maptestSet, HeuristicType.HEURISTIC_COMBINATION4TH5TH,
					maptestSet.size()/Global.levelOfTheTree);
			final Map<String, HashMap<String, Double>> mapEntCatAndVal = new LinkedHashMap<>(heurisitcFun.callHeuristic());
			
			final Map<String, HashMap<String, Double>> hmap_addCatValuesTillDepth = EvaluateHeuristicFunctions.aggregateCategoryValues(
					mapEntCatAndVal);
			//Print.printHashMapFormated(hmap_addCatValuesTillDepth);
			Map<String, HashMap<String, Double>> hmap_addCatValuesTillDepth_sort = new LinkedHashMap<>();
			for(Entry <String, HashMap<String, Double>> entry : hmap_addCatValuesTillDepth.entrySet() )
			{	
				HashMap<String, Double> mapSorted = new LinkedHashMap<>(MapUtil.entriesSortedByValues(hmap_addCatValuesTillDepth.get(entry.getKey())));
				hmap_addCatValuesTillDepth_sort.put(entry.getKey(), mapSorted);
			}
			//Print.printMapFormattedForExell(hmap_addCatValuesTillDepth_sort);
			for (String filePath : filePaths)
			{

				HashMap<String, HashSet<String>> mapGT = new HashMap<>(EvaluateHeuristicFunctions.initializeGroundTruthList(filePath));
				
				//analyseGT(mapGT,filePath);
				
				Map<String, HashMap<String, Double>> mapResult = new HashMap<>();
				//Either aggregated values or heu result hmap_addCatValuesTillDepth or mapEntCatAndVal
				for(Entry <String, HashMap<String, Double>> entry : hmap_addCatValuesTillDepth.entrySet() )
				{	
					//System.out.println(entry.getKey());
					Map <String, Double> mapSorted = new LinkedHashMap<>(MapUtil.entriesSortedByValues(hmap_addCatValuesTillDepth.get(entry.getKey())));
					HashMap<String, Double> mapForTopElements= new HashMap();

					int count =0;

					Iterator<Entry<String, Double>> it = mapSorted.entrySet().iterator();
					while (it.hasNext())
					{
						Entry<String, Double> entryCatAndVal = it.next();
						mapForTopElements.put(entryCatAndVal.getKey(), entryCatAndVal.getValue());
						//System.out.println(entry.getKey()+"\t"+entryCatAndVal.getKey());
						count++;
						if (count==topN) 
						{
							break;
						}
					}
					mapResult.put(entry.getKey(), mapForTopElements);
				}
				System.out.println("PrecisionAndRecallCalculate "+ filePath);
				eva.calculatePreRcallFscore_levelBased(mapResult, mapGT);
			} 
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
	private void analyseGT(HashMap<String, HashSet<String>> mapGT,String fileName)
	{
		System.out.println(fileName);
		int[] arrResult = new int[6];
		
		for (Entry <String, HashSet<String>> entry : mapGT.entrySet()) 
		{
			arrResult[entry.getValue().size()-1]+=1;
			
		}
		
		for (int i = 0; i < arrResult.length; i++)
		{
			System.out.println((i+1)+"\t"+arrResult[i]);
		}
	}
}
