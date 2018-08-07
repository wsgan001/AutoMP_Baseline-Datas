package Toy_DataGen;

import Graph.graph;
import JDBCUtils.JdbcUtil;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataGen_PreVersion {

	private static Integer BFSLim = 0;
	private static Integer NSN = 0;	
	private static Integer RandPathLen = 0;
	private static Integer TrainLim = 0;
    private static Integer NegaNum = 0;

    private static Map<Integer, HashSet<Integer>> ParChi = new HashMap<Integer, HashSet<Integer>>();
	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	private static String[] tmp = new String[105];
	private static Integer testcase = 0;
	private static Map<Integer, Integer> depth = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> degree = new HashMap<Integer, Integer>();
	private static Set<Integer> types = new HashSet<Integer>();
    private static Map<Integer, String> ID2Ent = new HashMap<Integer, String>();
	private static Map<Integer, ArrayList<Integer> > gg = new HashMap<Integer, ArrayList<Integer> >();
	
	static {
        try {
            // 加载dbinfo.properties配置文件
        	InputStream in = JdbcUtil.class.getClassLoader()
                    .getResourceAsStream("Toy_DataGen.properties");
            Properties properties = new Properties();
            properties.load(in);
            
            //给出的example的数目
            BFSLim = Integer.parseInt(properties.getProperty("SubGraphStepLimit"));
            NSN = Integer.parseInt(properties.getProperty("NegativeSampleNumber"));
            RandPathLen = Integer.parseInt(properties.getProperty("RandomPathLength"));
            TrainLim = Integer.parseInt(properties.getProperty("TrainSamplesLimit"));
            NegaNum = Integer.parseInt(properties.getProperty("NegativeNumber"));
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

    private static void GetID2Ent()
    {
        ID2Ent.clear();
        Map<String,Integer> uri2id = new HashMap<String, Integer>(); uri2id.clear();
        String sql1 = "SELECT * FROM mapping;";
        String sql2 = "SELECT * FROM labelinfos;";
        try {
            Integer cnt = 0;
            PreparedStatement ps1 = JdbcUtil.getConnection().prepareStatement(sql1);
            PreparedStatement ps2 = JdbcUtil.getConnection().prepareStatement(sql2);
            ResultSet r1 = ps1.executeQuery();
            ResultSet r2 = ps2.executeQuery();
            while(r1.next())
            {
                Integer id = r1.getInt("id");
                String uri = r1.getString("uri");
                uri2id.put(uri, id);
            }
            while(r2.next())
            {
                String uri = r2.getString("entity");
                String label = r2.getString("label");
                ID2Ent.put(uri2id.get(uri), label);
                cnt ++;
            }
            r1.close(); r2.close();
            ps1.close(); ps2.close();
            System.out.println("GetID2Ent End!\tAll : " + cnt + "\t" + mat.format(new Date()));
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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
	
	private static ArrayList<Integer>[] ty = new ArrayList[3500005];
	private static Map<Integer, Integer> NodeType = new HashMap<Integer, Integer>();
	
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
			Integer ret = 3481453, dep = -1;
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
	
	public static void PreSolveFile1()
	{
		Integer linecnt = 0; testcase = 0;
		String str = "";
		try {
			InputStream is = new FileInputStream("./datas/toys/toyin/toy.in");
			Scanner sc = new Scanner(is);
			FileWriter fw = new FileWriter("./datas/toys/toyin/toy.out");
			while( sc.hasNextLine() )
			{
				str = sc.nextLine();
				linecnt ++;
				if(linecnt % 5 == 2 || linecnt % 5 == 0)continue;
				if(linecnt % 5 == 1)
				{
					testcase ++;
					String outfile = "./datas/toys/toyin/" + testcase.toString() + ".sam";
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

	public static void PreSolveFile2()
	{
		Integer linecnt = 0; testcase = 0;
		String str = "";
		try {
			InputStream is = new FileInputStream("./datas/toys/toyin/toy.in");
			Scanner sc = new Scanner(is);
			FileWriter fw = new FileWriter("./datas/toys/toyin/toy.out");
			while( sc.hasNextLine() )
			{
				str = sc.nextLine();
				linecnt ++;
				if(linecnt % 2 == 1)
				{
					testcase ++;
					String outfile = "./datas/toys/toyin/" + testcase.toString() + ".sam";
					fw = new FileWriter(new File(outfile));
					fw.write(str + "\r\n");
					continue;
				}
				if(str.length() > 0)
				{
					tmp = str.split("\t");
					for(Integer i=0; i<tmp.length; ++i)fw.write(tmp[i] + " ");
				}
				fw.close();
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

	private static Map<Integer, Integer> NodeHash = new HashMap<Integer, Integer>();
	private static ArrayList<Integer> Nodes = new ArrayList<Integer>();
	private static Integer NodeCnt = 0;
	
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
	
	private static Map<Integer, ArrayList<Integer>> roads = new HashMap<Integer, ArrayList<Integer>>();
	
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
						 
						 AddEdge(NodeHash.get(x), NodeHash.get(p));
						 try {
							fw.write(NodeHash.get(x).toString() + "\t" + NodeHash.get(p) + "\r\n");
							fw.write(NodeHash.get(p).toString() + "\t" + NodeHash.get(x) + "\r\n");
						 } catch (IOException e) {
					// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
					 if((Integer)fat.get(p) != x)
					 {
						 AddEdge(NodeHash.get(x), NodeHash.get(p));
						 try {
							fw.write(NodeHash.get(x).toString() + "\t" + NodeHash.get(p) + "\r\n");
							fw.write(NodeHash.get(p).toString() + "\t" + NodeHash.get(x) + "\r\n");
						 } catch (IOException e) {
					// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
				 }
			 }
		 }
	}
	
	private static Set<Integer> HasWalked = new HashSet<Integer>();
	private static ArrayList<Integer> NodeList = new ArrayList<Integer>();
	
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
		Integer cnt = 20;
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
	
	private static void SolveEveryCase()
	{
        try {
            CreateFolder("./datas/toys/toydata/");
            FileWriter countfw = new FileWriter(new File("./datas/toys/toydata/count.txt"));
            countfw.write(testcase.toString() + "\r\n");
            countfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		for(Integer i=1; i<=testcase; ++i)
		{
			System.out.println("Case : " + i.toString() + "  Begin!" + "\t" + mat.format(new Date()));
			Integer cent = 0;
			String foldn = "./datas/toys/toydata/" + i.toString() + "/";
			CreateFolder(foldn);
			ArrayList<Integer> Samples = new ArrayList<Integer>(); Samples.clear();
			Set<Integer> AllTypes = new HashSet<Integer>(); AllTypes.clear();
			Random rd = new Random();
			try {
				InputStream is = new FileInputStream("./datas/toys/toyin/" + i.toString() + ".sam");
				Scanner sc = new Scanner(is);
				cent = sc.nextInt();

				//Clear
				NodeCnt = 0; NodeHash.clear(); Nodes.clear();

				//Edge
				CreateFolder(foldn + "dbpedia/");
				FileWriter fw = new FileWriter(foldn + "dbpedia/" + "graph.edge");
				GetSubGraph(cent, fw);fw.close();
				//System.out.println("Edge End!" + "\t" + mat.format(new Date()));

				//Nodes
				fw = new FileWriter(foldn + "dbpedia/" + "graph.node");
				for(Integer nd=0; nd<Nodes.size(); ++nd)
				{
				    Integer node = Nodes.get(nd);
					fw.write(nd.toString() + "\t" + NodeType.get( node ).toString() + "\t" + ID2Ent.get( Nodes.get(nd) ) + "\r\n");
					AllTypes.add( NodeType.get(Nodes.get(nd)) );
				}
				fw.close();
				//System.out.println("Node End!" + "\t" + mat.format(new Date()));

				//Samples
                Set<Integer> SampleTypes = new HashSet<Integer>(); SampleTypes.clear();
				Boolean SamplesFlag = true;
				while(sc.hasNextInt())
				{
					Integer p = sc.nextInt();
					if(!NodeHash.containsKey(p)) {SamplesFlag = false; continue;}
					p = NodeHash.get(p);
					Samples.add(p);
					SampleTypes.add( NodeType.get(p) );
				}
				if(SamplesFlag)System.out.println("All Samples in SubGraph!");else System.out.println("Exits Sample not in SubGraph!");
				//System.out.println("Sampls End!" + "  " + Samples.size() + "\t" + mat.format(new Date()));
				sc.close();is.close(); cent = 0;

                CreateFolder(foldn + "dbpedia.splits/");
                CreateFolder(foldn + "dbpedia.trainModels/");

                Set<Integer> trainsamples = new HashSet<Integer>(); trainsamples.clear();

                //train + test + ideal
                ArrayList<Integer> allnegative = new ArrayList<Integer>(); allnegative.clear();
                ArrayList<Integer> tmpallnegative = new ArrayList<Integer>(); tmpallnegative.clear();
                for(Integer p = 0; p < NodeCnt; ++ p)
                    if( (!Samples.contains(p)) && SampleTypes.contains( NodeType.get( Nodes.get(p) ) )) tmpallnegative.add(p);
                Integer negnum = Math.min(tmpallnegative.size(), NegaNum);
                while(allnegative.size() < negnum)
                {
                    Integer p = rd.nextInt(tmpallnegative.size());
                    while( allnegative.contains( tmpallnegative.get( p ) ) ) p = rd.nextInt(tmpallnegative.size());
                    allnegative.add( tmpallnegative.get( p ) );
                }
                Integer negativenum = Math.min(NSN, allnegative.size());
                Integer fucktrain = Math.min(Samples.size() - 2, TrainLim);

                System.out.println("Samples : " + Samples.size() + "\tNegative : " + allnegative.size());

                for(Integer trainnum = 1; trainnum <= fucktrain; trainnum ++)
                {
                    trainsamples.clear();
                    while(trainsamples.size() < trainnum)
                    {
                        Integer tt = rd.nextInt(Samples.size());
                        if(!trainsamples.contains( Samples.get(tt) )) trainsamples.add( Samples.get(tt) );
                    }
                    Integer trainpairs = negativenum * trainnum;
                    String trainfold = foldn + "dbpedia.splits/train." + trainpairs.toString() + "/";
                    CreateFolder(trainfold);
                    CreateFolder(foldn + "dbpedia.trainModels/train." + trainpairs.toString() + "/");
                    fw = new FileWriter(foldn + "dbpedia.trainModels/train." + trainpairs.toString() + "/nothing.txt");
                    fw.write("Hello World!\r\n"); fw.close();

                    //train
                    fw = new FileWriter(trainfold + "train_relation_1");
                    for(Integer x : trainsamples)
                    {
                        if(x == cent) continue;
                        Integer cnt = negativenum;
                        Set<Integer> negativesamples = new HashSet<Integer>();
                        negativesamples.clear();
                        while(cnt > 0)
                        {
                            cnt --;
                            Integer p = rd.nextInt(allnegative.size());
                            while( negativesamples.contains(p) ) p = rd.nextInt(allnegative.size());
                            negativesamples.add(p);
                        }
                        for(Integer y : negativesamples)fw.write(cent.toString() + "\t" + x.toString() + "\t" + allnegative.get(y).toString() + "\r\n");
                    }
                    fw.close();

                    //test
                    String testfold = foldn + "dbpedia.splits/test/";
                    CreateFolder(testfold);
                    fw = new FileWriter(testfold + "test_relation_" + trainnum.toString());
                    fw.write(cent.toString());
                    for(Integer p = 0; p<Nodes.size(); ++p)
                    {
                        if(p == cent || trainsamples.contains(p)) continue;
                        if(Samples.contains(p) || allnegative.contains(p)) fw.write("\t" + p.toString());
                    }
                    fw.close();

                    //ideal
                    String idealfold = foldn + "dbpedia.splits/ideal/";
                    CreateFolder(idealfold);
                    fw = new FileWriter(idealfold + "ideal_relation_" + trainnum.toString());
                    fw.write(cent.toString());
                    for(Integer p : Samples) if(!trainsamples.contains(p))fw.write("\t" + p.toString());
                    fw.close();
                }


                //Caiyang
                fw = new FileWriter(foldn + "/dbpedia/subpathsSaveFile");
                for(Integer p = 0; p<Nodes.size(); ++p)
                {
                    if(allnegative.contains(p) || Samples.contains(p) )
                    {
                        Boolean flag = false;
                        Integer cou = 30;
                        while(cou > 0)
                        {
                            cou --;
                            HasWalked.clear();NodeList.clear();
                            ArrayList<Integer> pt = new ArrayList<Integer>(); pt.clear();
                            if(FindPath(p, cent, pt, 0) > 0) { WriteCY(NodeList, cent, p, fw); flag = true; }
                        }
                        if(! flag) WriteCY(roads.get(p), cent, p, fw);
                    }
                }
                fw.close();

                //Config
                FileWriter configfw = new FileWriter(new File(foldn + "config.txt"));
                Integer dimen = (AllTypes.size() + 1) * 2;
                configfw.write("[param]\r\n");
                configfw.write("dataset_name = dbpedia\r\n");
                configfw.write("batch_size = 32\r\n");
                configfw.write("word_dimension = " + dimen.toString() + "\r\n");
                configfw.write("max_ epochs = 64\r\n");
                configfw.close();
            } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			System.out.println("Case : " + i.toString() + "\tEnd!" + "\t" + mat.format(new Date()));
		}
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        GetID2Ent();
		ImportDataBase();
		ReadOntology();
		GetNodeType();
		PreSolveFile2();
		SolveEveryCase();
		JdbcUtil.closeConnection();
	}

}
