package Data_v2;

import java.math.BigInteger;
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
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import nDCG.DCG;
import Graph.graph;
import JDBCUtils.JdbcUtil;

import ESER.ESER_Algorithm.GetAnswer_SingleCenterNode;
import ESER.ESER_Algorithm.GetAnswer_MultiCenterNode;
import ESER.ESER_Algorithm.GetAnswer_EntityPathCount;

public class all {

	//private static final String origin_filename = "./datas/v2/qrels-v2_addid.txt";
	private static final String origin_filename = "./datas/v2/qrels-v2_addid_dezero.txt";
	private static graph KnowledgeGraph;
	private static FileWriter AllTestFw, FinalTestFw;
	private static Integer testcase = 0;
	private static String[] tmp = new String[25];		
	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	private static String[] testname = new String[505];
	private static Integer Steps;
	private static Double Rate;
	private static Integer Loss;
	private static Integer Model;
	private static Map<Integer, ArrayList<Integer> > CanVisit = new HashMap<Integer, ArrayList<Integer> >();
	private static Map<Integer, String> ID2Ent = new HashMap<Integer, String>();
	private static Map<String, String> Que2Lan = new HashMap<String, String>();
	
	static {
        try {
            // 加载dbinfo.properties配置文件
        	InputStream in = JdbcUtil.class.getClassLoader()
                    .getResourceAsStream("v2_Check.properties");
            Properties properties = new Properties();
            properties.load(in);
            
            //给出的example的数目
            Steps = Integer.parseInt(properties.getProperty("Steps"));
            Model= Integer.parseInt(properties.getProperty("JudgeModel"));
            Loss = Integer.parseInt(properties.getProperty("JudgeLoss"));
            Rate = Double.parseDouble(properties.getProperty("JudgeRate"));
            
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
	
	private static void ImportDataBase()
	{
		KnowledgeGraph.clean();
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
				KnowledgeGraph.addTriple(sub, pre, obj);
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
	
	private static void PreSolveFile()
	{
		Que2Lan.clear();
		Integer linecnt = 0; testcase = 0;
		try {
			//Samples
			InputStream is = new FileInputStream(origin_filename);
			Scanner sc = new Scanner(is);
			FileWriter fw = new FileWriter(new File("./datas/v2/nothing.txt"));
			String lastcase = "";
			while(sc.hasNextLine())
			{
				linecnt ++;
				String ss = sc.nextLine();
				tmp = ss.split("\t");
				//System.out.println(linecnt.toString() + "   " + tmp.length);
				//for(Integer i=0; i<tmp.length; ++i)System.out.print(tmp[i] + "  ");
				//System.out.println();
				if( !lastcase.equals(tmp[0]) )
				{
					fw.close();
					testcase ++; lastcase = tmp[0]; testname[testcase] = tmp[0];
					String outfold = "./datas/v2/cases/" + testcase.toString() + "/";
					CreateFolder(outfold);
					fw = new FileWriter(new File(outfold + "Samples.txt"));
				}
				fw.write(tmp[5] + "\t" + tmp[3] + "\r\n");
			}
			sc.close();is.close();fw.close();
			
			//Natural Languages
			is = new FileInputStream("./datas/v2/queries-v2_stopped_count.txt");
			sc = new Scanner(is);
			while(sc.hasNextLine())
			{
				String ss = sc.nextLine();
				tmp = ss.split("\t");
				Que2Lan.put(tmp[0], tmp[1]);
			}
			sc.close();is.close();
			
			//
			fw = new FileWriter(new File("./datas/v2/DataSet.txt"));
			for(Integer i=1; i<=testcase; ++i)fw.write(i.toString() + "\t" + testname[i] + "\r\n");
			fw.close();
			System.out.println("PreSolve File End!" + "\t" + mat.format(new Date()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static Map<Integer, Integer> VisitScore = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> VisitCount = new HashMap<Integer, Integer>();
	private static Set<Integer> VisitRecord = new HashSet<Integer>();
	
	private static void CaseClean()
	{
		VisitScore.clear();
		VisitCount.clear();
		VisitRecord.clear();
		CanVisit.clear();
	}
	
	private static Boolean judge(Integer nd, Integer num)
	{
		Integer score = VisitScore.get(nd);
		Integer count = VisitCount.get(nd);
		if(count < 10) return false;
		if(Model == 1) return count >= num - Loss;
		if(Model == 2) return count >= Math.ceil((double)num * Rate);
		if(Model == 3) return score >= num;
		return true;
	}
	
	private static void AddScore(Integer nd, Integer s)
	{
		if( !VisitScore.containsKey(nd) ) VisitScore.put(nd, 0);
		Integer tmp = VisitScore.get(nd);
		tmp += s;
		VisitScore.put(nd, tmp);
	}
	
	private static void AddCount(Integer nd, Integer s)
	{
		if( !VisitCount.containsKey(nd) ) VisitCount.put(nd, 0);
		Integer tmp = VisitCount.get(nd);
		tmp += s;
		VisitCount.put(nd, tmp);
	}
	
	private static void BFS(Integer bg, Integer sco)
	{
		Queue<Integer> q = new LinkedList<Integer>(); q.clear();
		Map<Integer, Integer> dis = new HashMap<Integer, Integer>(); dis.clear();
		dis.put(bg, 0); q.add(bg);
		while(!q.isEmpty())
		{
			Integer x = q.poll();
			if(!CanVisit.containsKey(x))
			{
				ArrayList<Integer> tmp = new ArrayList<Integer>(); tmp.clear();
				CanVisit.put(x, tmp);
			}
			ArrayList<Integer> vis = CanVisit.get(x);
			vis.add(bg); CanVisit.put(x, vis);
			Integer d = dis.get(x);
			VisitRecord.add(x); AddScore(x, sco); AddCount(x, 1);
			if(d >= Steps || !KnowledgeGraph.G.containsKey(x)) continue;
			HashMap<Integer, List<Integer> > outedges = KnowledgeGraph.G.get(x);
			for(Map.Entry<Integer, List<Integer>> KV : outedges.entrySet())
			 {
				 List<Integer> outnodes = KV.getValue();
				 for(Integer p : outnodes)
					 if(!dis.containsKey(p))
					 {
						 q.add(p);
						 dis.put(p, d + 1);
					 }
			 }
 		}
	}

	private static double Judge1(Integer cen, ArrayList<Integer> samp)
	{
		Integer ccnt = 0;
		Vector<Integer> relsam = new Vector<Integer>(); relsam.clear();
		for(Integer sp : samp)
		{
			ccnt ++;
			if(ccnt <= 5) relsam.add(sp);
			else break;
		}
		GetAnswer_SingleCenterNode gscr = new GetAnswer_SingleCenterNode();
		gscr.clean();
		Vector<Integer> ans = gscr.findanswer(cen, relsam, KnowledgeGraph);
		DCG dcg = new DCG(); dcg.clean();
		dcg.AddData2(ans, samp);
		return dcg.getDCG();
	}

	private static Map<String, Integer> PathCoveredCount = new HashMap<String, Integer>();
	private static Set<Integer> DFSVisted = new HashSet<Integer>();

	private static String PathHash(ArrayList<Integer> path)
	{
		BigInteger unitmul = BigInteger.valueOf(2000000000);
		BigInteger sum = BigInteger.valueOf(0);
		BigInteger mul = BigInteger.valueOf(1);
		for(Integer p : path)
		{
			sum = sum.add(mul.multiply(BigInteger.valueOf(p)));
			mul = mul.multiply(unitmul);
		}
		return sum.toString();
	}

	private static void JudgeDFS(Integer node, Integer dis, ArrayList<Integer> path, ArrayList<Integer> samp)
	{
		DFSVisted.add(node);
		if(samp.contains(node))
		{
			String has = PathHash(path);
			if(!PathCoveredCount.containsKey(has))PathCoveredCount.put(has, 0);
			Integer cnt = PathCoveredCount.get(has);
			PathCoveredCount.put(has, cnt + 1);
		}
		if(dis >= Steps || !KnowledgeGraph.G.containsKey(node))
		{
			DFSVisted.remove(node);
			return;
		}
		HashMap<Integer, List<Integer>> outedges = KnowledgeGraph.G.get(node);
		for(Map.Entry<Integer, List<Integer>> ot : outedges.entrySet())
		{
			Integer edgenum = ot.getKey();
			List<Integer> nodes = ot.getValue();
			ArrayList<Integer> tmppath = (ArrayList<Integer>) path.clone();
			tmppath.add(edgenum);
			for(Integer nd : nodes)
			{
				if(DFSVisted.contains(nd)) continue;
				JudgeDFS(nd, dis + 1, tmppath, samp);
			}
		}
		DFSVisted.remove(node);
	}

	private static double Judge2(Integer cen, ArrayList<Integer> samp)
	{
		DFSVisted.clear(); PathCoveredCount.clear();
		ArrayList<Integer> pat = new ArrayList<Integer>(); pat.clear();
		JudgeDFS(cen, 0, pat, samp);
		Integer ret = 0;
		for(Map.Entry<String, Integer> kv : PathCoveredCount.entrySet())
		{
			Integer tmp = kv.getValue();
			ret = Math.max(ret, tmp);
		}
		return (double)ret;
	}

	private static void SolveCase(Integer casnum)
	{
		Integer SampleCnt = 0;
		String outfold = "./datas/v2/cases/" + casnum.toString() + "/";
		try {
			InputStream is = new FileInputStream(outfold + "Samples.txt");
			Scanner sc = new Scanner(is);
			ArrayList<Integer> samp = new ArrayList<Integer>(); samp.clear();
			while(sc.hasNextInt())
			{
				Integer id = sc.nextInt();
				Integer sco = sc.nextInt();
				BFS(id, sco); SampleCnt ++;
				samp.add(id);
			}
			FileWriter fw = new FileWriter(new File(outfold + "Results.txt"));
			Boolean hasCent = false;
			Integer ma = 0;
			ArrayList<Integer> ret = new ArrayList<Integer>(); ret.clear();
			for(Integer nd : VisitRecord)
			{
				if( !judge(nd, SampleCnt) ) continue;
				hasCent = true;
				Integer nu = VisitCount.get(nd);
				if(nu == ma) ret.add(nd);
				if(nu > ma) { ma = nu; ret.clear(); ret.add(nd);}
				fw.write(nd.toString() + "\t" + VisitCount.get(nd).toString() + "\t" + VisitScore.get(nd).toString() + "\r\n");
			}
			fw.close();
			if( hasCent && ret.size()<=20)
			{
				AllTestFw.write(casnum.toString() + "\t" + testname[casnum] + "\t" + Que2Lan.get(testname[casnum]) + "\r\n");
				
				FileWriter ww = new FileWriter(new File(outfold + "AllCenter.txt"));
				ww.write(testname[casnum] + "\t" + Que2Lan.get(testname[casnum]) + "\r\n");
				
				double maxscore = -1;
				Integer bestcent = 0;
				
				for(Integer cen : ret)
				{
					String dbname = ID2Ent.get(cen);
					ww.write(cen.toString() + "\t" + dbname + "\r\n");
					FileWriter ff = new FileWriter(new File(outfold + cen.toString() + ".txt"));
					ff.write(cen.toString() + "\r\n");
					ArrayList<Integer> vis =  CanVisit.get(cen);
					for(Integer sp : vis)ff.write(sp.toString() + "\t");
					ff.close();

					double tmpscore = Judge2(cen, samp);
					if(tmpscore > maxscore) { maxscore = tmpscore; bestcent = cen; }
				}
				ww.write("Samples : \r\n");
				for(Integer sp : samp)ww.write(sp.toString() + "\t" + ID2Ent.get(sp) + "\r\n");
				ww.close();
				
				AllTestFw.write(bestcent.toString() + "\t" + ID2Ent.get(bestcent) + "\t" + maxscore + "\r\n");
				for(Integer sp : samp)AllTestFw.write(sp.toString() + "\t" + ID2Ent.get(sp) + "\r\n");
				AllTestFw.write("\r\n\r\n");

				FinalTestFw.write(bestcent.toString() + "\r\n");
				ArrayList<Integer> cv = CanVisit.get(bestcent);
				for(Integer nd : cv)FinalTestFw.write(nd.toString() + "\t");
				FinalTestFw.write("\r\n");

				System.out.println(casnum.toString() + "\tOK!\t" + SampleCnt.toString() + "\t" + testname[casnum] + "\t" + ret.size() + "\t" + ma.toString() + "\tBest :" + bestcent.toString());
			}
			sc.close();is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GetID2Ent();
		ImportDataBase();
		PreSolveFile();
		try {
			AllTestFw = new FileWriter(new File("./datas/v2/alltest.txt"));
			FinalTestFw = new FileWriter(new File("./datas/v2/finaltest.txt"));
			for(Integer cas = 1; cas <= testcase; ++ cas)
			{
				//System.out.println("Case : " + cas.toString() + " Begin!" + "\t" + mat.format(new Date()));
				CaseClean();
				SolveCase(cas);
				//System.out.println("Case : " + cas.toString() + " End!" + "\t" + mat.format(new Date()));
			}
			AllTestFw.close(); FinalTestFw.close();
			JdbcUtil.closeConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
