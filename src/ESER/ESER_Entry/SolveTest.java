package ESER.ESER_Entry;

import JDBCUtils.JdbcUtil;
import Graph.graph;

import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Vector;

import ESER.ESER_Algorithm.GetAnswer_SingleCenterNode;
import ESER.ESER_Algorithm.GetAnswer_MultiCenterNode;
import ESER.ESER_Algorithm.GetAnswer_EntityPathCount;

public class SolveTest {

	public static graph g;
	public static Integer QueryNumber = 0;
	private static SimpleDateFormat mat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	private static void ImportDataBase()
	{
		g.clean();
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
				g.addTriple(sub, pre, obj);
				cnt ++;
				//if(cnt % 1000000 == 0)System.out.println("Import DataBase :\t" + cnt + "\t" + mat.format(new Date()));
			}
			rs.close();ps.close();JdbcUtil.closeConnection();
			System.out.println("Import DataBase End!\tAll : " + cnt + "\t" + mat.format(new Date()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(Integer testcase) {
		// TODO Auto-generated method stub
		ImportDataBase();
		//TestGraph();
		QueryNumber = testcase;
		/*
		BigInteger EntCnt1 = BigInteger.valueOf(0), EntCnt2 = BigInteger.valueOf(0), EntCnt3 = BigInteger.valueOf(0), EntCnt4 = BigInteger.valueOf(0);
		String CentersFn = "./datas/Test/Centers.txt";
		try {
			InputStream is = new FileInputStream(CentersFn);
			Scanner sc = new Scanner(is);
			Integer cnt = 0;
			while(sc.hasNextInt())
			{
				cnt ++;
				GetAnswer_EntityPathCount epc = new GetAnswer_EntityPathCount();
				Integer ct = sc.nextInt();
				epc.clean();epc.count(ct, 4, g);
				EntCnt1 = EntCnt1.add( epc.dis[1] );
				EntCnt2 = EntCnt2.add( epc.dis[2] );
				EntCnt3 = EntCnt3.add( epc.dis[3] );
				EntCnt4 = EntCnt4.add( epc.dis[4] );
				System.out.println(ct + " : " + epc.dis[1].toString() + " " + epc.dis[2].toString() + " " + epc.dis[3].toString() + " " + epc.dis[4].toString());
			}
			sc.close();is.close();
			System.out.println("EntityCnt1 : " + EntCnt1.toString() + " / " + cnt);
			System.out.println("EntityCnt2 : " + EntCnt2.toString() + " / " + cnt);
			System.out.println("EntityCnt3 : " + EntCnt3.toString() + " / " + cnt);
			System.out.println("EntityCnt4 : " + EntCnt4.toString() + " / " + cnt);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		for(Integer queryid = 1; queryid <= QueryNumber; ++ queryid)
		{
			System.out.println("Test " + queryid.toString() + " Begin!\t" + mat.format(new Date()));
			Integer centernode = 0;
			Vector<Integer> relativenode = new Vector<Integer>();
			relativenode.clear();
			String filename = "./datas/Test/" + queryid.toString() + ".in";
			try {
				InputStream is = new FileInputStream(filename);
				Scanner sc = new Scanner(is);
				centernode = sc.nextInt();
				while(sc.hasNextInt())relativenode.add(sc.nextInt());
				sc.close();is.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//GetAnswer_SingleCenterNode ga = new GetAnswer_SingleCenterNode();
			GetAnswer_MultiCenterNode ga = new GetAnswer_MultiCenterNode();
			ga.clean();
			Vector<Integer> answer = ga.findanswer(centernode, relativenode, g);
			filename = "./datas/Test/" + queryid.toString() + ".out";
			File opf = new File(filename);
			FileWriter wr;
			try {
				wr = new FileWriter(opf);
				for(Integer p : answer)wr.write(p.toString() + "\r\n");
				wr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Test " + queryid.toString() + " End!\t" + mat.format(new Date()));
		}
		
	}

}
