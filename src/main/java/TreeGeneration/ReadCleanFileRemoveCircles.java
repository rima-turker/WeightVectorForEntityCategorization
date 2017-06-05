package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class ReadCleanFileRemoveCircles 
{
	
	private static String str_resultFile = "ResultsWithoutCircle";
	
	public static  void main() throws IOException 
	{
		//removeCirclesFromResultFiles();
//		String str_entityName = "george_ellery_hale";
//		findPathsForTheEntity(str_entityName);
	}
	
	
	public static void removeCirclesFromResultFiles()
	{
		try {
			
			File folder = new File(Global.path_Local+File.separator+"ResultFilesBasedOnLevel");
			File[] listOfFiles = folder.listFiles();
			HashSet<String> hset_allConnections = new HashSet<>();
			LinkedList<String> llist_results = new LinkedList<>();
			for (File file : listOfFiles) 
			{
			    if (file.isFile()) {
			        System.out.println(file.getName());
			        BufferedReader br_MainFile = new BufferedReader(new FileReader(file.getPath()));
					String line ;
					
					while ((line = br_MainFile.readLine()) != null)
					{
						line = line.toLowerCase();
						//leonardo_da_vinci,mathematics_and_culture,2,mathematics
						String[] str_lineSplit = line.split(",");
						if (!hset_allConnections.contains(str_lineSplit[0]+","+str_lineSplit[1]+","+str_lineSplit[3])) 
						{
							llist_results.add(line);
							hset_allConnections.add(str_lineSplit[0]+","+str_lineSplit[1]+","+str_lineSplit[3]);
							
						}
					}
					br_MainFile.close();
			    }
			}
			writeListToFile(llist_results,str_resultFile);
		}		
		
		catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Map<String, HashMap<String, Double>> testSetForEntities()
	{
		Map<String,HashMap<String, Double> > hashMap_result = new LinkedHashMap<>();
		
		try {
			BufferedReader br_Entity = new BufferedReader(new FileReader(Global.path_Local+"EntityList.txt"));
			String str_entity ;
			int count =0;
			while ((str_entity = br_Entity.readLine()) != null)
			{
				count++;
				String str_entityName=str_entity.toLowerCase();
				
				BufferedReader br_MainFile = new BufferedReader(new FileReader(Global.path_Local+str_resultFile));
				String line ;
				while ((line = br_MainFile.readLine()) != null)
				{
					line = line.toLowerCase();
					HashMap <String, Double>  hashMap_catAndVal= new HashMap<>() ;
					count = StringUtils.countMatches(line, ",");
					if (count>3) 
					{
						System.out.println(line);
					}
					if (line.contains(str_entityName)) 
					{
						//System.out.println(line);
						String[] str_lineSplit = line.split(",");
						String str_entityAndDepth = str_lineSplit[0]+Global.str_depthSeparator+str_lineSplit[2];
						String str_cat=str_lineSplit[str_lineSplit.length-1];
						
						if (hashMap_result.containsKey(str_entityAndDepth)) 
						{
							hashMap_catAndVal = hashMap_result.get(str_entityAndDepth);
							if (hashMap_catAndVal.containsKey(str_cat)) 
							{
								hashMap_catAndVal.put(str_cat,(hashMap_catAndVal.get(str_lineSplit[str_lineSplit.length-1])+1));
							}
							else
							{
								hashMap_catAndVal.put(str_cat,1.);
							}
							//System.out.println(line);
							
						}
						else
						{
							hashMap_catAndVal.put(str_cat, 1.);
							hashMap_result.put(str_entityAndDepth, hashMap_catAndVal);
						}
						hashMap_result.put(str_entityAndDepth, hashMap_catAndVal);
					}
				}
				br_MainFile.close();
			}
		}
			 catch (IOException e) 
		{
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return hashMap_result;
			//EvaluateHeuristicFunctions.printMap(hashMap_resul);
	}
	private static void writeListToFile(List<String> results,String str_fileName)
	{
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(Global.path_Local+File.separator+str_fileName,false);
			bw = new BufferedWriter(fw);
			
			for (String str_: results) 
			{
				bw.write(str_.toString());
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
