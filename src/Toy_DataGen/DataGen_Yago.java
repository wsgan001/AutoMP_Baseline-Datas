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

public class DataGen_Yago {

	private static Integer RandPathLen = 0;
	private static Integer TrainLim = 0;
    private static Integer NegaNum = 0;
	private static Integer TrainLines = 0;
    private static Integer CYLim = 0;

	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	private static String[] tmp = new String[105];
	private static Integer testcase = 0;
    private static Map<Integer, Integer> NodeType = new HashMap<Integer, Integer>();
    private static Integer MaxEntityNumber = 3480806;
    private static DecimalFormat decifm=new DecimalFormat("#.000000");
    private static FileWriter Sta_FW;

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

	private static ArrayList<Integer>[] ty = new ArrayList[15000005];

	private static void GetNodeInformation()
	{
        DecimalFormat fm=new DecimalFormat("#.000000");
		NodeType.clear();
		Integer cnt = 0;
		for(Integer i=0; i<=15000000; ++i)ty[i] = new ArrayList<Integer>();
		String sql = "SELECT * FROM onttypesinfos;";
		try {
			PreparedStatement ps = JdbcUtil.getConnection().prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Integer ent = rs.getInt("entity");
				Integer typ = rs.getInt("type");
				ty[ent].add(typ);
				cnt ++; MaxEntityNumber = Math.max(ent, MaxEntityNumber);
			}
			rs.close();ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        Map<Integer, Integer> typehash = new HashMap<Integer, Integer>(); typehash.clear();
        Integer typehashcnt = 0;

        //System.out.println(MaxEntityNumber);

        for(Integer i=1; i<=MaxEntityNumber; ++i)
		{
			Integer ret = 4832388, dep = -1;
			for(Integer p : ty[i])
			{
				Integer de = 0;
				if(depth.containsKey(p))de = depth.get(p);
				if(de > dep && de <= 2)
				{
					dep = de;
					ret = p;
				}
			}
			NodeType.put(i, ret);
			if(!typehash.containsKey(ret))
            {
                typehash.put(ret, typehashcnt);
				typehashcnt ++;
            }
		}

		CreateFolder("./datas/toys/toydata/");

        System.out.println("GetNodeInformation End!\tAll : " + cnt + "\t" + mat.format(new Date()));
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
					for(Integer i=0; i<tmp.length; ++i)
                    {
                        Integer pos=tmp[i].indexOf(':');
                        fw.write(tmp[i].substring(0,pos) + " ");
                    }
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
    private static Map<Integer, Integer> TypeHash = new HashMap<Integer, Integer>();
    private static ArrayList<Integer> Nodes = new ArrayList<Integer>();
    private static Integer NodeCnt = 0;
    private static Integer TypeCnt = 0;
    private static Map<Integer, ArrayList<Integer> > Node2Path = new HashMap<Integer, ArrayList<Integer> >();
    private static ArrayList< ArrayList<Integer> > Paths = new ArrayList< ArrayList<Integer> >();
    private static ArrayList<Integer> NegaSamples = new ArrayList<Integer>();
    private static Set<Integer> HasWalked = new HashSet<Integer>();
    private static Set<Integer> SampleTypes = new HashSet<Integer>();
    private static ArrayList<Integer> Samples = new ArrayList<Integer>();
    private static Set<Integer> AllTypes = new HashSet<Integer>();
    private static ArrayList<Integer> trainsamples = new ArrayList<Integer>();
    private static List<Integer> NegaShuf = new ArrayList<Integer>();
    private static Integer CYCnt = 0;

    private static void ClearMem()
    {
        NodeHash.clear(); Nodes.clear(); NodeCnt = 0;
        TypeHash.clear(); TypeCnt = 0; AllTypes.clear();
        Node2Path.clear(); Paths.clear();
        NegaSamples.clear(); SampleTypes.clear(); Samples.clear(); trainsamples.clear();
        NegaShuf.clear();
    }

    private static void WriteCY(Integer node, FileWriter tmpfw)
    {
        try {
            ArrayList<Integer> pathlabels = Node2Path.get( node );
            Collections.shuffle(pathlabels);
            Integer num = (pathlabels.size() + 1) / 2;
            for(Integer llb = 0; llb < num; ++llb)
            {
                Integer lb = pathlabels.get(llb);
                CYCnt ++;
                ArrayList<Integer> nl = Paths.get(lb);
                if(nl.size() <= 1)
                {
                    tmpfw.write(nl.get(0).toString() + "\t" + nl.get(0).toString() + "\t");
                    tmpfw.write(nl.get(0).toString() + " " + nl.get(0).toString() + " ");
                    tmpfw.write("\r\n"); continue;
                }
                if(CYCnt % 2 == 0)
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
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
        Paths.add((ArrayList<Integer>) pt.clone());
        if (!Node2Path.containsKey(node))
        {
            ArrayList<Integer> tmp = new ArrayList<Integer>();
            tmp.clear();
            Node2Path.put(node, tmp);
        }
        ArrayList<Integer> now = (ArrayList<Integer>) Node2Path.get(node).clone();
        Integer label = Paths.size() - 1;
        now.add(label);
        Node2Path.put(node, now);
        if(SampleTypes.contains(tp) && !NegaSamples.contains(node)) NegaSamples.add( node );

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
            Sta_FW.write(foldname + " : \r\n\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Integer Sta_Nodes = 0;
        Integer Sta_Types = 0;
        Integer Sta_Paths = 0;
        double Sta_PathperEnt = 0;
        double Sta_PathperEnt_Train = 0;
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
			    Sta_FW.write("Case " + i.toString() + " : ");

				InputStream is = new FileInputStream("./datas/toys/toyin/" + i.toString() + ".sam");
				Scanner sc = new Scanner(is);
				cent = sc.nextInt();

				CreateFolder(foldn + "/dbpedia/");
				FileWriter CYfw = new FileWriter(foldn + "/dbpedia/subpathsSaveFile");

				Boolean SamplesFlag = true;
				while(sc.hasNextInt()) {
                    Integer p = sc.nextInt();
                    Samples.add(p);
                    SampleTypes.add(NodeType.get(p));
                }
				sc.close();is.close();

                CreateFolder(foldn + "dbpedia.splits/");
                CreateFolder(foldn + "dbpedia.trainModels/");

                ArrayList<Integer> pt = new ArrayList<Integer>(); pt.clear();
                HasWalked.clear(); GetSubGraph(cent, 0, pt);

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

                for(Integer p : NegaSamples) if( p != cent && !Samples.contains(p) ) NegaShuf.add(p);
                for(Integer p : Samples) if(!NodeHash.containsKey(p)) { Samples.remove(p); SamplesFlag = false; }
                if(!SamplesFlag) System.out.println("Fuck!!!");

                //Statistics
                Sta_Nodes += NodeCnt;
                Sta_Paths += Paths.size();
                Sta_Types += TypeCnt;
                Sta_PathperEnt += (double)Paths.size() / (double)NegaSamples.size();
                Sta_FW.write("Nodes : " + NodeCnt.toString() + "  Types : " + TypeCnt.toString() + "  Paths : "+ Paths.size() + "\r\n");
                Sta_FW.write("Paths per Entity : " + decifm.format((double)Paths.size() / (double)NegaSamples.size()));

                //debug
                //System.out.println("Sampls : " + "  " + Samples.size() + "\tPaths : " + Paths.size()+ "\t" + "NegaShufs : " + NegaShuf.size() + "\t" + mat.format(new Date()));
                //if(Samples.contains(cent))System.out.println("Samples Contains Center!");
                //System.out.println("Cent : " + cent.toString());System.out.print("Samples :");for(Integer p : Samples)System.out.print(" " + p.toString());System.out.println();

                Integer tmp_Nodes_Train = 0;
                Integer tmp_Paths_Train = 0;

                for(Integer trainnum = 1; trainnum <= TrainLim; trainnum ++)
                {
                    Integer CYCnt = 0;
                    trainsamples.clear();
                    for(Integer k = 0; k < trainnum; ++k) trainsamples.add( Samples.get( k ) );

                    Integer everysample = TrainLines / trainnum;
                    String trainfold = foldn + "dbpedia.splits/train." + trainnum.toString() + "/";
                    CreateFolder(trainfold);
                    CreateFolder(foldn + "dbpedia.trainModels/train." + trainnum.toString() + "/");
                    fw = new FileWriter(foldn + "dbpedia.trainModels/train." + trainnum.toString() + "/nothing.txt");
                    fw.write("Hello World!\r\n"); fw.close();

                    //train
                    fw = new FileWriter(trainfold + "train_relation_1");
                    Integer lines = 0, label = 0, tmplb = 0;
                    while(lines < TrainLines)
                    {
                        lines ++; tmplb ++;
                        if(lines % everysample == 1 || everysample == 1)
                        {
                            Integer tmplabel = label;
                            label++;
                            label = Math.min(label, trainnum);
                            if(label > tmplabel)
                            {
                                Collections.shuffle(NegaSamples);
                                tmplb = 0;
                                if(trainnum <= 5)
                                {
                                    tmp_Nodes_Train ++;
                                    //System.out.print("Train Sample : " + trainsamples.get( label - 1 ));
                                    //if(!Node2Path.containsKey(trainsamples.get( label - 1 ))) System.out.print("  NodeHash : " + NodeHash.get( trainsamples.get( label - 1 ) ));
                                    //System.out.println();
                                    tmp_Paths_Train += Node2Path.get( trainsamples.get( label - 1 ) ).size();
                                }
                                WriteCY( trainsamples.get( label - 1 ), CYfw );
                            }
                        }
                        Integer nu = 0;
                        if(tmplb >= NegaSamples.size()) nu = Nodes.get( rd.nextInt(NodeCnt) ); else nu = NegaSamples.get(tmplb);
                        if(trainnum <= 5)
                        {
                            tmp_Nodes_Train ++;
                            tmp_Paths_Train += Node2Path.get( nu ).size();
                        }
                        WriteCY(nu, CYfw);
                        fw.write(NodeHash.get(cent).toString() + "\t" + NodeHash.get( trainsamples.get(label - 1)).toString() + "\t" + NodeHash.get( nu ).toString() + "\r\n");
                    }
                    fw.close();

                    //test
                    String testfold = foldn + "dbpedia.splits/test/";
                    CreateFolder(testfold);
                    fw = new FileWriter(testfold + "test_relation_" + trainnum.toString());
                    fw.write(NodeHash.get(cent).toString());
                    for(Integer p : Samples)
                        if(!trainsamples.contains(p))
                        {
                            WriteCY(p, CYfw);
                            fw.write("\t" + NodeHash.get(p).toString());
                        }
                    Integer negnum = Math.min(NegaNum, NegaShuf.size());
                    Collections.shuffle(NegaShuf);
                    //for(Integer p : NegaShuf) System.out.print(p.toString() + " ");
                    for(Integer labe = 0; labe < negnum; labe ++)
                    {
                        CYCnt ++;
                        Integer nu = NegaShuf.get(labe);
                        fw.write("\t" + NodeHash.get( nu ).toString());
                        WriteCY(nu, CYfw);
                        //System.out.println(labe.toString() + " " + nu.toString() + "  " + pathlabels.size());
                    }
                    fw.close();

                    //ideal
                    String idealfold = foldn + "dbpedia.splits/ideal/";
                    CreateFolder(idealfold);
                    fw = new FileWriter(idealfold + "ideal_relation_" + trainnum.toString());
                    fw.write(NodeHash.get(cent).toString());
                    for(Integer p : Samples) if(!trainsamples.contains(p))fw.write("\t" + NodeHash.get(p).toString());
                    fw.close();
                }

                Sta_FW.write("  Train Paths per Entity : " + decifm.format((double)tmp_Paths_Train / (double)tmp_Nodes_Train) + "\r\n\r\n");
                Sta_PathperEnt_Train += (double)tmp_Paths_Train / (double)tmp_Nodes_Train;

                FileWriter HashFW = new FileWriter(foldn + "Hash2Node.txt");
                for(Integer id = 0; id < NodeCnt; ++id) HashFW.write(id.toString() + "\t" + Nodes.get(id) + "\r\n");
                HashFW.close();

                CYfw.close();
            } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
			//System.out.println("Case : " + i.toString() + "\tEnd!" + "\t" + mat.format(new Date()));
		}
        try {
            Sta_FW.write("All : \r\n");
            Sta_FW.write("Nodes : " + decifm.format((double)Sta_Nodes / (double)testcase) + "\r\n");
            Sta_FW.write("Types : " + decifm.format((double)Sta_Types / (double)testcase) + "\r\n");
            Sta_FW.write("Paths : " + decifm.format((double)Sta_Paths / (double)testcase) + "\r\n");
            Sta_FW.write("Paths per Entitiy : " + decifm.format(Sta_PathperEnt / (double)testcase) + "\r\n");
            Sta_FW.write("Train Paths of pre 5 : " + decifm.format(Sta_PathperEnt_Train / (double)testcase) + "\r\n");
            Sta_FW.write("\r\n\r\n\r\n");
        } catch (IOException e) {
            e.printStackTrace();
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

		CreateFolder("./datas/toys/toydata/");

        try {
            Sta_FW = new FileWriter(new File("./datas/toys/toydata/Yago_Statistic.txt"));
            //yago
            Doit("yago_11b");
            Doit("yago_21o");
            Doit("yago_22b");
            Sta_FW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

		JdbcUtil.closeConnection();
	}

}
