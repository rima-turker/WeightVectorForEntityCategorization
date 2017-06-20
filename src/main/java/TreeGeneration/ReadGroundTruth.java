package TreeGeneration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

public class ReadGroundTruth 
{
	private Set<String> hsetMainBlogCat;
	private Map<String, String> mapEntityBlogCat;
	private Map<String, HashSet<String>> mapIntersection;
	private Map<String, HashSet<String>> mapUnion;	
	
	public ReadGroundTruth()
	{
		hsetMainBlogCat= new HashSet<>(WriteReadFromFile.readFileToHashSet(Global.strPathMainCat));
		mapEntityBlogCat= new HashMap<>(EvaluateHeuristicFunctions.initializeGroundTruth(Global.strfileEntBlogCat));
		mapIntersection= new HashMap<>();
		mapUnion= new HashMap<>();
		
	}
	
	public void findIntersectionUnion()
	{
		Map<String, HashSet<String>> mapProf = new LinkedHashMap<String, HashSet<String>>(WriteReadFromFile.readFileToMap(Global.pathLocal+"Prof_GT.tsv"));
		Map<String, HashSet<String>> mapTabea = new LinkedHashMap<String, HashSet<String>>(WriteReadFromFile.readFileToMap(Global.pathLocal+"Tabea_GT.tsv"));	
		Map<String,HashSet<String>> mapMary = new LinkedHashMap<String, HashSet<String>>(WriteReadFromFile.readFileToMap(Global.pathLocal+"Mary_GT.tsv"));
	
		Map<String, HashSet<String>> mapIntersection = new HashMap<String, HashSet<String>>();
		
		for (Entry <String,String> entry: mapEntityBlogCat.entrySet()) 
		{
			String strEntName = entry.getKey();
			HashSet<String> setEntValProf = mapProf.get(strEntName);
			HashSet<String> setEntValTabea = mapTabea.get(strEntName);
			HashSet<String> setEntValMary = mapMary.get(strEntName);
			
			for(String str:setEntValProf)
			{
				if (!hsetMainBlogCat.contains(str)) 
				{
					System.out.println("setEntValProf "+str);
				}
			}
			
			for(String str:setEntValTabea)
			{
				if (!hsetMainBlogCat.contains(str)) 
				{
					System.out.println("setEntValTabea "+str);
				}
			}
			
			for(String str:setEntValMary)
			{
				if (!hsetMainBlogCat.contains(str)) 
				{
					System.out.println("setEntValMary "+str);
				}
			}
			
		}
		
		for (Entry <String,String> entry: mapEntityBlogCat.entrySet()) 
		{
			String strEntName = entry.getKey();
			HashSet<String> setresult = new HashSet<>();
			HashSet<String> setresultUnion = new HashSet<>();
			
			HashSet <String> setIntersectPT = new HashSet<>(mapProf.get(strEntName));
			HashSet <String> setIntersectPM = new HashSet<>(mapProf.get(strEntName));
			HashSet <String> setIntersectTM = new HashSet<>(mapTabea.get(strEntName));
			
			setresultUnion.addAll(mapProf.get(strEntName));
			setresultUnion.addAll(mapTabea.get(strEntName));
			setresultUnion.addAll(mapMary.get(strEntName));
			setresultUnion.add(mapEntityBlogCat.get(entry.getKey()));
			
			setIntersectPT.retainAll(mapTabea.get(strEntName));
			setIntersectPM.retainAll(mapMary.get(strEntName));
			setIntersectTM.retainAll(mapMary.get(strEntName));
			
			setresult.addAll(setIntersectPT);
			setresult.addAll(setIntersectPM);
			setresult.addAll(setIntersectTM);
			setresult.add(mapEntityBlogCat.get(entry.getKey()));
			
			
			mapIntersection.put(strEntName.toLowerCase(), setresult);
			mapUnion.put(strEntName.toLowerCase(), setresultUnion);
				
		}
//		Print.printMapFormattedForExell(mapIntersection);
//		Print.printMapFormattedForExell(mapUnion);
		Print.printMap(mapEntityBlogCat);
		
	}
}
