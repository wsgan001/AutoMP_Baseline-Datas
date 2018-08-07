package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class graph {
	
	public static Map<Integer, HashMap<Integer, List<Integer>>> G = new HashMap<Integer, HashMap<Integer, List<Integer>>>();
	
	public static void clean()
	{
		G.clear();
	}
	
	private static void addEdge(Integer sub, Integer pre, Integer obj)
	{
		if(!G.containsKey(sub))
		{
			HashMap<Integer, List<Integer>> tmp = new HashMap<Integer, List<Integer>>();
			tmp.clear();G.put(sub, tmp);
		}
		HashMap<Integer, List<Integer>> now = (HashMap<Integer, List<Integer>>) G.get(sub).clone();
		if(!now.containsKey(pre))
		{
			List<Integer> tmp = new ArrayList<Integer>();
			tmp.clear();now.put(pre, tmp);
		}
		List<Integer> node = now.get(pre);
		node.add(obj);
		now.put(pre, node);G.put(sub, now);
	}
	
	public static void addTriple(Integer sub, Integer pre, Integer obj)
	{
		addEdge(sub, pre, obj);
		addEdge(obj, pre+100000000, sub);
	}
}
