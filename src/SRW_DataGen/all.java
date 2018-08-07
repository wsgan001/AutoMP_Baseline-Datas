package SRW_DataGen;

import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import Graph.graph;
import JDBCUtils.JdbcUtil;

public class all {

	public static Integer BFSLim = 0;
	public static Integer NSN = 0;	
	public static Integer RandPathLen = 0;
	public static double trainrate = 0.0;
	
	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	public static String[] tmp = new String[105];
	public static Integer testcase = 0;
	public static Map<Integer, Integer> depth = new HashMap<Integer, Integer>();
	public static Map<Integer, Integer> degree = new HashMap<Integer, Integer>();
	public static Set<Integer> types = new HashSet<Integer>();
	
	public static Map<Integer, ArrayList<Integer> > gg = new HashMap<Integer, ArrayList<Integer> >();
	
	static {
        try {
            // 加载dbinfo.properties配置文件
        	InputStream in = JdbcUtil.class.getClassLoader()
                    .getResourceAsStream("SRW_DataGen.properties");
            Properties properties = new Properties();
            properties.load(in);
            
            //给出的example的数目
            BFSLim = Integer.parseInt(properties.getProperty("SubGraphStepLimit"));
            NSN = Integer.parseInt(properties.getProperty("NegativeSampleNumber"));
            RandPathLen = Integer.parseInt(properties.getProperty("RandomPathLength"));
            trainrate = Double.parseDouble(properties.getProperty("TrainSamplesRate"));
            //trainrate = 1.0;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private static void CreateFolder(String add)
	{
        File dir = new File(add);
        if (dir.exists()) return; else dir.mkdirs();
	}
	
	public static Map<Integer, HashSet<Integer>> ParChi = new HashMap<Integer, HashSet<Integer>>();
	
	private static void ReadOntology()
	{
		ParChi.clear();depth.clear();types.clear();degree.clear();
		String sql = "SELECT * FROM ontinfos;";
		try {
			PreparedStatement ps = JdbcUtil.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Integer par = rs.getInt("parent");
				Integer chi = rs.getInt("child");
				types.add(par);types.add(chi);
				if(!degree.containsKey(chi))degree.put(chi, 0);
				Integer cc = degree.get(chi);
				degree.put(chi, cc + 1);
				if(!ParChi.containsKey(par))
				{
					HashSet<Integer> ss = new HashSet<Integer>();
					ss.clear();
					ParChi.put(par, ss);
				}
				HashSet<Integer> tmp = ParChi.get(par);
				tmp.add(chi);
				ParChi.put(par, tmp);
			}
			rs.close();ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Queue<Integer> q = new LinkedList<Integer>(); q.clear();
		for(Integer nd : types) if(!degree.containsKey(nd)) { q.offer(nd); depth.put(nd, 1); }
		while( !q.isEmpty() )
		{
			Integer x = q.poll();
			Integer d = depth.get(x);
			if( !ParChi.containsKey(x) ) continue;
			HashSet<Integer> outtype = ParChi.get(x);
			for(Integer p : outtype)
			{
				Integer cc = degree.get(p);
				cc --; degree.put(p, cc);
				if( cc == 0 )
				{
					q.add(p);
					depth.put(p, d + 1);
				}
			}
		}
		System.out.println("Read Ontology End!" + "\t" + mat.format(new Date()));
	}
	
	private static void ImportDataBase()
	{
		graph.clean();
		String sql = "SELECT * FROM objtriples;";
		try {
			Integer cnt = 0;
			PreparedStatement ps = JdbcUtil.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Integer pre = rs.getInt("predicate");
				Integer sub = rs.getInt("subject");
				Integer obj = rs.getInt("object");
				graph.addTriple(sub, pre, obj);
				cnt ++;
				//if(cnt % 1000000 == 0)System.out.println("Import DataBase :\t" + cnt + "\t" + mat.format(new Date()));
			}
			rs.close();ps.close();
			System.out.println("Import DataBase End!\tAll : " + cnt + "\t" + mat.format(new Date()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<Integer>[] ty = new ArrayList[3500005];
	public static Map<Integer, Integer> NodeType = new HashMap<Integer, Integer>();
	
	private static void GetNodeType()
	{
		NodeType.clear();
		Integer cnt = 0;
		for(Integer i=0; i<=3500000; ++i)ty[i] = new ArrayList<Integer>();
		String sql = "SELECT * FROM onttypesinfos;";
		try {
			PreparedStatement ps = JdbcUtil.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Integer ent = rs.getInt("entity");
				Integer typ = rs.getInt("type");
				ty[ent].add(typ);
				cnt ++;
			}
			rs.close();ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Integer i=0; i<=3500000; ++i)
		{
			Integer ret = 0, dep = -1;
			for(Integer p : ty[i])
			{
				Integer de = 0;
				if(depth.containsKey(p))de = depth.get(p);
				if(de > dep)
				{
					dep = de;
					ret = p;
				}
			}
			NodeType.put(i, ret);
		}
		
		System.out.println("GetNodeType End!\tAll : " + cnt + "\t" + mat.format(new Date()));
	}
	
	private static void PreSolveFile()
	{
		Integer linecnt = 0; testcase = 0;
		String str = "";
		try {
			InputStream is = new FileInputStream("./datas/srws/srwin/srw.in");
			Scanner sc = new Scanner(is);
			FileWriter fw = new FileWriter("./datas/srws/srwin/srw.out");
			while( sc.hasNextLine() )
			{
				str = sc.nextLine();
				linecnt ++;
				if(linecnt % 5 == 2 || linecnt % 5 == 0)continue;
				if(linecnt % 5 == 1)
				{
					testcase ++;
					String outfile = "./datas/srws/srwin/" + testcase.toString() + ".sam";
					fw = new FileWriter(new File(outfile));
					fw.write(str + "\r\n");
					continue;
				}
				if(str.length() > 0)
				{
					tmp = str.split("\t");
					for(Integer i=0; i<tmp.length; ++i)fw.write(tmp[i] + " ");
				}
				if(linecnt % 5 == 4)fw.close();
			}
			sc.close();is.close();
			System.out.println("PreSolve File End!" + "\t" + mat.format(new Date()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Map<Integer, Integer> NodeHash = new HashMap<Integer, Integer>();
	public static ArrayList<Integer> Nodes = new ArrayList<Integer>();
	public static Integer NodeCnt = 0;
	public static Integer EdgeCnt = 0;
	public static Map<Integer, Integer> EdgeHash = new HashMap<Integer, Integer>();
	
	private static void AddEdge(Integer x, Integer y)
	{
		if(!gg.containsKey(x))
		{
			ArrayList<Integer> ss = new ArrayList<Integer>();
			ss.clear();
			gg.put(x, ss);
		}
		if(!gg.containsKey(y))
		{
			ArrayList<Integer> ss = new ArrayList<Integer>();
			ss.clear();
			gg.put(y, ss);
		}
		ArrayList<Integer> tmpx = gg.get(x); tmpx.add(y); gg.put(x, tmpx);
		ArrayList<Integer> tmpy = gg.get(y); tmpy.add(x); gg.put(y, tmpy);
	}
	
	public static Map<Integer, ArrayList<Integer>> roads = new HashMap<Integer, ArrayList<Integer>>();
	
	private static void GetSubGraph(Integer cent, FileWriter fw)
	{
		 gg.clear();roads.clear();
		 ArrayList<Integer> tmp = new ArrayList<Integer>(); tmp.clear(); tmp.add(0); roads.put(0, tmp);
		 Queue<Integer> q = new LinkedList<Integer>(); q.clear();
		 Nodes.add(cent); NodeHash.put(cent, 0); NodeCnt ++;
		 Map<Integer, Integer> dis = new HashMap<Integer, Integer>();dis.clear();
		 Map<Integer, Integer> fat = new HashMap<Integer, Integer>();fat.clear();
		 dis.put(cent, 0); q.offer(cent);fat.put(0, 0);
		 while( !q.isEmpty() )
		 {
			 Integer x = q.poll();
			 Integer d = dis.get(x);
			 if(!graph.G.containsKey(x) || d >= BFSLim)continue;
			 HashMap<Integer, List<Integer>> outedges = graph.G.get(x);
			 for(Map.Entry<Integer, List<Integer>> KV : outedges.entrySet())
			 {
				 Boolean ReverseEdge = false;
				 Integer edgetype = KV.getKey();
				 if(edgetype > 100000000) { edgetype -= 100000000; ReverseEdge = true; }
				 if(!EdgeHash.containsKey(edgetype))
				 {
					 EdgeHash.put(edgetype, EdgeCnt);
					 EdgeCnt ++;
				 }
				 edgetype = EdgeHash.get(edgetype); edgetype ++;
				 List<Integer> outnodes = KV.getValue();
				 for(Integer p : outnodes)
				 {
					 if(!NodeHash.containsKey(p))
					 {
						 NodeHash.put(p, NodeCnt);
						 NodeCnt ++;
						 Nodes.add(p);
						 dis.put(p, d + 1);
						 q.offer(p);fat.put(p, x);
						 ArrayList<Integer> last = (ArrayList<Integer>) roads.get( NodeHash.get(x) ).clone();
						 last.add( NodeHash.get(p) ); roads.put( NodeHash.get(p) , last);
						 Integer idx = NodeHash.get(x), idp = NodeHash.get(p);
						 AddEdge(idx, idp);
						 try {
							idx ++; idp ++; 
							if(!ReverseEdge)
								fw.write(idx.toString() + " " + idp.toString() + " " + edgetype.toString() + "\r\n");
							else 
								fw.write(idp.toString() + " " + idx.toString() + " " + edgetype.toString() + "\r\n");
						 } catch (IOException e) {
					// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
					 if((Integer)fat.get(p) != x)
					 {
						 Integer idx = NodeHash.get(x), idp = NodeHash.get(p);
						 AddEdge(idx, idp);
						 try {
							idx ++; idp ++; 
							if(!ReverseEdge)
								fw.write(idx.toString() + " " + idp.toString() + " " + edgetype.toString() + "\r\n");
							else 
								fw.write(idp.toString() + " " + idx.toString() + " " + edgetype.toString() + "\r\n");
						 } catch (IOException e) {
					// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
				 }
			 }
		 }
	}
	/*
	public static Set<Integer> HasWalked = new HashSet<Integer>();
	public static ArrayList<Integer> NodeList = new ArrayList<Integer>();
	
	private static Integer FindPath(Integer now, Integer ed, ArrayList<Integer> PT, Integer len)
	{
		ArrayList<Integer> pt = (ArrayList<Integer>) PT.clone();
		pt.add(now);HasWalked.add(now);
		if(now == ed) { NodeList = (ArrayList<Integer>) pt.clone(); return 1;}
		if(len >= RandPathLen)  return -1;
		ArrayList<Integer> otnd = gg.get(now);
		Random rd = new Random();
		Integer sz = otnd.size();
		//System.out.println(now + "  " + ed + "  " + len + "  " + sz);
		Integer label = rd.nextInt(sz);
		Integer cnt = 500;
		while(cnt > 0 && HasWalked.contains( otnd.get(label) ))
		{
			label = rd.nextInt(sz);
			cnt --;
		}
		if(cnt == 0 )return -1;
		return FindPath(otnd.get(label), ed, pt, len + 1);
	}
	
	private static void WriteCY(ArrayList<Integer> nl, Integer st, Integer ed, FileWriter fw)
	{
		try {
			if(nl.get(0).equals(st))
			{
				fw.write(st.toString() + "\t" + ed.toString() + "\t");
				for(Integer pp : nl)fw.write(pp.toString() + " "); fw.write("\r\n");
				fw.write(ed.toString() + "\t" + st.toString() + "\t");
				for(Integer id = nl.size() - 1; id >= 0; -- id)fw.write(nl.get(id).toString() + " "); fw.write("\r\n");
			} 
			else
			{
				fw.write(ed.toString() + "\t" + st.toString() + "\t");
				for(Integer pp : nl)fw.write(pp.toString() + " "); fw.write("\r\n");
				fw.write(st.toString() + "\t" + ed.toString() + "\t");
				for(Integer id = nl.size() - 1; id >= 0; -- id)fw.write(nl.get(id).toString() + " "); fw.write("\r\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	private static void SolveEveryCase()
	{	
		for(Integer i=1; i<=testcase; ++i)
		{
			System.out.println("Case : " + i.toString() + "  Begin!" + "\t" + mat.format(new Date()));
			Integer cent = 0;
			String foldn = "./datas/srws/srwdata/" + i.toString() + "/";
			CreateFolder(foldn);
			Set<Integer> Samples = new HashSet<Integer>();
			List<Integer> SampleOrder = new ArrayList<Integer>();
			Samples.clear();SampleOrder.clear();
			try {
				InputStream is = new FileInputStream("./datas/srws/srwin/" + i.toString() + ".sam");
				Scanner sc = new Scanner(is);
				cent = sc.nextInt();
				
				//Clear
				NodeCnt = 0; NodeHash.clear(); Nodes.clear(); 
				EdgeCnt = 0; EdgeHash.clear();
				
				//Edge
				FileWriter fw = new FileWriter(foldn +  "Graph.txt");
				GetSubGraph(cent, fw);fw.close();
				System.out.println("Graph End!" + "\t" + mat.format(new Date()));
				
				//Samples
				Boolean flag = true;
				while(sc.hasNextInt())
				{
					Integer p = sc.nextInt();
					if(!NodeHash.containsKey(p)) {flag = false; continue;}
					p = NodeHash.get(p);
					Samples.add(p);SampleOrder.add(p);
				}
				if(flag)System.out.println("All Samples in SubGraph!");else System.out.println("Exits Sample not in SubGraph!");
				cent = 0;
				System.out.println("Sampls End!" + "  " + Samples.size() + "\t" + mat.format(new Date()));
				sc.close();is.close();
				
				//Scalar
				fw = new FileWriter(foldn +  "scalar.txt");
				fw.write("source_node=1\r\n");
				fw.write("num_nodes=" + NodeCnt.toString() + "\r\n");
				fw.write("num_features=" + EdgeCnt.toString() + "\r\n");
				fw.close();
				
				//Positive_link
				fw = new FileWriter(foldn +  "positive_link");
				for(Integer p : Samples)
				{
					Integer pp = p + 1;
					fw.write(pp.toString() + "\r\n");
				}
				fw.close();
				
				//Negative_link
				ArrayList<Integer> nega = new ArrayList<Integer>(); nega.clear();
				Set<Integer> negachoice = new HashSet<Integer>(); negachoice.clear();
				Random rd = new Random();
				fw = new FileWriter(foldn +  "negative_link");
				for(Integer p = 1; p<NodeCnt; ++p)
					if( (!Samples.contains(p)) && NodeType.get( Nodes.get(p) ).equals(NodeType.get( Nodes.get(cent) )))
					nega.add(p);
				Integer neganum = Math.min(nega.size(), 5 * Samples.size());
				while(neganum > 0)
				{
					neganum --;
					Integer p = rd.nextInt(nega.size());
					while( negachoice.contains(p) ) p = rd.nextInt(nega.size());
					negachoice.add(p);
				}
				for(Integer x : negachoice)
				{
					Integer pp = nega.get(x) + 1;
					fw.write(pp.toString() + "\r\n");
				}
				fw.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			//Caiyang
			try {
				FileWriter fw = new FileWriter(foldn + "/dbpedia/paths.txt");
				//FileWriter fww = new FileWriter(foldn + "pathss.txt");		
				for(Integer p = 0; p<Nodes.size(); ++p)
				{
					if(p == cent) continue;
					if(negativesamples.contains(p) || trainsamples.contains(p) || NodeType.get( Nodes.get(p) ).equals(NodeType.get( Nodes.get(cent) )))
					{
						Boolean flag = false;
						Integer cou = 100;
						while(cou > 0)
						{
							cou --;
							//System.out.println(cou);
							HasWalked.clear();NodeList.clear();
							ArrayList<Integer> pt = new ArrayList<Integer>(); pt.clear();
							if(FindPath(p, cent, pt, 0) > 0) { WriteCY(NodeList, cent, p, fw); flag = true; }
						}
						if(! flag) WriteCY(roads.get(p), cent, p, fw);
					}
				}
				fw.close();//fww.close();
				System.out.println("Caiyang End!" + "\t" + mat.format(new Date()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			System.out.println("Case : " + i.toString() + "End!" + "\t" + mat.format(new Date()));
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ImportDataBase();
		ReadOntology();
		GetNodeType();
		PreSolveFile();
		SolveEveryCase();
		JdbcUtil.closeConnection();
	}

}
