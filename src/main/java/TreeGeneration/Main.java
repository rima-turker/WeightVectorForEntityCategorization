package TreeGeneration;
//execute 'cd /home/rima/playground/CurrentlyWorking/WeightVectorForEntityCategorization/bin && ./WeightVectorForEntityCategorization /home/rima/playground/CurrentlyWorking/article_categories_clean2016'

import java.util.HashMap;

import TreeGeneration.Global.HeuristicType;
import util.ComparisonFunctions;

public class Main {

	//final static String str_fileName = Global.pathLocal+"article_cats_testEntities";
	//final static String str_fileName = Global.pathLocal+"article_cats_testEntities_CatFiltered_7";	
	//execute 'cd /home/rima/playground/CurrentlyWorking/WeightVectorForEntityCategorization/bin && ./WeightVectorForEntityCategorization /home/rima/playground/CurrentlyWorking/article_categories_clean2016 /home/rima/playground/CurrentlyWorking/CategoryTrees_2016 /home/rima/playground/CurrentlyWorking/MainCategoryFile /home/rima/playground/CurrentlyWorking/article_cats2016_7_DistinctPaths_Formated_sort'
	public static void main(String[] args) throws Exception {
		
		System.out.println(args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
		}
		
		ReadResultsFromFilteredFiles read = new ReadResultsFromFilteredFiles(args[1],args[2]);
		WriteReadFromFile.writeMapToAFile(read.ReadResults_withLevel(read.createCategoryMap(),args[3]),"albert_2016_7_withSubCats");
		
//		HashMap<String, HashMap<String,Double>> mapFirst = new HashMap<>(WriteReadFromFile.readTestSet_tab(Global.pathTestFile_tab));
//		HashMap<String, HashMap<String,Double>> mapSecond = new HashMap<>(WriteReadFromFile.readTestSet_tab(Global.pathLocal+"test_DistinctPaths"));
//		
//		ComparisonFunctions.compareMaps(mapFirst,mapSecond);
		
//		
//		new CreateWeightVector(args[3],0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH).main();
		
	
		
		//Print.printMap(hmap_distinctTest);
//		for (int i = 1; i <= Global.levelOfTheTree; i++) 
//		{
//			if (!hmap_distinctTest.containsKey(str_entName+Global.str_depthSeparator+i)) 
//			{
//				hmap_distinctTest.put(str_entName+Global.str_depthSeparator+i, new HashMap<>());
//			}
//		}
		
		
		
		
		
//		WriteReadFromFile.writeMapToAFile(ReadResultsFromFilteredFiles.readAndCreateMapDistinctPaths(args[3]),"article_cats2016_7_DistinctPaths");
		
		
//		ArrayList<Integer> arrList = new ArrayList<>();
//		arrList.add(1);
//		arrList.add(3);
//		arrList.add(5);
//		
//		for (int i = 0; i < arrList.size(); i++) 
//		{
//			System.out.println("Top "+ arrList.get(i) +" elements");
//			EvaluateTopNElements evaTopN = new EvaluateTopNElements(arrList.get(i), Global.pathTestFile_tab);
//			evaTopN.calculatePrecisionAndRecallForTopN();
//			
//		}
		
		
//		CompareTwoFiles.compareTwoFiles(Global.pathLocal+"PageLinks_EntityCatFiltered_7", Global.pathLocal+"article_Cat_entityFiltered_2016_CatEntFiltered_7");
		
		
		//final String goalSetFile=Global.pathLocal+"GoalSet_Intersection.tsv"; //0.7257379773936717 
		
		//final String goalSetFile=Global.pathLocal+"GoalSet_Blog.tsv";
		//final String goalSetFile=Global.pathLocal+"GoalSet_Uni.tsv"; // 0.7108394111399493
	
		//final String goalSetFile=Global.pathLocal+"GoalSet_Intersection_meIncluded.tsv";
		
		
		//DistinctPaths_PageLinks_2015.tsv
//		EvaluateHeuristicFunctions evaSecond = new EvaluateHeuristicFunctions(goalSetFile,Global.pathTestFile_tab, 0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH);
//		evaSecond.main();
	
//		ComparisonFunctions.compareMaps_(new EvaluateHeuristicFunctions(goalSetFile,Global.pathTestFile_tab, 0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH).getHmap_groundTruthlist(),
//				new EvaluateHeuristicFunctions(goalSetFileMe,Global.pathTestFile_tab, 0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH).getHmap_groundTruthlist());
		
			
//		for (double the = 0.01; the<=0.1; the += 0.01) {
//			 System.out.println("--------------------Threshold-------------------"+
//			the);
//			for (Global.HeuristicType heu : Global.HeuristicType.values()) 
//			{
//			    //System.out.println("heuristic function"+ heu); 
//				EvaluateHeuristicFunctions eva = new EvaluateHeuristicFunctions(goalSetFile, Global.pathTestFile_tab, the, heu);
//				eva.main();
//			}
//		}
//		Map<String, Double> hmap_fmeasure = new HashMap<>();
//		hmap_fmeasure=MapUtil.entriesSortedByValues(EvaluateHeuristicFunctions.hmap_fmeasureAll);
//		int count =0;
//		for (Entry <String, Double>   entry: hmap_fmeasure.entrySet()) 
//		{
//			System.out.println(entry.getKey()+" "+entry.getValue());
//			count++;
//			if (count==50) 
//			{
//				break;
//			}
//		}
		
//		EvaluateHeuristicFunctions eva = new EvaluateHeuristicFunctions(goalSetFile, Global.pathTestFile_tab, 0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH);
//		eva.main();
//		ComparisonFunctions.compareTwoHashSet(MapUtil.getKeySetFromMap_3(eva.getHmap_groundTruth()),
//												MapUtil.getKeySetFromMap(eva.getHmap_testSetDistinctPaths()));
//		ComparisonFunctions.compareMaps(eva.getHmap_filteredResults(), eva.getHmap_filteredResults());
//		ComparisonFunctions.compareMaps(WriteReadFromFile.readTestSet_tab("Comparison"+File.separator+"article_cats_formated_2016.tsv"),
//				WriteReadFromFile.readTestSet_coma("Comparison"+File.separator+"article_cats_formated.csv"));
		
		
		
		
		

		
		
//		System.out.println(args.length);
//		if (args.length == 1) 
//		{
//			System.out.println(args[0]);
//			new CreateWeightVector(args[0], 0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH).main();
//		}
//		else if (args.length == 2) 
//		{
//			System.out.println("Arg size 2");
//			new CreateWeightVector(args[0],args[1], 0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH).main();
//		}
//		else
//		{
//			new CreateWeightVector(Global.pathLocal+"test_Entities",Global.pathLocal+"test_PageLinks", 0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH).main();
//		}
//		
		
	}


/*
  
  
  	ReadGroundTruth gd = new ReadGroundTruth();
	gd.findIntersectionUnion();
		
	1)Create Cat Tree Linux UTF-8
	CreateCategoryTrees.main();
	String str_entityList = Global.path_Local+"WeightVectorEntities";
//		
	2)
	//	ReadResultsFromFilteredFiles read = new ReadResultsFromFilteredFiles();
//		read.createCategoryMap();
//		CompareTwoFilesForCatFiltering.compareTwoHashsetAndFile(Global.pathLocal+"article_Cat_entityFiltered_2016", read.getHset_allCats(),
		
		ReadResultsFromFilteredFiles read = new ReadResultsFromFilteredFiles(args[1],args[2]);
		CompareTwoFilesForCatFiltering.compareHashsetAndFile(args[0], read.getHset_allCats());
 	
 	
 	3)
	
		ReadResultsFromFilteredFiles read = new ReadResultsFromFilteredFiles();
		//WriteReadFromFile.writeMapToAFile(read.ReadResults(read.createCategoryMap(), str_fileName),"article_cats2016_7");
		WriteReadFromFile.writeMapToAFile(read.ReadResults_withLevel(read.createCategoryMap(), Global.pathLocal+"article_Cat_entityFiltered_2016_CatEntFiltered_7"),"article_cats2016_7_withSubCats");

		ReadResultsFromFilteredFiles read = new ReadResultsFromFilteredFiles(args[1],args[2]);
		WriteReadFromFile.writeMapToAFile(read.ReadResults_withLevel(read.createCategoryMap(),args[3]),"article_cats2016_7_withSubCats");
	
	
	4)
	
		WriteReadFromFile.writeMapToAFile(ReadResultsFromFilteredFiles.readAndCreateMapDistinctPaths(Global.pathLocal+"article_cats2016_7_withSubCats"),"article_cats2016_7_DistinctPaths");
		WriteReadFromFile.writeMapToAFile(ReadResultsFromFilteredFiles.readAndCreateMapDistinctPaths(args[3]),"article_cats2016_7_DistinctPaths");
		
	5) sed cmd put into csv format and remove any space 
	
		sed 's/__1={/,1,/' pageLinksDistinctPaths | sed 's/__2={/,2,/' | sed 's/__3={/,3,/' | sed 's/__4={/,4,/' | sed 's/__5={/,5,/' | sed 's/__6={/,6,/' | sed 's/__7={/,7,/' | sed 's/}//'
		WriteReadFromFile.formatMapFile(Global.pathLocal+"article_cats2016_7_DistinctPaths");
	
	
	6) 	
			final String goalSetFile="GoalSet_Intersection.tsv"; //0.7257379773936717 
		//final String goalSetFile="GoalSet_Blog.tsv";
		//final String goalSetFile="GoalSet_Union.tsv";
	
			
			for (double the = 0.03; the<=0.07; the += 0.01) {
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
	
	String str_entityList = Global.path_Local+"WeightVectorEntities";
//		new CreateWeightVector(str_entityList, 0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH).main();
 * new CreateWeightVector(str_entityList, 0.07, HeuristicType.HEURISTIC_COMBINATION4TH5TH).main();
	
	*/
	 
}