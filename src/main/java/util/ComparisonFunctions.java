package util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import TreeGeneration.Global;
import TreeGeneration.Print;

public class ComparisonFunctions 
{
	

	public static void test_CompareTwoLists(Map<String, LinkedHashMap<String, Double>> hmap_1,
			Map<String, LinkedHashMap<String, Double>> hmap_2) {
		int int_count = 0;
		for (Entry<String, LinkedHashMap<String, Double>> entry_1 : hmap_1.entrySet()) {
			String str_entityName = entry_1.getKey();
			LinkedHashMap<String, Double> lhmap_CatAndVal = entry_1.getValue();

			if (!lhmap_CatAndVal.equals(hmap_2.get(str_entityName))) {
				//LOG.error("Two maps are not identical");
				System.out.println("ERROR");
			}
			int_count++;
		}
		System.out.println("Entities are tested:" + int_count);
	}
	
	public static void compareMaps(Map<String, HashMap<String, Double>> mapFirst ,Map<String, HashMap<String, Double>> mapSecond )
	{
		
		for (Entry<String, HashMap<String, Double>> entry : mapFirst.entrySet()) 
		{
			String str_entityNameAndDepth = entry.getKey();

//			String str_depth = str_entityNameAndDepth.substring(str_entityNameAndDepth.indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(),
//					str_entityNameAndDepth.length());
//			String str_entityName = str_entityNameAndDepth.substring(0,str_entityNameAndDepth.indexOf(Global.str_depthSeparator));

			HashMap<String, Double> lhmap_firstCatAnVal = entry.getValue();
//			HashMap<String, Double> lhmap_secondCatAnVal = mapSecond.get("john_reith,_1st_baron_reith__1");
			HashMap<String, Double> lhmap_secondCatAnVal = mapSecond.get(str_entityNameAndDepth);
			if (lhmap_secondCatAnVal==null) 
			{
				System.out.println(str_entityNameAndDepth+" is not exist in second map");
				System.out.println();
				Print.printMap(mapSecond);
			}
			else
			{
				for (Entry<String, Double> entry_cat : lhmap_firstCatAnVal.entrySet()) 
				{
					String str_cat= entry_cat.getKey();
					
					if (!lhmap_secondCatAnVal.containsKey(entry_cat.getKey()))
					{
						System.out.println(str_entityNameAndDepth+" Second map does not contain "+entry_cat.getKey());
					}
					
//					if (!entry_cat.getValue().equals(lhmap_secondCatAnVal.get(str_cat)))
//					{
//						System.out.println("");
//					}
				}
			}
			
		}
		
	}
	public static void compareMaps_(Map<String, HashSet<String>> mapFirst ,Map<String, HashSet<String>> mapSecond )
	{
		
		for (Entry<String, HashSet<String>> entry : mapFirst.entrySet()) 
		{
//			String str_entityNameAndDepth = entry.getKey();
//
//			String str_depth = str_entityNameAndDepth.substring(str_entityNameAndDepth.indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(),
//					str_entityNameAndDepth.length());
			String str_entityName = entry.getKey();

			if (mapSecond.containsKey(entry.getKey())) 
			{
				HashSet<String> setValuesFirst = new HashSet<>(entry.getValue());
				HashSet<String> setValuesSecond = new HashSet<>(mapSecond.get(entry.getKey()));
				
				for (String str: setValuesFirst) 
				{
					if (!setValuesSecond.contains(str)) 
					{
						System.out.println(str_entityName+" Second does not contain cat "+ str);
					}
				}
				for (String str: setValuesSecond) 
				{
					if (!setValuesFirst.contains(str)) 
					{
						System.out.println(str_entityName+" First does not contain cat "+ str);
					}
				}
				
			}
			else
			{
				System.out.println("Secon map Does not contain key "+entry.getKey());
			}
		} 
	}
	public static void compareTwoHashSet(HashSet<String> hset_first, HashSet<String> hset_second)
	{
		for (String str_firstElement : hset_first) 
		{
			if (!hset_second.contains(str_firstElement))
			{
				System.out.println("Second dos not contain "+str_firstElement);
			}
		}
		for (String str_firstElement : hset_second) 
		{
			if (!hset_first.contains(str_firstElement))
			{
				System.out.println("First dos not contain "+str_firstElement);
			}
		}
		//System.out.println("Finished Comparing");
	}
	public static void compareZipAndHashSet(String pathZipFile, HashSet<String> hsetCats,String srtResultFileName)
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

						File log=null;
						log = new File(srtResultFileName+ze.getName());

						if (log.exists()) 
						{
							log.delete();
						}
						log.createNewFile();

						BufferedWriter bufferedWriter = null;
						FileWriter fileWriter;

						fileWriter = new FileWriter(log, false);
						bufferedWriter = new BufferedWriter(fileWriter);

						String lineMain;

						int count = 0;

						HashSet<String> hsetResult = new HashSet<>();

						br_mainFile = new BufferedReader(
								new InputStreamReader(zf.getInputStream(ze)));

						while ((lineMain = br_mainFile.readLine()) != null) 
						{
							count++;
							lineMain=lineMain.toLowerCase().replace(">", "");
							if (hsetCats.contains(lineMain.split(" ")[2]))
							{
								hsetResult.add(lineMain);
							}
						}
						
						for(String line:hsetResult) 
						{
							bufferedWriter.write(line);
							bufferedWriter.newLine();
						}
						System.out.println("size"+hsetResult.size());
						System.out.println("count"+count);
						count=0;
						
						br_mainFile.close();
						bufferedWriter.close();
					}
					
				}
	}
	catch (IOException e) {
		e.printStackTrace();
	}
	Date end = new Date();
	System.out.println ("MainLoop: "+(end.getTime() - start.getTime())/1000 + " seconds");

}
	
	public static boolean isZipFile(File file) throws IOException {
	      if(file.isDirectory()) {
	          return false;
	      }
	      if(!file.canRead()) {
	          throw new IOException("Cannot read file "+file.getAbsolutePath());
	      }
	      if(file.length() < 4) {
	          return false;
	      }
	      DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
	      int test = in.readInt();
	      in.close();
	      return test == 0x504b0304;
	  }
	
}
