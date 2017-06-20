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
		filePaths.add(pathGTMaj);
		filePaths.add(pathGTUni);
		try 
		{
			final Map<String, HashMap<String, Double>>  maptestSet = new HashMap<String, HashMap<String,Double>>(WriteReadFromFile
					.readTestSet_tab(pathMainFile));
			HeurisitcFunctions heurisitcFun = new HeurisitcFunctions(maptestSet, HeuristicType.HEURISTIC_COMBINATION4TH5TH,
					maptestSet.size()/Global.levelOfTheTree);
			
			final Map<String, HashMap<String, Double>> mapEntCatAndVal = new LinkedHashMap<>(heurisitcFun.callHeuristic());
			
			for (String filePath : filePaths)
			{

				HashMap<String, HashSet<String>> mapGT = new HashMap<>(EvaluateHeuristicFunctions.initializeGroundTruthList(filePath));
				Map<String, HashMap<String, Double>> mapResult = new HashMap<>();

				for(Entry <String, HashMap<String, Double>> entry : mapEntCatAndVal.entrySet() )
				{	
					Map <String, Double> mapSorted = new LinkedHashMap<>(MapUtil.entriesSortedByValues(mapEntCatAndVal.get(entry.getKey())));
					HashMap<String, Double> mapForTopElements= new HashMap();

					int count =0;

					Iterator<Entry<String, Double>> it = mapSorted.entrySet().iterator();
					while (it.hasNext())
					{
						Entry<String, Double> entryCatAndVal = it.next();
						mapForTopElements.put(entryCatAndVal.getKey(), entryCatAndVal.getValue());
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

}
