package TreeGeneration;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class FindQuartile {

	public static double findOutliers(double[] data) {
		DescriptiveStatistics da = new DescriptiveStatistics(data);
		final double q3 = da.getPercentile(75);
		final double q1 = da.getPercentile(25);
		double iqr = q3 - q1;

		System.out.println("Q3 = "+q3+" "+" Q1 = "+q1 );
		System.out.println("Inter quartl "+iqr);
		//System.out.println("iqr*1.5 = "+ iqr*1.5);
		final double t = q3+iqr*1.5;
		System.out.println("Thresold "+t);
		
		System.out.println("mean " + da.getMean());
		System.out.println("min " + da.getMin());
		System.out.println("max " + da.getMax());
		System.out.println("median " + da.getPercentile(50));
		return t;
	}

}
