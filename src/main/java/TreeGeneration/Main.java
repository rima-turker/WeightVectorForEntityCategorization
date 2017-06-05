package TreeGeneration;

public class Main {

	final static String str_ExampleNo = "3";
	final static String str_fileName = "page_resourcePath_Cleaned_objOnlyCategory";
	//"infoBoxCleaned_OnlyCategoryFiltered_sort"
	
	
	public static void main(String[] args) throws Exception {

		final String goalSetFile="GoalSet_Intersection.tsv"; //0.7257379773936717 
		//final String goalSetFile="GoalSet_Blog.tsv";
		//final String goalSetFile="GoalSet_Union.tsv";
	
		
		for (double the = 0.03; the<=0.07; the += 0.01) {
			 System.out.println("--------------------Threshold-------------------"+
			the);
			for (GlobalVariables.HeuristicType heu : GlobalVariables.HeuristicType.values()) {
			   // System.out.println("heuristic function"+ heu); 
				new EvaluateHeuristicFunctions<Object>(goalSetFile, the, heu).main();
			}
		}	
//		Map<String, Double> hmap_fmeasure = new HashMap<>();
//		hmap_fmeasure=MapUtil.entriesSortedByValues(EvaluateHeuristicFunctions.hmap_fmeasureAll);
//		int count =0;
//		for (Entry <String, Double>   entry: hmap_fmeasure.entrySet()) 
//		{
//			System.out.println(entry.getKey()+" "+entry.getValue());
//			count++;
//			if (count==50) {
//				break;
//			}
//		}


	}
}
	 
