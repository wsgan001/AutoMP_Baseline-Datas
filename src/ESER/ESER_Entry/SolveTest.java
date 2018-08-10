package ESER.ESER_Entry;

import JDBCUtils.JdbcUtil;
import Graph.graph;

import java.util.ArrayList;
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

	public static void main(Integer SampleNumber, ArrayList<String> names, ArrayList<Integer> cases) {
		ImportDataBase();
        try {
            for(Integer trainid = 0; trainid < names.size(); ++ trainid)
            {
                Integer casenum = cases.get(trainid);
                FileWriter timew = new FileWriter(new File("./datas/ESER/" + names.get(trainid).toString() + "/Time.txt"));
                FileWriter answ = new FileWriter(new File("./datas/ESER/" + names.get(trainid).toString() + "/Answer.txt"));
                for(Integer caseid = 1; caseid <= casenum; ++ caseid)
                {
                    System.out.println("Case : " + names.get(trainid) + "\t" + caseid.toString());
                    String foldnm = "./datas/ESER/" + names.get(trainid).toString() + "/" + caseid.toString() + "/";
                    for(Integer queryid = 1; queryid <= SampleNumber; ++queryid)
                    {
                        long startTime=System.currentTimeMillis();   //获取开始时间
                        String InputName = foldnm + "Query_" + queryid.toString() + ".txt";
                        Scanner sc = new Scanner(new FileInputStream(InputName));
                        Integer centernode = sc.nextInt();
                        Vector<Integer> relativenode = new Vector<Integer>(); relativenode.clear();
                        while(sc.hasNextInt())
                        {
                            Integer p = sc.nextInt();
                            relativenode.add(p);
                        }
                        sc.close();
                        GetAnswer_SingleCenterNode ga = new GetAnswer_SingleCenterNode();
                        //GetAnswer_MultiCenterNode ga = new GetAnswer_MultiCenterNode();
                        ga.clean();
                        Vector<Integer> answer = ga.findanswer(centernode, relativenode, g);
                        for(Integer ans : answer) answ.write(ans.toString() + "\t");
                        answ.write("\r\n");
                        long endTime=System.currentTimeMillis(); //获取结束时间
                        System.out.println("Query : " + queryid.toString() + "  程序运行时间： "+ (endTime-startTime) + "ms");
                        timew.write((endTime-startTime) + "\r\n");
                    }
                }
                answ.close(); timew.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

}
