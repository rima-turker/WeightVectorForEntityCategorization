package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class WriteReadFromFile 
{
	/*
	 * This function only reads entites and their corresponding categories no value
	 * like ground truth list or for each entity top 3
	 */
	
	static ArrayList<String> readFileToList(String str_fileName) 
	{
		ArrayList<String> arrList_result = new ArrayList<>();
		String line=null;
		try {
			BufferedReader br_MainFile = new BufferedReader(new FileReader(GlobalVariables.path_Local+File.separator+str_fileName));
			while ((line = br_MainFile.readLine()) != null) 
			{
				line= line.toLowerCase();
				arrList_result.add(line.replaceAll(">", ""));
				//hmap_result.put(line.split(" ")[0].replace(">", ""), line.split(" ")[1].replace(">", ""));
			}
			br_MainFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrList_result;
	}
	public static Map<String, HashSet<String>> readEntitiesAndCats(String fileName) {
		
		Map<String, HashSet<String>> hmap_groundTruthlist = new LinkedHashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(GlobalVariables.path_Local + fileName));) {

			String str_entity = null;
			String line;
			while ((line = br.readLine()) != null) {
				line = line.toLowerCase();
				HashSet<String> ll_goalSet = new HashSet<>();
				String[] str_split = line.split(",");
				for (int i = 0; i < str_split.length; i++) {
					str_entity = str_split[0];
					if (i != 0) {
						ll_goalSet.add(str_split[i]);
					}
				}

				hmap_groundTruthlist.put(str_entity, ll_goalSet);
			}

			for (Entry<String, HashSet<String>> entry : hmap_groundTruthlist.entrySet()) {
				str_entity = entry.getKey();
				HashSet<String> str_categories = entry.getValue();

				// System.out.println(str_entity+ " "+ str_categories);
			}
		} catch (IOException e) {

			System.out.println(e.getMessage());
			e.printStackTrace();

		}
		return hmap_groundTruthlist;
	}
	public static void writeMapFormattedForExell(Map mp,String str_fileName) {

		File file =(new File(GlobalVariables.path_Local+str_fileName+".csv"));
		try {
			file.createNewFile();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			Iterator it = mp.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				String str_entityAndCatList = pair.getKey().toString().replace(GlobalVariables.str_depthSeparator,",")+pair.getValue().toString().replace("{",",").replace("}","");
				bufferedWriter.write(str_entityAndCatList);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished Writing to a file: "+file.getName()+" "+file.getAbsolutePath());
	}

	public static void writeMapToAFile(Map mp,String str_fileName) {

		File file =(new File(GlobalVariables.path_Local+str_fileName));
		try {
			file.createNewFile();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			Iterator it = mp.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				bufferedWriter.write(pair.getKey()+"="+pair.getValue());
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished Writing to a file: "+file.getName()+" "+file.getAbsolutePath());
	}
	public static Map<String, HashMap<String, Double>> readTestSet(String str_fileName)
	{
		Map<String, HashMap<String, Double>> hmap_result = new LinkedHashMap<>();
		String line=null;
		try {
			BufferedReader br_MainFile = new BufferedReader(new FileReader(GlobalVariables.path_Local+File.separator+str_fileName));
			while ((line = br_MainFile.readLine()) != null) 
			{
				
				line=line.toLowerCase();
				HashMap<String, Double> hmap_catAndValues = new HashMap<>();
				
				if (line.contains(",")) 
				{
					String strSplit=line.toLowerCase(); 
					while (strSplit.length()>0) 
					{
						
						String strTemp = strSplit.substring(strSplit.lastIndexOf(",")); 
						if (strTemp.length()==0 || strTemp.equals(",")) 
						{
							strSplit = strSplit.substring(0, strSplit.length()-1);
						}
						
						else if (strTemp.length()>0&&strTemp.contains("=")&&strTemp.matches(",.*=\\d*\\.0")) 
						{
							//hmap_catAndValues.put(str_cat,Double.valueOf(str_split[i].substring(str_split[i].indexOf("=")+1,str_split[i].length())));
							hmap_catAndValues.put(strTemp.substring(1,strTemp.indexOf("=")), Double.valueOf(strTemp.substring(strTemp.lastIndexOf("=") + 1,strTemp.length()-1)));
							strSplit=strSplit.substring(0, strSplit.length()-strTemp.length());
						}
						else if (strTemp.length()==2&&strTemp.matches(",[0-9]")) 
						{
							//strTemp = ,1
							String strDepth= strTemp.substring(1, strTemp.length());
							String strEntityName = strSplit.replace(","+strDepth, "");
							hmap_result.put((strEntityName+GlobalVariables.str_depthSeparator+strDepth), hmap_catAndValues);
							strSplit=strSplit.substring(0, strSplit.length()-strTemp.length());
							break;
						
						}
						
					}
				}
				
				
				
				
				
//				String[] str_split = line.split(",");
//				HashMap<String, Double> hmap_catAndValues = new HashMap<>();
//				for (int i = 2; i < str_split.length; i++) 
//				{
//					String str_cat;
//					if (!str_split[i].contains("="))
//					{
//						 str_cat = str_split[i];
//					}
//					else
//					{
//						 str_cat = str_split[i].substring(0,str_split[i].indexOf("="));
//					}
//					
//					hmap_catAndValues.put(str_cat,Double.valueOf(str_split[i].substring(str_split[i].indexOf("=")+1,str_split[i].length())));
//				}
//				hmap_result.put((str_split[0]+GlobalVariables.str_depthSeparator+str_split[1]), hmap_catAndValues);
			}
			br_MainFile.close();
		} 	
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Print.printMap(hmap_result);
		return hmap_result;
	}
}
