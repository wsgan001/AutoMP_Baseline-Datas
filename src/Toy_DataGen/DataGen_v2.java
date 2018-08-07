package Toy_DataGen;

import Graph.graph;
import JDBCUtils.JdbcUtil;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataGen_v2 {

	private static Integer RandPathLen = 0;
	private static Integer TrainLim = 0;
    private static Integer NegaNum = 0;
	private static Integer TrainLines = 0;
    private static Integer CYLim = 0;

    private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    private static String[] tmp = new String[105];
    private static Integer testcase = 0;
    private static Map<Integer, Integer> NodeType = new HashMap<Integer, Integer>();
    private static final Integer MaxEntityNumber = 3480806;
    private static DecimalFormat decifm=new DecimalFormat("#.000000");
	
	static {
        try {
            // 加载dbinfo.properties配置文件
        	InputStream in = JdbcUtil.class.getClassLoader()
                    .getResourceAsStream("Toy_DataGen.properties");
            Properties properties = new Properties();
            properties.load(in);
            
            //给出的example的数目
            RandPathLen = Integer.parseInt(properties.getProperty("RandomPathLength"));
            TrainLim = Integer.parseInt(properties.getProperty("TrainSamplesLimit"));
            NegaNum = Integer.parseInt(properties.getProperty("NegativeNumber"));
            TrainLines = Integer.parseInt(properties.getProperty("TrainLines"));
            CYLim = Integer.parseInt(properties.getProperty("CaiYangCnt"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private static void CreateFolder(String add)
	{
        File dir = new File(add);
        if (dir.exists()) return; else dir.mkdirs();
	}

    private static Map<Integer, HashSet<Integer>> ParChi = new HashMap<Integer, HashSet<Integer>>();
    private static Map<Integer, Integer> depth = new HashMap<Integer, Integer>();
    private static Map<Integer, Integer> degree = new HashMap<Integer, Integer>();
    private static Set<Integer> types = new HashSet<Integer>();

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

	private static void GetNodeInformation()
	{
        DecimalFormat fm=new DecimalFormat("#.000000");
		NodeType.clear();
		Integer cnt = 0;
		for(Integer i=0; i<=3500000; ++i)ty[i] = new ArrayList<Integer>();
		String sql = "SELECT * FROM typesinfos;";
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

        for(Integer i=1; i<=MaxEntityNumber; ++i)
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

        //System.out.println("GetNodeInformation End!\tAll : " + cnt + "\t" + mat.format(new Date()));
	}

	public static void PreSolveFile(String filename)
	{
		Integer linecnt = 0; testcase = 0;
		String str = "";
		CreateFolder("./datas/toys/toyin/");
		try {
			InputStream is = new FileInputStream("./datas/toys/OriginData/" + filename + ".txt");
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
    private static Integer TypeCnt = 0;
	private static Set<Integer> HasWalked = new HashSet<Integer>();
	private static ArrayList<Integer> NodeList = new ArrayList<Integer>();
	private static Set<Integer> SampleTypes = new HashSet<Integer>();
    private static ArrayList<Integer> Samples = new ArrayList<Integer>();
    private static ArrayList<Integer> trainsamples = new ArrayList<Integer>();
    private static Set<Integer> nega = new HashSet<Integer>();
    private static Set<Integer> alrcho = new HashSet<Integer>();
    private static Map<Integer, Integer> TypeHash = new HashMap<Integer, Integer>();
    private static Set<Integer> AlreadyOutput = new HashSet<Integer>();
    private static Map<Integer, ArrayList<Integer> > Node2Path = new HashMap<Integer, ArrayList<Integer> >();
    private static ArrayList< ArrayList<Integer> > Paths = new ArrayList< ArrayList<Integer> >();

	private static void ClearMem()
    {
        NodeHash.clear(); Nodes.clear(); NodeCnt = 0;
        SampleTypes.clear(); Samples.clear();
        TypeHash.clear(); TypeCnt = 0;
        AlreadyOutput.clear(); HasWalked.clear();
        Node2Path.clear(); Paths.clear();
    }

    private static void GetSubGraph(Integer node, Integer len, ArrayList<Integer> path)
    {
        HasWalked.add(node);
        if(!NodeHash.containsKey(node))
        {
            NodeHash.put(node, NodeCnt);
            Nodes.add(node); NodeCnt ++;
        }
        ArrayList<Integer> pt = (ArrayList<Integer>) path.clone();
        pt.add(NodeHash.get( node ));
        Integer tp = NodeType.get(node);
        if(!TypeHash.containsKey(tp))
        {
            TypeHash.put(tp, TypeCnt);
            TypeCnt ++;
        }
        if (Samples.contains(node))
        {
            Paths.add((ArrayList<Integer>) pt.clone());
            if (!Node2Path.containsKey(node)) {
                ArrayList<Integer> tmp = new ArrayList<Integer>();
                tmp.clear();
                Node2Path.put(node, tmp);
            }
            ArrayList<Integer> now = (ArrayList<Integer>) Node2Path.get(node).clone();
            Integer label = Paths.size() - 1;
            now.add(label);
            Node2Path.put(node, now);
        }

        if(!graph.G.containsKey(node) || len >= RandPathLen)
        {
            HasWalked.remove(node);
            return;
        }

        HashMap<Integer, List<Integer>> outnodes = graph.G.get(node);

        for(Map.Entry<Integer, List<Integer>> kv : outnodes.entrySet())
        {
            List<Integer> otnd = kv.getValue();
            for(Integer nd : otnd)
                if(!HasWalked.contains(nd))GetSubGraph(nd, len + 1, pt);
        }

        HasWalked.remove(node);
    }

	private static Integer FindPath(Integer now, ArrayList<Integer> PT, Integer len)
	{
		ArrayList<Integer> pt = (ArrayList<Integer>) PT.clone();
		if(!NodeHash.containsKey(now))
        {
            NodeHash.put(now, NodeCnt);
            Nodes.add(now);
            NodeCnt ++;
        }
        Integer tp=NodeType.get(now);
        if(!TypeHash.containsKey(tp))
        {
            TypeHash.put(tp, TypeCnt);
            TypeCnt ++;
        }
		pt.add( NodeHash.get(now) ); HasWalked.add(now);
		if( SampleTypes.contains(tp) && len > 0) { NodeList = (ArrayList<Integer>) pt.clone(); return now;}
		if(len >= RandPathLen)  return -1;
		HashMap<Integer, List<Integer>> outs = graph.G.get(now);
        ArrayList<Integer> otnd = new ArrayList<Integer>(); otnd.clear();
        for(Map.Entry<Integer, List<Integer>> kv : outs.entrySet())
        {
            List<Integer> outnode = kv.getValue();
            for(Integer p : outnode) if(!HasWalked.contains(p))otnd.add(p);
        }
		Random rd = new Random();
		Integer sz = otnd.size();
		if(sz <= 0)return -1;
		Integer label = rd.nextInt(sz);
		return FindPath(otnd.get(label), pt, len + 1);
	}

    private static void WriteCY(ArrayList<Integer> nl, FileWriter tmpfw, Integer reverse)
    {
        try {
            if(reverse == 0)
            {
                tmpfw.write(nl.get(nl.size() - 1).toString() + "\t" + nl.get(0).toString() + "\t");
                for(Integer id = nl.size() - 1; id >= 0; -- id)tmpfw.write(nl.get(id).toString() + " ");
                tmpfw.write("\r\n");
            }
            else
            {
                tmpfw.write(nl.get(0).toString() + "\t" + nl.get(nl.size() - 1).toString() + "\t");
                for(Integer id = 0; id < nl.size(); ++ id)tmpfw.write(nl.get(id).toString() + " ");
                tmpfw.write("\r\n");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void GetDegAndEntro(Integer node, FileWriter fw)
    {
        Integer deg = 0;
        double entro = 0;

        Map<Integer, Integer> typecnt = new HashMap<Integer, Integer>(); typecnt.clear();

        if(graph.G.containsKey(node))
        {
            HashMap<Integer, List<Integer>> outnodes = graph.G.get(node);

            for (Map.Entry<Integer, List<Integer>> kv : outnodes.entrySet())
            {
                List<Integer> otnd = kv.getValue();
                for (Integer nd : otnd)
                {
                    if (!NodeHash.containsKey(nd)) continue;
                    Integer ndtp = NodeType.get(nd);
                    deg++;
                    if (!typecnt.containsKey(ndtp)) typecnt.put(ndtp, 0);
                    Integer now = typecnt.get(ndtp);
                    typecnt.put(ndtp, now + 1);
                }
            }
        }

        try {
            fw.write(deg.toString() + " ");
            for(Map.Entry<Integer, Integer> kv : typecnt.entrySet())
            {
                Integer tp = kv.getKey();
                Integer nu = kv.getValue();
                fw.write(TypeHash.get(tp).toString() + ":" + nu.toString() + " ");
                double p = (double)nu / (double)deg;
                entro -= p * Math.log(p);
            }
            fw.write(decifm.format(entro));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private static void SolveEveryCase(String foldname)
	{
        try {
            CreateFolder("./datas/toys/toydata/" + foldname + "/");
            FileWriter countfw = new FileWriter(new File("./datas/toys/toydata/" + foldname + "/count.txt"));
            countfw.write(testcase.toString() + "\r\n");
            countfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		for(Integer i=1; i<=testcase; ++i)
		{
			System.out.println("Case : " + i.toString() + "  Begin!" + "\t" + mat.format(new Date()));
			Integer cent = 0;
			FileWriter fw = null;
			String foldn = "./datas/toys/toydata/" + foldname + "/" + i.toString() + "/";
			CreateFolder(foldn);

			ClearMem();

			Random rd = new Random();
			try {
				InputStream is = new FileInputStream("./datas/toys/toyin/" + i.toString() + ".sam");
				Scanner sc = new Scanner(is);
				cent = sc.nextInt();

				CreateFolder(foldn + "/dbpedia/");
				FileWriter CYfw = new FileWriter(foldn + "/dbpedia/subpathsSaveFile");

				//Samples
				Boolean SamplesFlag = true;
				while(sc.hasNextInt())
				{
					Integer p = sc.nextInt();
					Samples.add(p);
					SampleTypes.add( NodeType.get(p) );
				}
				//System.out.println("Sampls End!" + "  " + Samples.size() + "\t" + mat.format(new Date()));
				sc.close();is.close();

                ArrayList<Integer> pth = new ArrayList<Integer>(); pth.clear();
                GetSubGraph(cent, 0, pth);

                for(Integer p : Samples)
                    if(!NodeHash.containsKey(p))Samples.remove(p);

                CreateFolder(foldn + "dbpedia.splits/");
                CreateFolder(foldn + "dbpedia.trainModels/");

                Integer CYCnt = 0;

                for(Integer trainnum = 1; trainnum <= TrainLim; trainnum ++)
                {
                    CYCnt = 0;
                    trainsamples.clear();
                    while(trainsamples.size() < trainnum)
                    {
                        Integer tt = rd.nextInt(Samples.size());
                        if(!trainsamples.contains( Samples.get(tt) )) trainsamples.add( Samples.get(tt) );
                    }

                    Integer everysample = TrainLines / trainnum;
                    String trainfold = foldn + "dbpedia.splits/train." + trainnum.toString() + "/";
                    CreateFolder(trainfold);
                    CreateFolder(foldn + "dbpedia.trainModels/train." + trainnum.toString() + "/");
                    fw = new FileWriter(foldn + "dbpedia.trainModels/train." + trainnum.toString() + "/nothing.txt");
                    fw.write("Hello World!\r\n"); fw.close();

                    pth.clear();
                    //train
                    fw = new FileWriter(trainfold + "train_relation_1");
                    nega.clear();
                    Integer lines = 0, label = 0;
                    while(lines < TrainLines)
                    {
                        lines ++;
                        if(lines % everysample == 1 || everysample == 1)
                        {
                            Integer tmplabel = label;
                            label++;
                            label = Math.min(label, trainnum);
                            if(label > tmplabel) nega.clear();
                        }
                        while( true )
                        {
                            Integer nu = 0;
                            while (nu <= 0)
                            {
                                CYCnt++;
                                HasWalked.clear();
                                nu = FindPath(cent, pth, 0);
                            }
                            if(Samples.contains(nu))AlreadyOutput.add(nu);
                            WriteCY(NodeList, CYfw, CYCnt % 2);
                            if(nega.contains(nu)) continue;
                            fw.write(NodeHash.get(cent).toString() + "\t" + NodeHash.get(trainsamples.get(label - 1)).toString() + "\t" + NodeHash.get(nu).toString() + "\r\n");
                            nega.add(nu); break;
                        }
                    }
                    //System.out.print("Train     ");
                    fw.close();

                    //test
                    String testfold = foldn + "dbpedia.splits/test/";
                    CreateFolder(testfold);
                    fw = new FileWriter(testfold + "test_relation_" + trainnum.toString());
                    fw.write(NodeHash.get(cent).toString());
                    for(Integer p : Samples) if(!trainsamples.contains(p))fw.write("\t" + NodeHash.get(p).toString());
                    alrcho.clear();
                    while(CYCnt < CYLim / TrainLim)
                    {
                        CYCnt ++; HasWalked.clear();
                        Integer nu = FindPath(cent, pth, 0);
                        if(nu <= 0)continue;
                        WriteCY(NodeList, CYfw, CYCnt % 2);
                        if(Samples.contains(nu)) AlreadyOutput.add(nu);
                        if(!alrcho.contains(nu) && alrcho.size() < NegaNum) { fw.write("\t" +NodeHash.get(nu).toString()); alrcho.add(nu); }
                    }
                    //System.out.println("Nega Samp : " + alrcho.size());
                    //System.out.print("Test     ");
                    fw.close();

                    //ideal
                    String idealfold = foldn + "dbpedia.splits/ideal/";
                    CreateFolder(idealfold);
                    fw = new FileWriter(idealfold + "ideal_relation_" + trainnum.toString());
                    fw.write(NodeHash.get(cent).toString());
                    for(Integer p : Samples) if(!trainsamples.contains(p))fw.write("\t" + NodeHash.get(p).toString());
                    //System.out.println("Ideal     ");
                    fw.close();
                }

                //addition samples paths
                for(Integer p : Samples)
                {
                    if(AlreadyOutput.contains(p)) continue;
                    ArrayList<Integer> labels = Node2Path.get(p);
                    for(Integer id : labels)
                    {
                        CYCnt ++;
                        NodeList = Paths.get(id);
                        WriteCY(NodeList, CYfw, CYCnt % 2);
                    }
                }
                CYfw.close();

                FileWriter NodeFW = new FileWriter(new File( foldn + "/dbpedia/nodefeatures" ));
                Integer dimen = 2 * (TypeCnt + 1);
                NodeFW.write(NodeCnt.toString() + " " + dimen.toString() + "\r\n");
                for(Integer nodeid = 0; nodeid < NodeCnt; nodeid ++)
                {
                    Integer node = Nodes.get(nodeid);
                    NodeFW.write(nodeid.toString() + " " + TypeHash.get( NodeType.get( node ) ).toString() + " ");
                    GetDegAndEntro(node, NodeFW);
                    NodeFW.write("\r\n");
                }
                NodeFW.close();
            } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			//System.out.println("Case : " + i.toString() + "\tEnd!" + "\t" + mat.format(new Date()));
		}
    }

    private static void Doit(String fn)
    {
        System.out.println(fn + "\tBegin!" + "\t" + mat.format(new Date()));
        PreSolveFile(fn);
        SolveEveryCase(fn);
        System.out.println(fn + "\tEnd!" + "\t" + mat.format(new Date()));
    }

	public static void main(String[] args) {
		ImportDataBase();
		ReadOntology();
		GetNodeInformation();

		Doit("v2");

		JdbcUtil.closeConnection();
	}

}
