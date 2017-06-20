package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class CombinePathsFromOtherFiles {
	static String path = System.getProperty("user.dir") + File.separator;

	public static void ReadResultFromCVSFile(String fileName) 
	{
		String str_format = "=SPLIT(\"";
		HashMap<String, HashMap<String, Double>> hmap_pathsFromOthers= ReadResults.ReadResultFromDifferentFileEntAndCat("EntityAndCatFromOtherFiles"+File.separator+
														"InfoMappingPageCombanied.txt");
		
		Map<String, String> hmap_entityAndPaths = new LinkedHashMap<String, String>();
		
		boolean bl_newentity = true;
		try (BufferedReader br = new BufferedReader(new FileReader(path+fileName));)
		{
			String line = null;


			while ((line = br.readLine()) != null) 
			{
				String[] str_split = line.split("\t");
				
				if (str_split.length>7) 
				{
					
					
					bl_newentity = false;
					
					
					
				}
					
				if (hmap_pathsFromOthers.containsKey(str_split[0])) 
				{
					HashMap<String, Double> hmap_catAndVal = hmap_pathsFromOthers.get(str_split[0]);
					
					Map.Entry<String,Double> entry= hmap_catAndVal.entrySet().iterator().next();
					String key= entry.getKey();
					Double value=entry.getValue();
					
					String str_catAndPath= entry.getKey()+":"+entry.getValue();
					for (int i = 0; i < str_split.length-1; i++) 
					{
						str_format+=str_split[i]+ " ," ;
					}
					str_format+=str_catAndPath+ " ," ;
				}
					
				
				for (int i = 0; i < str_split.length; i++) 
				{
					if (str_split.length>7) 
					{
						
					}
					
					str_format+=str_split[i]+ " ," ;
				}
				str_format += "\",\",\")";
				if (str_split.length>7) 
				{
					if (hmap_pathsFromOthers.containsKey(str_split[0])) 
					{
						//str_format
					}
					hmap_entityAndPaths.put(str_split[0], str_format);
					
				}
				//System.out.println(str_format);
				str_format = "=SPLIT(\"";
				
			}
			for (Entry<String, String> entry_entityAndPaths : hmap_entityAndPaths.entrySet())
			{
				String str_entity = entry_entityAndPaths.getKey();
				System.out.println(str_entity+" "+entry_entityAndPaths.getValue());
			}
		}	
		catch (Exception e) 
		{
				// TODO: handle exception
		}

		
	}
}