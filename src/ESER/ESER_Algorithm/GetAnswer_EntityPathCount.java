package ESER.ESER_Algorithm;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;

import Graph.graph;

public class GetAnswer_EntityPathCount {
	
	public static Integer CenterNode = 0;
	public static Integer CountLimit = 0;
	public static Queue<Integer> q = new LinkedList<Integer>();
	public static Map<Integer, Integer> dist = new HashMap<Integer, Integer>();
	public static BigInteger[] dis = new BigInteger[5];
	public static graph g;
	
	public static void clean()
	{
		dis[0]=dis[1]=dis[2]=dis[3]=dis[4]=BigInteger.valueOf(0);
		q.clear();dist.clear();
	}
	
	private static void addInfo(Integer centernode)
	{
		CenterNode = centernode;
	}
	
	private static void BFS()
	{
		dist.put(CenterNode, 0);
		q.add(CenterNode);
		while( !q.isEmpty() )
		{
			Integer x = q.poll();
			Integer d = dist.get(x);
			dis[d] = dis[d].add(BigInteger.valueOf(1));
			if( d >= CountLimit ) continue;
			if(!g.G.containsKey(x))continue;
			HashMap<Integer, List<Integer>> outedges = g.G.get(x);
			for(Map.Entry<Integer, List<Integer>> ent : outedges.entrySet())
			{
				List<Integer> outnodes = ent.getValue();
				for(Integer p : outnodes)
				{
					if(dist.containsKey(p))continue;
					dist.put(p, d + 1);
					q.add(p);
				}
			}
		}
	}
	
	public static void count(Integer centernode, Integer lim, graph gg)
	{
		Vector<Integer> ret = new Vector<Integer>();
		ret.clear();g = gg;
		addInfo(centernode);
		CountLimit = lim;
		BFS();
	}
}
