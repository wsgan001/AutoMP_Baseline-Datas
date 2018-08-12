package ESER.ESER_Entry;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import ESER.ESER_Entry.CheckAnswer;
import ESER.ESER_Entry.PreTest;
import ESER.ESER_Entry.SolveTest;
import JDBCUtils.JdbcUtil;

public class TestAll {

	private static  Integer QueryNumberLimit = 0;
	
	static {
        try {
            // 加载dbinfo.properties配置文件
        	InputStream in = JdbcUtil.class.getClassLoader()
                    .getResourceAsStream("ESER.properties");
            Properties properties = new Properties();
            properties.load(in);
            
            //给出的example的数目
            QueryNumberLimit = Integer.parseInt(properties.getProperty("QueryNumberLimit"));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> names = new ArrayList<String>(); names.clear();
//		names.add("dbpedia_11b");
//		names.add("dbpedia_12b");
//		names.add("dbpedia_21b");
//		names.add("dbpedia_22b");
//		names.add("dbpedia_21o");
//		names.add("yago_11b");
//		names.add("yago_22b");
//		names.add("yago_21o");
//		names.add("yago_21b");
		names.add("v2");
		PreTest pre = new PreTest();
		SolveTest sol = new SolveTest();
		//CheckAnswer che = new CheckAnswer();
		ArrayList<Integer> cases = pre.main(QueryNumberLimit, names);
		sol.main(QueryNumberLimit, names, cases);
		//che.main(QueryNumberLimit, names, cases);
	}

}
