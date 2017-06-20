

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import TreeGeneration.EvaluateHeuristicFunctions;
import TreeGeneration.Global;
import TreeGeneration.HeurisitcFunctions;
import TreeGeneration.WriteReadFromFile;

public class test_MapTFIDF {

	@Test
	public void test()
	{
		Map<String, HashMap<String, Double>> hmap_testSet = new HashMap<String, HashMap<String,Double>>();
		hmap_testSet=WriteReadFromFile.readTestSet_coma(Global.pathLocal_test+"test_TestSetDistinctpaths.csv");
		
		Map<String, Integer> hmap_expected = new HashMap<String, Integer>();
		hmap_expected.put("archaeology__7", 1);
		hmap_expected.put("history__7", 1);
		hmap_expected.put("philosophy__7", 2);
		hmap_expected.put("politics__7", 2);
		hmap_expected.put("psychology__7", 2);

		hmap_expected.put("architecture__7", 1);
		hmap_expected.put("arts__7", 1);
		hmap_expected.put("physics__7", 1);
		hmap_expected.put("literature__7", 1);
		hmap_expected.put("economics__7", 1);
		hmap_expected.put("engineering__7", 1);
		hmap_expected.put("communication__7", 1);
		
		
		
		hmap_expected.put("archaeology__6", 1);
		hmap_expected.put("history__6", 1);
		
		hmap_expected.put("architecture__6", 1);
		hmap_expected.put("arts__6", 1);
		hmap_expected.put("communication__6", 1);
		hmap_expected.put("chemistry__6", 1);
		hmap_expected.put("politics__6", 1);
		hmap_expected.put("mathematics__6", 1);
		hmap_expected.put("engineering__6", 1);
		hmap_expected.put("transport__6", 1);
		hmap_expected.put("philosophy__6", 1);
		hmap_expected.put("economics__6", 1);
		hmap_expected.put("physics__6", 1);

		hmap_expected.put("archaeology__5", 1);
		hmap_expected.put("history__5", 1);
		
		hmap_expected.put("architecture__5", 1);
		hmap_expected.put("arts__5", 1);
		hmap_expected.put("physics__5", 1);
		hmap_expected.put("communication__5", 1);
		hmap_expected.put("engineering__5", 1);
		hmap_expected.put("transport__5", 1);
		
		
		hmap_expected.put("archaeology__4", 1);
		hmap_expected.put("inventions__4", 1);
		hmap_expected.put("architecture__4", 1);
		hmap_expected.put("engineering__4", 1);
		hmap_expected.put("photography__4", 1);
	

		hmap_expected.put("chemistry__3", 1);
		hmap_expected.put("architecture__3", 1);
		
		Map<String,Integer> hmap_actual = new HashMap<String,Integer>();
		hmap_actual=HeurisitcFunctions.initializeMapForTFIDF(hmap_testSet);
		
		for (Entry<String, Integer> entry: hmap_expected.entrySet()) 
		{
			//System.out.println(entry.getKey()+" "+hmap_actual.get(entry.getKey()));
			assertEquals(hmap_actual.get(entry.getKey()), hmap_expected.get(entry.getKey()));
		}
		

		
	}

}
