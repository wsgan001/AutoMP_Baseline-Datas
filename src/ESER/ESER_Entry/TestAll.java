package ESER.ESER_Entry;

import java.io.IOException;
import java.io.InputStream;
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
		PreTest pre = new PreTest();
		SolveTest sol = new SolveTest();
		CheckAnswer che = new CheckAnswer();
		Integer testcase = pre.main(QueryNumberLimit);
		sol.main(testcase);
		che.main(testcase);
	}

}
