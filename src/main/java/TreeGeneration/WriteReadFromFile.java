package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class WriteReadFromFile 
{
	
	public static void readZipFile(String pathZipFile)
	{
		Date start = new Date();
		try {
			ZipFile zf = new ZipFile(pathZipFile);
			Enumeration<? extends ZipEntry> entries = zf.entries();

			while (entries.hasMoreElements()) 
			{
				ZipEntry ze = (ZipEntry) entries.nextElement();
				//ze = (ZipEntry) entries.nextElement();
				long size = ze.getSize();
				if (size > 0) 
				{
					BufferedReader br_mainFile = new BufferedReader(
							new InputStreamReader(zf.getInputStream(ze)));

						HashSet<String> hsetResult = new HashSet<>();
					    List<Double> arrList = new ArrayList<>();
						int count =0;
						String lineMain;
						while ((lineMain = br_mainFile.readLine()) != null) 
						{
							//count++;
							//System.out.println(lineMain);
							String prolineMain = lineMain.substring(lineMain.indexOf("6={")+("6={").length()).replaceAll("}", "");
							if (prolineMain.length()>1) 
							{
								String[] split = prolineMain.split(", ");
							
								for (int i = 0; i < split.length; i++)
								{
									//hsetResult.add(split[i].split("=")[1]);
									//System.out.println(split[i].split("=")[1]);
									arrList.add(Double.parseDouble(split[i].split("=")[1]));
								}
							}
							else
							{
								//System.out.println(lineMain);
								count++;
							}
							
							
							
							
//							if (lineMain.split(" ")[0].equals("Albert_Einstein>")) 
//							{
//								hsetResult.add(lineMain);
//							}
							
						}
						//System.out.println("size"+hsetResult.size());
						System.out.println("size "+arrList.size());
						System.out.println("Count empty entities: "+ count);
						double[] target = new double[arrList.size()];
						 for (int i = 0; i < target.length; i++) {
						    target[i] = arrList.get(i);                // java 1.5+ style (outboxing)
						 }
						
						
						double threshold = FindQuartile.findOutliers(target);
						
						long positive = 0;
						long negative = 0;
						for (int i = 0; i < target.length; i++) {
						    if(target[i]<=threshold) {
						    	positive++;
						    }else {
						    	negative++;
						    }
						 }
						
						
						double keepPercentage = (100.*positive/target.length);
						
						System.out.println("keep percenatge "+keepPercentage);
						System.out.println("remove percenatge "+(100- keepPercentage));
						br_mainFile.close();
						
//						for (String str : hsetResult) 
//						{
//							System.out.println(str);
//						}
					}
					
				}
			
	}
	catch (IOException e) {
		e.printStackTrace();
	}
	Date end = new Date();
	System.out.println ("MainLoop: "+(end.getTime() - start.getTime())/1000 + " seconds");

}	
	
	public static Map<String, HashSet<String>> readFileToMap(String fileName) {
		
		 Map<String, HashSet<String>> hmap_groundTruthlist  = new LinkedHashMap();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName));) {

			String str_entity = null, str_mainCat = null;
			String line;
			while ((line = br.readLine()) != null) {
				line = line.toLowerCase();
				//System.out.println(line);
				HashSet<String> ll_goalSet = new HashSet<>();
				String[] str_split = line.split("\t");
				str_entity = str_split[0];
				
				if ( str_split.length > 1) 
				{
					str_mainCat = str_split[1];
					for (int i = 0; i < str_split.length; i++) {
						
						if (i != 0) {
							ll_goalSet.add(str_split[i]);
						}
					}
				}
				
				hmap_groundTruthlist.put(str_entity, ll_goalSet);
			}

		} 
		
		catch (IOException e) {

			System.out.println(e.getMessage());
			e.printStackTrace();

		}
		return hmap_groundTruthlist;
	}
	public static HashSet<String> readFileToHashSet(String str_fileName)
	{
		HashSet<String> hset_results = new HashSet<>();
		try 
		{
			BufferedReader br_FamEntities = new BufferedReader(new FileReader(str_fileName));
			String line = null;
			while ((line = br_FamEntities.readLine()) != null) 
			{
				hset_results.add(line.toLowerCase());
			}
			br_FamEntities.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hset_results;
	}
	/*
	 * This function only reads entites and their corresponding categories no value
	 * like ground truth list or for each entity top 3
	 */
	
	static ArrayList<String> readFileToList(String str_fileName) 
	{
		ArrayList<String> arrList_result = new ArrayList<>();
		String line=null;
		try {
			BufferedReader br_MainFile = new BufferedReader(new FileReader(Global.pathLocal+File.separator+str_fileName));
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
		try (BufferedReader br = new BufferedReader(new FileReader(Global.pathLocal + fileName));) {

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

		File file =(new File(Global.pathLocal+str_fileName+".csv"));
		try {
			file.createNewFile();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			Iterator it = mp.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				String str_entityAndCatList = pair.getKey().toString().replace(Global.str_depthSeparator,",")+pair.getValue().toString().replace("{",",").replace("}","");
				bufferedWriter.write(str_entityAndCatList);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished Writing to a file: "+file.getName()+" "+file.getAbsolutePath());
	}
	public static void writeSetToFile(String fileToRead,String str_fileName) 
	{
		String line=null;

		File file =(new File(str_fileName));
		try {
			file.createNewFile();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(str_fileName+"_Formated"), false));
			BufferedReader br_MainFile = new BufferedReader(new FileReader(fileToRead));
			while ((line = br_MainFile.readLine()) != null) 
			{
				bufferedWriter.write(line.toLowerCase());
				bufferedWriter.newLine();
			}
			br_MainFile.close();
			bufferedWriter.close();
		} 	
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeSetFile(HashSet<String> set,String strFileName) 
	{
		try {
			
			File file =(new File(strFileName));
			
			file.createNewFile();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			for(String str : set)
			{
				bufferedWriter.write(str);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
			System.out.println("Finished Writing to a file: "+file.getName()+" "+file.getAbsolutePath());
		} 
		catch (IOException e) 
		{
				e.printStackTrace();
		}
		
	}
	
	public static void writeSetFile_db(HashSet<Double> set,String strFileName) 
	{
		try {
			
			File file =(new File(strFileName));
			
			file.createNewFile();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			for(Double db : set)
			{
				bufferedWriter.write(String.valueOf(db));
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
			System.out.println("Finished Writing to a file: "+file.getName()+" "+file.getAbsolutePath());
		} 
		catch (IOException e) 
		{
				e.printStackTrace();
		}
		
	}
	
	public static Map<String, String> entityNameMapCase(String fileName)
	{
		Map<String, String> mapResults = new HashMap<>();
		
		try {
			String line = null;
			int i =0;
			BufferedReader br_MainFile = new BufferedReader(new FileReader(fileName));
			
			while ((line = br_MainFile.readLine()) != null) 
			{
				String[] split = line.replaceAll(">", "").split(" ");
//				System.out.println(split[0]+" "+split[1]);
//				System.out.println("for Map "+split[0].toLowerCase()+" "+split[0]);
				mapResults.put(split[0].toLowerCase(), split[0]);
			}
			br_MainFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Size of Entity Map "+mapResults.size() );
		return mapResults;
		
	}
	/*
	 * This function only changes the format of the WV for evaluation
	 * However since evaluation format requers Capital letter for each entity, in the first line Mapping is used to convert each entity to
	 * a capital letter
	 */
	public static HashSet<String> formatForWeightVector(Map<String, HashMap<String, Double>> mapToFormat)
	{
		Map<String, String> mapEntities = new HashMap<>(entityNameMapCase(Global.pathServer+"article_categories_clean2016_CatFiltered_7"));
		
		
		//<$100_Film_Festival>      < 0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,0 ,1 ,0 ,0 ,0 ,0 ,0 ,0>
		
		//String line= "albert_einstein	7={chemistry=0.05945411586914874, politics=1.6804591596553597, transport=0.103678303083399, history=0.7341221734991089, engineering=0.4063858979926278, philosophy=2.9891100754301267, literature=0.46382888130830713, economics=0.3486273820841701, psychology=0.6903195854275956, communication=0.024007225726569038, arts=0.44858320162873133, architecture=0.10931021437959335, physics=8.117516056507183, outer_space=0.3771555550903578, mathematics=0.2757756240130567, inventions=2.8424083313936026, photography=3.5235875870740054}";
		//<albert_einstein>	<0.0 ,0.10931 ,0.44858 ,0.0 ,0.0 ,0.0 ,0.0 ,0.0 ,0.05945 ,0.0 ,0.02401 ,0.0 ,0.34863 ,0.0 ,0.40639 ,0.0 ,0.0 ,0.73412 ,2.84241 ,0.46383 ,0.27578 ,0.0 ,0.0 ,0.37716 ,0.0 ,2.98911 ,3.52359 ,8.11752 ,0.0 ,1.68046 ,0.0 ,0.69032 ,0.10368 ,0.0 ,0.0>
		
		
		HashSet<String> setResult = new HashSet<>();
//		String entityName = line.split("\t")[0];
//		String[]  strCats = line.split("\t")[1].replace("7={", "").replace("}", "").replaceAll(" ", "").split(",");
		Map<String, Integer> mapCat= new LinkedHashMap<>();
		
		try {
			String line = null;
			int i =0;
			BufferedReader br_MainFile = new BufferedReader(new FileReader(Global.pathServer+"MainCategoryFile"));
			//BufferedReader br_MainFile = new BufferedReader(new FileReader(Global.strPathMainCat));
			while ((line = br_MainFile.readLine()) != null) 
			{
				line= line.toLowerCase();
				mapCat.put(line.replaceAll(">", ""), i);
				i++;
			}
			
			for (Entry<String, HashMap<String, Double>> entry : mapToFormat.entrySet()) {
				
				String[] result =  new String [mapCat.size()];
				for (int j = 0; j < result.length; j++)
				{
					result[j]="0";
				}
				
				String str_entityNameAndDepth = entry.getKey();
				String str_entityName=str_entityNameAndDepth.split("\t")[0];
				String str_depth =str_entityNameAndDepth.split("\t")[1];
//				if (str_entityName.contains("albert_einstein"))
//					System.out.println("YES");
				final Map<String, Double> lhmap_catAnVal = new HashMap<>(entry.getValue());

				for (final Entry<String, Double> entry_cat : lhmap_catAnVal.entrySet()) 
				{
					result[mapCat.get(entry_cat.getKey())] = String.valueOf(entry_cat.getValue());
				}
				
				Locale.setDefault(Locale.US);
				DecimalFormat df = new DecimalFormat("0.00000");
				
				
				//String wv= "<"+str_entityName+">"+"\t"+"<";
				if (mapEntities.containsKey(str_entityName)) {
					
					
					String wv= "<"+mapEntities.get(str_entityName)+">"+"\t"+"<"; 
					
					for (int j = 0; j < result.length; j++) 
					{
						
						double val = Double.valueOf(result[j]);
						String strformated = String.valueOf(val);
						if (val>0) 
						{
							strformated = df.format(val);
						}
						wv+=strformated+" ,";
						//System.out.print(strformated+" ,");
					}
					
					wv+=">";
					wv=wv.replace(" ,>", ">");
					//System.out.println();
					if (setResult.contains(wv)) {
						System.out.println(wv);
					}
					setResult.add(wv);
					//System.out.println(wv);
				}
				else
				{
					System.out.println("ERROR "+str_entityName);
				}
				
				
			}
			System.out.println("Size of the WV(total entity Count) "+ setResult.size());
//			for (Entry<String, Integer> entry:mapCat.entrySet()) 
//			{
//				System.out.println(entry.getKey()+" " +entry.getValue());
//			}
//			for (int j = 0; j < strCats.length; j++) 
//			{
//				String[] cat = strCats[j].split("=");
//				result[mapCat.get(cat[0])] =cat[1]; 
//			}
			
			br_MainFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return setResult;
	}
	public static void formatMapFile(String str_fileName, String pathResultFile) 
	{
		//sed 's/__1={/,1,/' pageLinksDistinctPaths | sed 's/__2={/,2,/' | sed 's/__3={/,3,/' | sed 's/__4={/,4,/' | sed 's/__5={/,5,/' | sed 's/__6={/,6,/' | sed 's/__7={/,7,/' | sed 's/}//'
	
		/*
		 * florida_atlantic_university_athletic_hall_of_fame       7={history=2.0, architecture=1.0}
		 */
		String line=null;
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(pathResultFile), false));
			BufferedReader br_MainFile = new BufferedReader(new FileReader(str_fileName));
			while ((line = br_MainFile.readLine()) != null) 
			{
				line=line.toLowerCase().replace("1={", "1\t").replace("2={", "2\t").replace("3={", "3\t").replace("4={", "4\t").replace("5={", "5\t").replace("6={", "6\t").replace("7={", "7\t").replace("}", "").replace(", ", "\t");
				
				bufferedWriter.write(line);
				bufferedWriter.newLine();
				
				
			}
			br_MainFile.close();
			bufferedWriter.close();
			System.out.println("Finished writing "+str_fileName+"_Formated");
		} 	
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void writeMapToAFile(Map mp,String str_fileName) {

		File file =(new File(str_fileName));
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
	
	
//	public static Map<String, HashMap<String, Double>> readTestSet(String str_fileName)
//	{
//		Map<String, HashMap<String, Double>> hmap_result = new LinkedHashMap<>();
//		String line=null;
//		try {
//			BufferedReader br_MainFile = new BufferedReader(new FileReader(str_fileName));
//			while ((line = br_MainFile.readLine()) != null) 
//			{
//				
//				line=line.toLowerCase();
//				HashMap<String, Double> hmap_catAndValues = new HashMap<>();
//				
//				if (line.contains(",")) 
//				{
//					String strSplit=line.toLowerCase(); 
//					while (strSplit.length()>0) 
//					{
//						
//						String strTemp = strSplit.substring(strSplit.lastIndexOf(",")); 
//						if (strTemp.length()==0 || strTemp.equals(",")) 
//						{
//							strSplit = strSplit.substring(0, strSplit.length()-1);
//						}
//						
//						else if (strTemp.length()>0&&strTemp.contains("=")&&strTemp.matches(",.*=\\d*\\.0")) 
//						{
//							//hmap_catAndValues.put(str_cat,Double.valueOf(str_split[i].substring(str_split[i].indexOf("=")+1,str_split[i].length())));
//							hmap_catAndValues.put(strTemp.substring(1,strTemp.indexOf("=")), Double.valueOf(strTemp.substring(strTemp.lastIndexOf("=") + 1,strTemp.length()-1)));
//							strSplit=strSplit.substring(0, strSplit.length()-strTemp.length());
//						}
//						else if (strTemp.length()==2&&strTemp.matches(",[0-9]")) 
//						{
//							//strTemp = ,1
//							String strDepth= strTemp.substring(1, strTemp.length());
//							String strEntityName = strSplit.replace(","+strDepth, "");
//							hmap_result.put((strEntityName+Global.str_depthSeparator+strDepth), hmap_catAndValues);
//							strSplit=strSplit.substring(0, strSplit.length()-strTemp.length());
//							break;
//						
//						}
//						
//					}
//				}
//				
//				
//				
//				
//				
////				String[] str_split = line.split(",");
////				HashMap<String, Double> hmap_catAndValues = new HashMap<>();
////				for (int i = 2; i < str_split.length; i++) 
////				{
////					String str_cat;
////					if (!str_split[i].contains("="))
////					{
////						 str_cat = str_split[i];
////					}
////					else
////					{
////						 str_cat = str_split[i].substring(0,str_split[i].indexOf("="));
////					}
////					
////					hmap_catAndValues.put(str_cat,Double.valueOf(str_split[i].substring(str_split[i].indexOf("=")+1,str_split[i].length())));
////				}
////				hmap_result.put((str_split[0]+GlobalVariables.str_depthSeparator+str_split[1]), hmap_catAndValues);
//			}
//			br_MainFile.close();
//		} 	
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		//Print.printMap(hmap_result);
//		return hmap_result;
//	}
	public static Map<String, HashMap<String, Double>> readTestSet_coma(String str_fileName)
	{
		
		Map<String, HashMap<String, Double>> hmap_result = new LinkedHashMap<>();
		String line=null;
		try {
			BufferedReader br_MainFile = new BufferedReader(new FileReader(str_fileName));
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
							hmap_result.put((strEntityName+Global.str_depthSeparator+strDepth), hmap_catAndValues);
							strSplit=strSplit.substring(0, strSplit.length()-strTemp.length());
							break;
						
						}
						
					}
				}
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
	
public static Map<String, HashMap<String, Double>> readTestSet_tab(String str_fileName)
{
	
	Map<String, HashMap<String, Double>> hmap_result = new LinkedHashMap<>();
	String line=null;
	try {
		BufferedReader br_MainFile = new BufferedReader(new FileReader(str_fileName));
		while ((line = br_MainFile.readLine()) != null) 
		{
			line=line.toLowerCase();
			HashMap<String, Double> hmap_catAndValues = new HashMap<>();
			String[] strSplit = line.split("\t");
			String strEntName = strSplit[0];
			String strDepth = strSplit[1];
			
			if (strSplit.length>2) 
			{
				for (int i = 2; i < strSplit.length; i++) 
				{
					//System.out.println(strEntName);
					hmap_catAndValues.put(strSplit[i].substring(0, strSplit[i].indexOf("=")), Double.valueOf(strSplit[i].substring(strSplit[i].indexOf("=")+1)));
					
				}
			}
			//"Albert_	1", map
			hmap_result.put((strEntName+Global.str_depthSeparator+strDepth), hmap_catAndValues);
					
		}
		br_MainFile.close();
	} 	
	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return hmap_result;
}
public static Map<String, HashMap<String, Double>> fixSpecialCharProblem(Map<String, HashMap<String, Double>> mapTest,HashSet<String> setEntity)
{
	Map<String, HashMap<String, Double>> hmap_result = new LinkedHashMap<>(mapTest);
	
	for(String entitiy : setEntity)
	{
		for (int i = 1; i <=Global.levelOfTheTree; i++) 
		{
			if (!mapTest.containsKey(entitiy+Global.str_depthSeparator+i)) 
			{
				System.out.println(entitiy+Global.str_depthSeparator+i+ "added to testSet");
				hmap_result.put((entitiy+Global.str_depthSeparator+i), new HashMap<String,Double>());
			}
		}
	}
	return  hmap_result;
	
}

}





