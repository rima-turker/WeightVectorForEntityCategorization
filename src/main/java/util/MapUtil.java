package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import TreeGeneration.EvaluateHeuristicFunctions;
import TreeGeneration.Global;
import TreeGeneration.ReadGroundTruth;

import java.util.Set;

public class MapUtil 
{
	
	public static HashSet<String> getKeySetFromMap(Map<String, HashMap<String, Double>> mp )
	{	
		HashSet<String> setEntities = new HashSet<>();
		for (Entry<String, HashMap<String, Double>> entry : mp.entrySet()) 
		{
			setEntities.add(entry.getKey().substring(0,entry.getKey().indexOf(Global.str_depthSeparator)));
		}
		return setEntities;
	}
	public static HashSet<String> getKeySetFromMap_2(Map<String, HashSet<String>> mp )
	{	
		HashSet<String> setEntities = new HashSet<>();
		for (Entry<String, HashSet<String>> entry : mp.entrySet()) 
		{
			setEntities.add(entry.getKey());
		}
		return setEntities;
	}
	public static HashSet<String> getKeySetFromMap_3(Map<String,String> mp )
	{	
		HashSet<String> setEntities = new HashSet<>();
		for (Entry<String, String> entry : mp.entrySet()) 
		{
			setEntities.add(entry.getKey());
		}
		return setEntities;
	}
	private static Map<String, Double> sortByValues(Map<String, Double> unsortMap) { 
		Map<String, Double> result = new LinkedHashMap<>();

		//sort by value, and reserve, 10,9,8,7,6...
		unsortMap.entrySet().stream()
		.sorted()
		.forEachOrdered(x -> result.put(x.getKey(), x.getValue()));
		return result;
	}
	public static Map<String, HashMap<String, Double>> getAsMapCertainLevel(int level,Map<String, HashMap<String, Double>> mapAggregated)
	{
			
		Map<String, HashMap<String, Double>> mapResult = new HashMap<>();
		
		for (Entry<String, HashMap<String, Double>> entry : mapAggregated.entrySet()) {
			
			String str_entityNameAndDepth = entry.getKey();
			String str_depth =str_entityNameAndDepth.split("\t")[1];
			
			if (str_depth.equals(Integer.toString(level)))
			{
				mapResult.put(entry.getKey(), entry.getValue());
			}


		}
		
		return mapResult;
	}
	public static <K, V extends Comparable<? super V>> Map<K, V> 
	sortByValue( Map<K, V> map )
	{
		List<Map.Entry<K, V>> list =
				new LinkedList<Map.Entry<K, V>>( map.entrySet() );
		Collections.sort( list, new Comparator<Map.Entry<K, V>>()
		{
			public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
			{
				return (o1.getValue()).compareTo( o2.getValue() );
			}
		} );

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list)
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V>
	entriesSortedByValues(Map<K,V> map) {

		List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());

		Collections.sort(sortedEntries, 
				new Comparator<Entry<K,V>>() {
			@Override
			public int compare(Entry<K,V> e1, Entry<K,V> e2) {
				return e2.getValue().compareTo(e1.getValue());
			}
		}
				);

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : sortedEntries)
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
	
	public static <K, V extends Comparable<? super V>> void printTopElementsMap(Map<String,Double> map, int int_topN)
	{
		Map<K, V> result = new LinkedHashMap<K, V>();
		Map<String, Double> hmap_result = new HashMap<>(MapUtil.entriesSortedByValues(map));
		
		int count =0;
		for (Entry <String, Double>   entry: hmap_result.entrySet()) 
		{
			System.out.print(entry.getKey()+",");
			count++;
			if (count>=int_topN) 
			{
				System.out.println();
				return;
			}
		}
		System.out.println();
	}
}
