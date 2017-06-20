package TreeGeneration;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Print 
{
	public static void printMapOnlyLevel(Map<String, HashMap<String, Double>> mp, String str_depth) {
		
		for (Entry<String, HashMap<String, Double>> entry : mp.entrySet()) 
		{
			if (entry.getKey().contains(str_depth)) 
			{
				System.out.println(entry.getKey()+"="+mp.get(entry.getKey()));
			}
		}
	}
	public static <K> void printSet(Set<K> set) 
	{
		for (K str : set) 
		{
			System.out.println(str);
		}
	}
	public static void printMap(Map mp) {
		// log.info(heu);
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			// //System.out.println(pair.getKey() + " = " + pair.getValue());
			//log.info(pair.getKey() + " = " + pair.getValue());
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
		// log.info("----------------------------");
		// log.info("");
	}
	public static void printMapFormattedForExell(Map mp) {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			String str_entityAndCatList = pair.getKey().toString().replace("__",",")+pair.getValue().toString().replace("{",",").replace("}","");
			System.out.println(str_entityAndCatList);
		}
	}
	
	public static void printMapOrdered(Map mp,Map<String, HashMap<String, Double>> hmap_testSet) 
	{
		for (Entry<String, HashMap<String, Double>> entry : hmap_testSet.entrySet()) 
		{
			if (mp.containsKey(entry.getKey())) 
			{
				System.out.println(entry.getKey()+"="+mp.get(entry.getKey()));
			}
			else
			{
				System.err.println("-------------"+entry.toString());
			}
		}
	}
	public static void printMapOnlyEntities(Map<String, HashMap<String, Double>> mp) {
		
		HashSet<String> setEntities = new HashSet<>();
		for (Entry<String, HashMap<String, Double>> entry : mp.entrySet()) 
		{
			
			setEntities.add(entry.getKey().substring(0,entry.getKey().indexOf(Global.str_depthSeparator)));
		}
		for (String str: setEntities) {
			System.out.println(str);
			
			
		}
	}
	public static void printMapOnlyCats(Map<String, HashMap<String, Double>> mp) {
		
		for (Entry<String, HashMap<String, Double>> entry : mp.entrySet()) 
		{
			System.out.print(entry.getKey()+" ");
			for(Entry <String, Double> entry_catAndVal : entry.getValue().entrySet()) 
			System.out.print(" "+entry_catAndVal.getKey()+" ");
			
			System.out.println();
			
		}
	}
	public static void printHashMapFormated(Map<String, HashMap<String, Double>> lhmap_print)
	{
		String str_format = "=SPLIT(\"";
		Locale.setDefault(Locale.US);
		DecimalFormat df = new DecimalFormat("0.00000");
		for (Entry<String, HashMap<String, Double>> entry : lhmap_print.entrySet()) 
		{
			String str_entityName = entry.getKey().substring(0, entry.getKey().indexOf(Global.str_depthSeparator));
			HashMap<String, Double> hmap_catAndVal = new HashMap<>(entry.getValue());
			Integer int_depth = Integer.parseInt(entry.getKey().substring(
					entry.getKey().indexOf(Global.str_depthSeparator) + Global.str_depthSeparator.length(), entry.getKey().length())); 

			str_format+=""+str_entityName+","+int_depth.toString()+",";
			for (int i = int_depth; i < Global.levelOfTheTree; i++) 
			{
				str_format+=""+",";

			}
			for (Entry<String, Double> entry_CatAndVal : hmap_catAndVal.entrySet()) 
			{
				str_format+=entry_CatAndVal.getKey()+":"+df.format(entry_CatAndVal.getValue()) +" ," ;
			}
			str_format += "\",\",\")";
			System.out.println(str_format);
			str_format = "=SPLIT(\"";
		}
	}
}
