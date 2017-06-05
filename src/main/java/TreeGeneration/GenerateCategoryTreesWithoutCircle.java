package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import util.ComparisonFunctions;

public class GenerateCategoryTreesWithoutCircle 
{
	
	public static void main(String[] args) throws IOException 
	{

		HashMap<String, HashSet<String>> hmap_categoryTrees = new HashMap<>(createCategoryTree());
		//writeMatchingLinesToFileFromCatTree(hmap_categoryTrees,"FilesCleaned_OnlyCategory.zip","EntitiesHasPathToCats");
	}
	/*
	 * In this function 35 category trees are generated no circle in separate trees
	 * but may be from tree to tree there could be a circle
	 */
	private static HashMap<String, HashSet<String>> createCategoryTree() 
	{
		long startTime = System.nanoTime();

		HashMap<String, HashSet<String>> hmap_categoryMap = new HashMap<>();
		try 
		{
			BufferedReader br_MainCategory = new BufferedReader(new FileReader(GlobalVariables.path_MainCategories));
			String line_mainCategory = null;
			String line=null;
			while ((line_mainCategory = br_MainCategory.readLine()) != null) 
			{
				System.out.println(line_mainCategory);
				String str_mainCategoryName = line_mainCategory.replace(">", "").toLowerCase();
				HashSet<String> hset_allCatsInTree = new HashSet<>();

				for (int i = 1; i <= GlobalVariables.levelOfTheTree ; i++) 
				{
					HashSet<String> hset_tempCats = new HashSet<>();
					String str_depth=Integer.toString(i);
					String str_catAndLevel = str_mainCategoryName+GlobalVariables.str_depthSeparator+str_depth;
					if (i==1) 
					{
						hset_tempCats.add(str_mainCategoryName);
						hset_allCatsInTree.add(str_mainCategoryName);
					}
					else
					{
						int int_childDepth = i-1;

						HashSet<String> hsetParents = new HashSet<>(hmap_categoryMap.get(str_mainCategoryName+GlobalVariables.str_depthSeparator+Integer.toString(int_childDepth)));
						BufferedReader br_MainFile = new BufferedReader(new FileReader(GlobalVariables.path_SkosFile));

						while ((line = br_MainFile.readLine()) != null)
						{
							String str_parent = line.split(" ")[1].replace(">", "").toLowerCase();
							String str_child =line.split(" ")[0].replace(">", "").toLowerCase();
							if (hsetParents.contains(str_parent)&& !hset_allCatsInTree.contains(str_child))
							{
								hset_tempCats.add(str_child);
								hset_allCatsInTree.add(str_child);

							}
						}
						br_MainFile.close();
					}
					hmap_categoryMap.put(str_catAndLevel, hset_tempCats);
					System.out.println(" "+Integer.toString(i)+" child size "+hset_tempCats.size());
				}
			}
			System.out.println();
			br_MainCategory.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long stopTime = System.nanoTime();
		System.out.println("Time" + (double)(stopTime - startTime) / 1000000000.0 +"seconds");

		return hmap_categoryMap;
	}

	public static void writeMatchingLinesToFileFromCatTree(HashMap<String, HashSet<String>> hmap_categoryMap ,String str_bigFile, 
			String str_resultFile)
	{
		File log = new File(System.getProperty("user.dir")+ str_resultFile);
		try 
		{
			if (log.exists()) 
			{
				log.delete();
			}
			log.createNewFile();

			BufferedWriter bf_Writer = new BufferedWriter(new FileWriter(log,true));
			
			if (ComparisonFunctions.isZipFile(new File(System.getProperty("user.dir")+str_bigFile))) 
			{
				Enumeration<? extends ZipEntry> entries = new ZipFile(GlobalVariables.path_Local+str_bigFile).entries();
 				while (entries.hasMoreElements()) 
				{
					ZipEntry ze = (ZipEntry) entries.nextElement();
					//ze = (ZipEntry) entries.nextElement();
					long size = ze.getSize();
					if (size > 0) 
					{
						System.out.println("Reading "+ ze.getName()+"size:"+ze.getSize());

						BufferedReader br_bigFile = new BufferedReader(new InputStreamReader(new ZipFile(GlobalVariables.path_Local+str_bigFile).getInputStream(ze)));
						String line = null;

						bf_Writer.write(ze.getName());
						while ((line = br_bigFile.readLine()) != null) 
						{
							String str_cat = line.toLowerCase().split(" ")[1].replace(">", ""); 
							
							for(Entry<String, HashSet<String>> entry_catMap : hmap_categoryMap.entrySet())
							{
								HashSet<String> hset_cats = new HashSet<>(entry_catMap.getValue());
								if (hset_cats.contains(str_cat)) 
								{
									bf_Writer.write(line.toLowerCase()+","+entry_catMap.getKey());
									bf_Writer.newLine();
								}
							}
						}
						bf_Writer.write("End of"+ze.getName()+" file.");
						br_bigFile.close();
					}
				}
			}
//			else
//			{
//				BufferedReader br_bigFile = new BufferedReader(new FileReader(GlobalVariables.path_Local+str_bigFile));
//				String line = null;
//				while ((line = br_bigFile.readLine()) != null) 
//				{
//					if (hset_smallFile.contains(line.toLowerCase().split(" ")[0])) 
//					{
//						bf_Writer.write(line.toLowerCase());
//						bf_Writer.newLine();
//					}
//
//				}
//				br_bigFile.close();
//			}

			bf_Writer.newLine();
			bf_Writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

	}
}
