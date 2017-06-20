package TreeGeneration;

import java.io.File;

public class Global 
{
	public final static  String pathLocal = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"+ File.separator +"resources"+ File.separator +"TreeGeneration"+ File.separator ;
	public final static  String pathZip= pathLocal+"articleAndSkosBroader_2016.zip";
	public final static  String pathLocal_test = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"+ File.separator +"resources"+ File.separator ;
	public static final int levelOfTheTree=7;
	public static final String path_SkosFile= System.getProperty("user.dir")+ File.separator+ "skos_broader_CatCleaned_sort.txt";
	public static final String strPathMainCat= pathLocal +"MainCategoryFile";
	public static final String str_depthSeparator = "\t";
	final static String pathTestFile_tab =pathLocal+"article_cats_formated_2016.tsv"; //"articleDistinctPaths_Formated";
//	final static String pathTestFile_tab =pathLocal+"DistinctPaths_PageLinks_2015.tsv";
//	
//	final static String pathTestFile_coma =pathLocal+ "DistinctPaths_new.csv";//"article_cats_formated.csv";
	final static String goalSetFileMe=Global.pathLocal+"GoalSet_Intersection_meIncluded.tsv";
	//final static String pathFileToCatFilter = pathLocal+"article_categories_testEntities";
	public static final String strFileSkos=  "skos_broader_CatCleaned_sort.txt";
	final  static String str_CatFolder= pathLocal+"CategoryTrees_2016";
	final static String str_entitiesWeightVec = "WeightVectorEntities";
	final static String strfileEntBlogCat = pathLocal + "GoalSet_Blog.tsv";
	
	final static String fileCatFiltered = pathLocal + "article_categories_testEntities_CatFiltered_7";
	
	//final String goalSetFile=Global.pathLocal+"GoalSet_Intersection.tsv"; //0.7257379773936717 
	
	final static String goalSetFile_Blog=Global.pathLocal+"GoalSet_Blog.tsv";
	final static String goalSetFile_Majority=Global.pathLocal+"GoalSet_Majority.tsv";
	final static String goalSetFile_Uni=Global.pathLocal+"GoalSet_Uni.tsv";
	
	
	//public static final String str_testFileName = "test_TestSetDistinctPaths.csv";
	public enum  HeuristicType 
	{
		//HEURISTIC_NO,
		HEURISTIC_NUMBEROFPATHS, 
		HEURISTIC_NUMBEROFPATHSANDDEPTH,
		HEURISTIC_FIRSTFINDFIRSTDEPTH,
		HEURISTIC_FIRSTFINDFIRSTDEPTHEXPONENTIAL,
		HEURISTIC_TFIDF,
		HEURISTIC_COMBINATION4TH5TH,;
	}
}
