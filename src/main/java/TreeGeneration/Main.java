package TreeGeneration;

import TreeGeneration.Global.HeuristicType;

public class Main {

	final static String str_ExampleNo = "3";
	final static String str_fileName = "page_resourcePath_Cleaned_objOnlyCategory";
	//"infoBoxCleaned_OnlyCategoryFiltered_sort"
	
	
	public static void main(String[] args) throws Exception {

		final String goalSetFile="GoalSet_Intersection.tsv"; //0.7257379773936717 
		//final String goalSetFile="GoalSet_Blog.tsv";
		//final String goalSetFile="GoalSet_Union.tsv";
		
		String str_entityList = Global.path_Local+"WeightVectorEntities";
		new CreateWeightVector(str_entityList, 0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH).main();
		
//		for (double the = 0.03; the<=0.07; the += 0.01) {
//			 System.out.println("--------------------Threshold-------------------"+
//			the);
//			for (Global.HeuristicType heu : Global.HeuristicType.values()) {
//			   // System.out.println("heuristic function"+ heu); 
//				new EvaluateHeuristicFunctions<Object>(goalSetFile, the, heu).main();
//			}
//		}	
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

/*
	1)Create Cat Tree Linux UTF-8
	CreateCategoryTrees.main();

	2)
	ReadResultsFromFilteredFiles read = new ReadResultsFromFilteredFiles();
	read.createCategoryMap();
	CompareTwoFilesForCatFiltering.compareHashsetAndFile(str_fileName, read.getHset_allCats());
	remove "category:" and ">" , sort remove dublicates

 	3)
	
	ReadResultsFromFilteredFiles read = new ReadResultsFromFilteredFiles();
	read.ReadResults(read.createCategoryMap(), str_fileReadResult_3);
	WriteReadFromFile.writeMapToAFile(read.ReadResults(read.createCategoryMap(), str_fileReadResult_3),"pageLinksWeight_7");
	WriteReadFromFile.writeMapToAFile(read.ReadResults_withLevel(read.createCategoryMap(), str_fileReadResult_3),"pageLinksWeight_7_withSubCatNumbers");

	4)
	
		WriteReadFromFile.writeMapToAFile(ReadResultsFromFilteredFiles.readAndCreateMapDistinctPaths("pageLinksWeight_7_withSubCatNumbers"),"pageLinksDistinctPaths");
	
	5) sed cmd put into csv format and remove any space 
	
		sed 's/__1={/,1,/' pageLinksDistinctPaths | sed 's/__2={/,2,/' | sed 's/__3={/,3,/' | sed 's/__4={/,4,/' | sed 's/__5={/,5,/' | sed 's/__6={/,6,/' | sed 's/__7={/,7,/' | sed 's/}//'
	
	6) 	for (double the = 0.03; the<=0.07; the += 0.01) {
			 System.out.println("--------------------Threshold-------------------"+
			the);
			for (Global.HeuristicType heu : Global.HeuristicType.values()) {
			   // System.out.println("heuristic function"+ heu); 
				new EvaluateHeuristicFunctions<Object>(goalSetFile, the, heu).main();
			}
		}	
		Map<String, Double> hmap_fmeasure = new HashMap<>();
		hmap_fmeasure=MapUtil.entriesSortedByValues(EvaluateHeuristicFunctions.hmap_fmeasureAll);
		int count =0;
		for (Entry <String, Double>   entry: hmap_fmeasure.entrySet()) 
		{
			System.out.println(entry.getKey()+" "+entry.getValue());
			count++;
			if (count==50) {
				break;
			}
		}
	
	
	*/
	 
