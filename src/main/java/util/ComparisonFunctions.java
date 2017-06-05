package util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import TreeGeneration.Global;

public class ComparisonFunctions 
{
	public static boolean compareMaps(Map<String, HashMap<String, Double>> hmap_heuResult ,Map<String, HashMap<String, Double>> hmap_second )
	{
		for (Entry<String, HashMap<String, Double>> entry : hmap_heuResult.entrySet()) 
		{
			String str_entityNameAndDepth = entry.getKey();

			String str_depth = str_entityNameAndDepth.substring(str_entityNameAndDepth.indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(),
					str_entityNameAndDepth.length());
			String str_entityName = str_entityNameAndDepth.substring(0,str_entityNameAndDepth.indexOf(Global.str_depthSeparator));

			HashMap<String, Double> lhmap_firstCatAnVal = entry.getValue();
			HashMap<String, Double> lhmap_secondCatAnVal = hmap_second.get(str_entityNameAndDepth);

			for (Entry<String, Double> entry_cat : lhmap_firstCatAnVal.entrySet()) 
			{
				String str_cat= entry_cat.getKey();
				if (!entry_cat.getValue().equals(lhmap_secondCatAnVal.get(str_cat)))
				{
					return false;
				}
			}
		}
		return true;
	}
	public static void compareTwoHashSet(String str_first , String str_second)
	{
//		HashSet<String> hset_first = ReadFileReturnHashSet(str_first);
//		HashSet<String> hset_second = ReadFileReturnHashSet(str_second);
//		
//		for (String str_firstElement : hset_first) 
//		{
//			if (hset_second.contains(str_firstElement))
//			{
//				System.out.println(str_firstElement);
//			}
//		}
//		System.out.println("Finished Comparing");
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
