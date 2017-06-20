package TreeGeneration;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ReadResultsFromFilteredFiles 
{
	private HashSet<String> hset_allCats;

	private String folderCategory;
	private String pathMainCatFile;
	public ReadResultsFromFilteredFiles(String pathCatFolder, String pathMainCatFile)
	{
		hset_allCats = new HashSet<>();
		folderCategory=pathCatFolder;
		this.pathMainCatFile=pathMainCatFile;
	}
	public  HashMap<String, HashSet<String>> createCategoryMap() 
	{
		
		System.out.println("Reading Categories");
		final File categoryFolder = new File(folderCategory);

		HashMap<String, HashSet<String>> hmap_categoryMap = new HashMap<>();

		try {

			BufferedReader br_MainCategory = new BufferedReader(new FileReader(this.pathMainCatFile));
			String line_mainCategory = null;
			while ((line_mainCategory = br_MainCategory.readLine()) != null) 
			{
				String str_mainCat = line_mainCategory.replace(">", "").toLowerCase();
				//System.out.println(str_mainCat);

				for(int i=1 ; i<=Global.levelOfTheTree; i++)
				{
					String str_fileName = line_mainCategory.replace(">", "").toLowerCase()+"__"
							+i;
					
					//for UTF-8 encodedfiles
//					String str_fileName = line_mainCategory.replace(">", "").toLowerCase()+Global.str_depthSeparator
//							+i;
					final String file = folderCategory+File.separator+str_fileName;	

					BufferedReader br = new BufferedReader(new FileReader(file));
					String sCurrentLine;
					final HashSet<String> content = new HashSet<>();
					while ((sCurrentLine = br.readLine()) != null) 
					{
						content.add(sCurrentLine);	
						getHset_allCats().add(sCurrentLine);
					}

					hmap_categoryMap.put(str_mainCat+Global.str_depthSeparator
							+i, new HashSet<>(content));
					//System.out.println(" "+Integer.toString(i)+" child size "+content.size());

				}
			}
			System.out.println("Size of the all categories "+getHset_allCats().size());
			br_MainCategory.close();

		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return hmap_categoryMap;
	}


	public HashMap<String, HashMap<String, Integer>> ReadResults(HashMap<String, HashSet<String>> hmap_categoryMap,String str_fileName) 
	{
		long startTime = System.nanoTime();
		HashMap<String, HashMap<String, Integer>> hmap_result = new HashMap<>(); 
		
		try 
		{
			BufferedReader br_MainFile = new BufferedReader(new FileReader(str_fileName));
			String line_mainCategory = null;
			int count = 0;
			while ((line_mainCategory = br_MainFile.readLine()) != null) 
			{
				String str_entName= line_mainCategory.split(" ")[0].toLowerCase();
				String str_entCat= line_mainCategory.split(" ")[1].toLowerCase().replace(">", "");
				Map<Integer,String> hmap_mainCat= getCategoryName(str_entCat, hmap_categoryMap);
				
				if (hmap_mainCat.isEmpty()) 
				{
					System.err.println("Main category could not found."+str_entCat);
					
				}
				
				HashMap<String, Integer>  hmap_catAndVal = new HashMap<>();
				
				if (!hmap_result.containsKey(str_entName)) 
				{
					for(Entry<Integer,String>  entry_depthAndCat : hmap_mainCat.entrySet())
					{
						hmap_catAndVal.put(entry_depthAndCat.getValue(), 1);
					}
				}
				else
				{
					hmap_catAndVal = new HashMap<>(hmap_result.get(str_entName));
					for(Entry<Integer,String>  entry_depthAndCat : hmap_mainCat.entrySet())
					{
						//int int_depth = entry_depthAndCat.getKey();
						String str_cat = entry_depthAndCat.getValue();
						
						if (hmap_catAndVal.containsKey(str_cat)) 
						{
							int int_old =hmap_catAndVal.get(str_cat); 
							hmap_catAndVal.put(str_cat, int_old+1);
						}
						else
						{
							hmap_catAndVal.put(str_cat, 1);
						}
					}
				}
				
				hmap_result.put(str_entName, hmap_catAndVal);
				
			}
			System.out.println("size of the map"+hmap_result.size() );
			br_MainFile.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long stopTime = System.nanoTime();
		//System.out.println("Time" + (stopTime - startTime)/1000000000);
		return hmap_result;
	}
	
	public HashMap<String, HashMap<Integer,HashMap<String, Integer>>> ReadResults_withLevel(HashMap<String, HashSet<String>> hmap_categoryMap,String str_fileName) 
	{
		long startTime = System.nanoTime();
		HashMap<String, HashMap<Integer,HashMap<String, Integer>>> hmap_result = new HashMap<>(); 
		
		/*
		 * EntNameAndDepth, Cat , Val
		 */
		try 
		{
			BufferedReader br_MainFile = new BufferedReader(new FileReader(str_fileName));
			String line_mainCategory = null;
			int count = 0;
			while ((line_mainCategory = br_MainFile.readLine()) != null) 
			{
				//count++;
				String str_entName= line_mainCategory.split(" ")[0].toLowerCase();
				String str_entCat= line_mainCategory.split(" ")[1].toLowerCase().replace(">", "");
				Map<Integer,String> hmap_mainCat= getCategoryName(str_entCat, hmap_categoryMap);
				
				if (hmap_mainCat.isEmpty()) 
				{
					
					System.err.println("Main category could not found "+str_entCat);
					
				}
				
				HashMap<Integer, HashMap<String, Integer>>   hmap_DepthCatAndVal = new HashMap<>();
				
				if (!hmap_result.containsKey(str_entName)) 
				{
					
					for(Entry<Integer,String>  entry_depthAndCat : hmap_mainCat.entrySet())
					{
						HashMap<String , Integer> hmap_catAndVal = new HashMap<>();
						hmap_catAndVal.put(entry_depthAndCat.getValue(), 1);
						hmap_DepthCatAndVal.put(entry_depthAndCat.getKey(), hmap_catAndVal);
					}
				}
				else
				{
					hmap_DepthCatAndVal = new HashMap<>(hmap_result.get(str_entName));
					for(Entry<Integer,String>  entry_depthAndCat : hmap_mainCat.entrySet())
					{
						int int_depth = entry_depthAndCat.getKey();
						String str_cat = entry_depthAndCat.getValue();
						
						if (hmap_DepthCatAndVal.containsKey(int_depth)) 
						{
							HashMap<String, Integer> hmap_CatAndVal = new HashMap<>(hmap_DepthCatAndVal.get(int_depth));
							if (hmap_CatAndVal.containsKey(str_cat)) 
							{
								int int_old =hmap_CatAndVal.get(str_cat); 
								hmap_CatAndVal.put(str_cat, int_old+1);
							}
							else
							{
								hmap_CatAndVal.put(str_cat, 1);
							}
							
							hmap_DepthCatAndVal.put(int_depth, hmap_CatAndVal);
						}
						else
						{
							HashMap<String, Integer> hmap_temp = new HashMap<>();
							hmap_temp.put(str_cat, 1);
							hmap_DepthCatAndVal.put(int_depth, hmap_temp);
							
						}
					}
				}
				hmap_result.put(str_entName, hmap_DepthCatAndVal);
			}
			System.out.println("size of the map"+hmap_result.size() );
			br_MainFile.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long stopTime = System.nanoTime();
		System.out.println("Time" + (stopTime - startTime)/1000000000);
		return hmap_result;
	}
	
	/*
	 * //carl_hagenbeck={3={zoology=2, history=1}, 4={biology=1}, 5={biology=2, philosophy=1, arts=2, zoology=1}, 6={botany=2, archaeology=1, biology=2}, 7={archaeology=4, history=1, physics=1}}
	 * 
	 */
	
	public static  Map<String, HashMap<String, Double>> readAndCreateMapDistinctPaths(String fileName) 
	{
		Map<String, HashMap<String, Double>> hmap_distinctTest = new HashMap<String, HashMap<String,Double>>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(fileName));) 
		{
			String line = null;
			//carl_hagenbeck={3={zoology=2, history=1}, 4={biology=1}, 5={biology=2, philosophy=1, arts=2, zoology=1}, 6={botany=2, archaeology=1, biology=2}, 7={archaeology=4, history=1, physics=1}}
			//007_racing>={4={electronic_games=7}, 5={transport=1, arts=1, electronic_games=2}, 6={literature=1, communication=4, arts=1}, 7={psychology=1, communication=1, philosophy=1, arts=6, physics=1}}
			
			while ((line = br.readLine()) != null) 
			{
				line = line.toLowerCase().replace(" ", "").replace(">", "");
				//System.out.println(line);
				String str_entName = line.substring(0,line.indexOf("="));
				String str_DepthCats = line.substring(line.indexOf("={")+("={").length(),line.length()).replace(" ", "");
				
				String[] str_split = str_DepthCats.split("},");
				
				
				for (int i = 0; i < str_split.length; i++) 
				{
					String str_depth = str_split[i].substring(0, 1);
				//	System.out.println("depth  "+str_depth);
					HashMap<String, Double> hmap_CatAndVal = new HashMap<>();
					String[] catAndVal = str_split[i].substring(1, str_split[i].length()).replace("={","").replace("}","").replaceAll(" ", "").split(",");
				//	System.out.println("str_split[i]   "+str_split[i]);
					for (int j = 0; j < catAndVal.length; j++) 
					{
						hmap_CatAndVal.put(catAndVal[j].substring(0, catAndVal[j].indexOf("=")), Double.valueOf(catAndVal[j].split("=")[1]));
					}
					hmap_distinctTest.put(str_entName+Global.str_depthSeparator+str_depth, hmap_CatAndVal);
				}
				//Print.printMap(hmap_distinctTest);
				for (int i = 1; i <= Global.levelOfTheTree; i++) 
				{
					if (!hmap_distinctTest.containsKey(str_entName+Global.str_depthSeparator+i)) 
					{
						hmap_distinctTest.put(str_entName+Global.str_depthSeparator+i, new HashMap<>());
					}
				}
				
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			
		}
		return hmap_distinctTest;
	}
	
	
	private  Map<Integer,String> getCategoryName(String categoryName,HashMap<String, HashSet<String>> hmap_categoryMap) 
	{
		final Map<Integer,String> mainCategoryNames = new HashMap<>();
		
		for(Entry<String, HashSet<String>>  entry_CatAndSubs : hmap_categoryMap.entrySet())
		{
			String str_Cat = entry_CatAndSubs.getKey().substring(0,entry_CatAndSubs.getKey().indexOf(Global.str_depthSeparator));
			String str_depth = entry_CatAndSubs.getKey().substring(entry_CatAndSubs.getKey().indexOf(Global.str_depthSeparator)+Global.str_depthSeparator.length(),entry_CatAndSubs.getKey().length());
			if(entry_CatAndSubs.getValue().contains(categoryName)) 
			{
				mainCategoryNames.put(Integer.parseInt(str_depth),str_Cat);
			}
				
		}
		return mainCategoryNames;
	}
	public static void writeCategoryTreeToAFile(Map<String, HashSet <String>> hmap, String str_folderName) {


		File folder =(new File(Global.pathLocal+str_folderName));
		folder.mkdir();

		try {

			for (Entry<String, HashSet <String>> entry: hmap.entrySet()) 
			{
				String str_entName = entry.getKey();
				File file = new File(Global.pathLocal+str_folderName+ str_entName);
				file.createNewFile();
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));

				HashSet<String> hset_subCats = new HashSet<>(entry.getValue());
				for(String str_subCat: hset_subCats)
				{
					bufferedWriter.write(str_subCat);
					bufferedWriter.newLine();
				}
				bufferedWriter.close();
				System.out.println("Finished Writing to a file: "+file.getName()+" "+file.getAbsolutePath());

			}
		}
		catch (Exception e) 
		{

		}

	}
	
	public HashSet<String> getHset_allCats() {
		return hset_allCats;
	}

}