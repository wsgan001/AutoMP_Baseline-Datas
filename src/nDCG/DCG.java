package nDCG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class DCG {

	private static Vector<Integer> result = new Vector<Integer>();
	private static Map<Integer, Integer> StdAns = new HashMap<Integer, Integer>();
	
	public static void clean()
	{
		result.clear();
		StdAns.clear();
	}
	
	public static void AddData1(Vector<Integer> res, Vector<Integer> std)
	{
		result = (Vector<Integer>) res.clone();
		Integer len = std.size();
		for(Integer i=0; i<std.size(); ++i)StdAns.put(std.get(i), len-i);
	}
	
	public static void AddData2(Vector<Integer> res, ArrayList<Integer> samp)
	{
		result = (Vector<Integer>) res.clone();
		for(Integer p : samp)StdAns.put(p, 1);
	}
	
	public static double getDCG()
	{
		Integer mul = 1;
		double ret = 0, cs = Math.log(2.0);
		for(Integer i : result)
		{
			double s = 0;
			double t = Math.log(mul + 1);
			if(StdAns.containsKey(i)) s = (double)StdAns.get(i);
			ret += s / t;
			mul = mul * 2;
		}
		return ret;
	}

}
