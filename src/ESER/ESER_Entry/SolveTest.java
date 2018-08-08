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
import org.omg.SendingContext.RunTime;

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
        try {
            FileWriter timew = new FileWriter(new File("./datas/Test/RunTime.txt"));
            for(Integer queryid = 1; queryid <= QueryNumber; ++ queryid)
            {
                System.out.print("Test " + queryid.toString());
                timew.write("Test " + queryid.toString());
                long startTime=System.currentTimeMillis();   //获取开始时间
                Integer centernode = 0;
                Vector<Integer> relativenode = new Vector<Integer>();
                relativenode.clear();
                String filename = "./datas/Test/" + queryid.toString() + ".in";
                InputStream is = new FileInputStream(filename);
                Scanner sc = new Scanner(is);
                centernode = sc.nextInt();
                while(sc.hasNextInt())relativenode.add(sc.nextInt());
                sc.close();is.close();
                GetAnswer_SingleCenterNode ga = new GetAnswer_SingleCenterNode();
                //GetAnswer_MultiCenterNode ga = new GetAnswer_MultiCenterNode();
                ga.clean();
                Vector<Integer> answer = ga.findanswer(centernode, relativenode, g);
                filename = "./datas/Test/" + queryid.toString() + ".out";
                File opf = new File(filename);
                FileWriter wr;
                wr = new FileWriter(opf);
                for(Integer p : answer)wr.write(p.toString() + "\r\n");
                wr.close();
                long endTime=System.currentTimeMillis(); //获取结束时间
                System.out.println("  程序运行时间： "+(endTime-startTime)+"ms");
                timew.write(" " + (endTime-startTime) + "\r\n");
            }
            timew.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

}
