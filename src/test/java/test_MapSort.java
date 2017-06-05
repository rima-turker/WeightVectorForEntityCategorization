

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import org.junit.Test;

import util.MapUtil;

public class test_MapSort {

	@Test
	public void test() 
	{
		HashMap<String, Double> hmap_unsort = new HashMap<>();
		hmap_unsort.put("Rima", 10.);
		hmap_unsort.put("Sev", 3.);
		hmap_unsort.put("Fua", 1.);
		hmap_unsort.put("Duygu", 5.);
		
		HashMap<String, Double> hmap_expected = new HashMap<>();
		hmap_expected.put("Rima", 10.);
		hmap_expected.put("Duygu", 5.);
		hmap_expected.put("Sev", 3.);
		hmap_expected.put("Fua", 1.);
		
		Map<String, Double> hmap_actual = new HashMap<>();
		hmap_actual=MapUtil.entriesSortedByValues(hmap_unsort);
		
		assertEquals(hmap_actual, hmap_expected);
		
	}

}
