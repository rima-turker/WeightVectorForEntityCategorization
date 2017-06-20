

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import TreeGeneration.EvaluateHeuristicFunctions;

public class test_AggregationFunction {

	@Test
	public void test() {
		Map<String, HashMap<String, Double>> hmap_actual = new HashMap<String, HashMap<String, Double>>();
		
		HashMap<String, Double> hmap_temp = new HashMap<>();
		hmap_temp.put("politics", 4.0);
		hmap_temp.put("psychology", 1.0);
		hmap_temp.put("philosophy", 1.0);
		hmap_actual.put("anne_claude_de_caylus__7", hmap_temp);

		hmap_temp = new HashMap<>();
		hmap_actual.put("anne_claude_de_caylus__6", hmap_temp);
		
		hmap_temp = new HashMap<>();
		hmap_temp.put("politics", 1.0);
		hmap_temp.put("archaeology", 1.0);
		hmap_temp.put("history", 1.0);
		hmap_actual.put("anne_claude_de_caylus__5", hmap_temp);

		hmap_temp = new HashMap<>();
		hmap_temp.put("archaeology", 1.0);
		hmap_actual.put("anne_claude_de_caylus__4", hmap_temp);
		
		hmap_temp = new HashMap<>();
		hmap_temp.put("history", 1.0);
		hmap_actual.put("anne_claude_de_caylus__3", hmap_temp);
		
		hmap_temp = new HashMap<>();
		hmap_actual.put("anne_claude_de_caylus__2", hmap_temp);
		
		hmap_temp = new HashMap<>();
		hmap_actual.put("anne_claude_de_caylus__1", hmap_temp);
		
		
//
		Map<String, HashMap<String, Double>> hmap_function = new HashMap<String, HashMap<String, Double>>(
		EvaluateHeuristicFunctions.aggregateCategoryValues(hmap_actual));

		Map<String, HashMap<String, Double>> hmap_expected = new HashMap<String, HashMap<String, Double>>();
	
		hmap_temp = new HashMap<>();
		hmap_temp.put("politics", 5.0);
		hmap_temp.put("psychology", 1.0);
		hmap_temp.put("philosophy", 1.0);
		hmap_temp.put("history", 2.0);
		hmap_temp.put("archaeology", 2.0);
		hmap_actual.put("anne_claude_de_caylus__7", hmap_temp);

		hmap_temp = new HashMap<>();
		hmap_temp.put("politics", 1.0);
		hmap_temp.put("history", 2.0);
		hmap_temp.put("archaeology", 2.0);
		hmap_actual.put("anne_claude_de_caylus__6", hmap_temp);
		
		
		hmap_temp = new HashMap<>();
		hmap_temp.put("politics", 1.0);
		hmap_temp.put("history", 2.0);
		hmap_temp.put("archaeology", 2.0);
		hmap_actual.put("anne_claude_de_caylus__5", hmap_temp);

		hmap_temp = new HashMap<>();
		hmap_temp.put("archaeology", 1.0);
		hmap_temp.put("history", 1.0);
		hmap_actual.put("anne_claude_de_caylus__4", hmap_temp);
		
		hmap_temp = new HashMap<>();
		hmap_temp.put("history", 1.0);
		hmap_actual.put("anne_claude_de_caylus__3", hmap_temp);
		
		hmap_temp = new HashMap<>();
		hmap_actual.put("anne_claude_de_caylus__2", hmap_temp);
		
		hmap_temp = new HashMap<>();
		hmap_actual.put("anne_claude_de_caylus__1", hmap_temp);
		
		assertEquals(hmap_actual.get("anne_claude_de_caylus__7"), hmap_function.get("anne_claude_de_caylus__7"));
	}

}
