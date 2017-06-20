

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import org.junit.Test;

import TreeGeneration.EvaluateHeuristicFunctions;
import util.PrecisionAndRecallCalculator;

public class test_PrecisionAndRecall {

	@Test
	public void test() throws Exception 
	{
		
		Map<String, HashMap<String, Double>> hmap_test1 = new HashMap<>();
		HashMap<String, Double> hmap_tempTest1 = new HashMap<String, Double>();
		
		hmap_tempTest1.put("computer",1.);
		hmap_tempTest1.put("money",1.);
		hmap_tempTest1.put("love",1.);
		hmap_tempTest1.put("red",1.);
		hmap_tempTest1.put("green",1.);
		
		hmap_test1.put("Entry1__1", hmap_tempTest1);
		hmap_test1.put("Entry1__2", new HashMap<String, Double>());
		hmap_test1.put("Entry1__3", new HashMap<String, Double>());
		hmap_test1.put("Entry1__4", new HashMap<String, Double>());
		hmap_test1.put("Entry1__5", new HashMap<String, Double>());
		hmap_test1.put("Entry1__6", new HashMap<String, Double>());
		hmap_test1.put("Entry1__7", new HashMap<String, Double>());
		
		HashMap<String, Double> hmap_tempTest2 = new HashMap<String, Double>();
		hmap_tempTest2.put("money",1.);
		
		
		hmap_test1.put("Entry2__1", hmap_tempTest2);
		hmap_test1.put("Entry2__2", new HashMap<String, Double>());
		hmap_test1.put("Entry2__3", new HashMap<String, Double>());
		hmap_test1.put("Entry2__4", new HashMap<String, Double>());
		hmap_test1.put("Entry2__5", new HashMap<String, Double>());
		hmap_test1.put("Entry2__6", new HashMap<String, Double>());
		hmap_test1.put("Entry2__7", new HashMap<String, Double>());
		
		Map<String, HashSet<String>> hmap_goal = new HashMap<>();
		HashSet<String> hmap_tempgoal = new HashSet<String>();

		hmap_tempgoal.add("computer");
		hmap_tempgoal.add("money");
		hmap_goal.put("Entry1", hmap_tempgoal);
		hmap_goal.put("Entry2", hmap_tempgoal);
		
//		double fmeasure = 0.7241379310344827;
//		Map<String, Double> hmap_expected = new HashMap<>();
//		hmap_expected.put("Fmeasure__1", fmeasure);
//		hmap_expected.put("Fmeasure__2", 0.);
//		hmap_expected.put("Fmeasure__3", 0.);
//		hmap_expected.put("Fmeasure__4", 0.);
//		hmap_expected.put("Fmeasure__5", 0.);
//		hmap_expected.put("Fmeasure__6", .0);
//		hmap_expected.put("Fmeasure__7", .0);
		
//		EvaluateHeuristicFunctions eva = new EvaluateHeuristicFunctions();
//		Map<String, Double> hmap_actualFmeasure = new HashMap<>(eva.calculatePreRcallFscore_levelBased(hmap_test1, hmap_goal));
//		assertEquals(hmap_actualFmeasure,hmap_expected);
		
		HashSet<String> hset_test1 = new HashSet<>();
		hset_test1.add("computer");
		hset_test1.add("money");
		hset_test1.add("love");
		hset_test1.add("red");
		hset_test1.add("green");
		
		HashSet<String> hset_test2 = new HashSet<>();
		hset_test2.add("money");
		
		HashSet<String> hset_goal = new HashSet<>();
		hset_goal.add("computer");
		hset_goal.add("money");
		
		HashMap<String, Double> hmap_expected1 = new HashMap<>();
		hmap_expected1.put("Precision", 0.4);
		hmap_expected1.put("Recall", 1.);
		
		HashMap<String, Double> hmap_expected2 = new HashMap<>();
		hmap_expected2.put("Precision", 1.);
		hmap_expected2.put("Recall", 0.5);
		
		
		Map<String, Double> hmap_actual = new HashMap<>(PrecisionAndRecallCalculator.calculatePrecisionRecall(hset_goal, hset_test1));
		assertEquals(hmap_actual,hmap_expected1);
//	
		hmap_actual = new HashMap<>(PrecisionAndRecallCalculator.calculatePrecisionRecall(hset_goal, hset_test2));
		assertEquals(hmap_actual,hmap_expected2);
//		
		double fmeasure = 0.7241379310344827;
		double db_avgPrecision= (hmap_expected1.get("Precision")+hmap_expected2.get("Precision"))/2;
		double db_avgRecall= (hmap_expected1.get("Recall")+hmap_expected2.get("Recall"))/2;
		double actual = PrecisionAndRecallCalculator.FmeasureCalculate(db_avgPrecision, db_avgRecall) ;
		assertEquals(fmeasure, actual,0.0001);
		
		//0,61904761904761904761904761904762 wrong
		//0,72413793103448275862068965517241 true
		
	}

}
