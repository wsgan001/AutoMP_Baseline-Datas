package ESER.ESER_Entry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PreTest {
	
	//private static final String filename = "./datas/Test/Test.in";
	private static String[] tmp = new String[105];
	private static Set<Integer> Centers =  new HashSet<Integer>();

	private static Integer PreSolve1(String filename)
    {
        Integer linecnt = 0;
        Integer testcase = 0;
        String str = "";
        try {
            InputStream is = new FileInputStream(filename);
            Scanner sc = new Scanner(is);
            FileWriter fw = null;
            fw = new FileWriter("./datas/Test/nothing.in");
            while( sc.hasNextLine() )
            {
                str = sc.nextLine();
                linecnt ++;
                if(linecnt % 5 == 2 || linecnt % 5 == 0)continue;
                if(linecnt % 5 == 1)
                {
                    testcase ++;
                    String outfile = "./datas/Test/" + testcase.toString() + ".sam";
                    fw = new FileWriter(new File(outfile));
                    fw.write(str + "\r\n");
                    Centers.add(Integer.parseInt(str));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testcase;
    }

    private static Integer PreSolve2(String filename)
    {
        Integer linecnt = 0;
        Integer testcase = 0;
        String str = "";
        try {
            InputStream is = new FileInputStream(filename);
            Scanner sc = new Scanner(is);
            FileWriter fw = null;
            fw = new FileWriter("./datas/Test/nothing.in");
            while( sc.hasNextLine() )
            {
                str = sc.nextLine();
                linecnt ++;
                if(linecnt % 2 == 1)
                {
                    testcase ++;
                    String outfile = "./datas/Test/" + testcase.toString() + ".sam";
                    fw = new FileWriter(new File(outfile));
                    fw.write(str + "\r\n");
                    Centers.add(Integer.parseInt(str));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testcase;
    }

	public static Integer main(Integer SampleNumber) {
		// TODO Auto-generated method stub
		//Integer testcase = PreSolve1("./datas/Test/Test.in");
        Integer testcase = PreSolve2("./datas/Test/Test.in");
		String str = "";
		try {
			FileWriter fww = new FileWriter("./datas/Test/Centers.txt");
			for(Integer p : Centers)fww.write(p.toString() + "\r\n");
			fww.close();
			InputStream is = null;
			Scanner sc = null;
			FileWriter fw = null;
			for(Integer test = 1; test <= testcase; ++ test)
			{
				is = new FileInputStream("./datas/Test/" + test.toString() + ".sam");
				sc = new Scanner(is);
				fw = new FileWriter("./datas/Test/" + test.toString() + ".in");
				str = sc.nextLine();
				fw.write(str + "\r\n");
				str = sc.nextLine();
				tmp = str.split(" ");
				Integer x = Math.min(SampleNumber, tmp.length);
				for(Integer i=0; i<x; ++i)fw.write(tmp[i] + "   ");
				fw.close();sc.close();is.close();
				fw = new FileWriter("./datas/Test/" + test.toString() + ".ans");
				for(Integer i=x; i<tmp.length; ++i)fw.write(tmp[i] + " ");
				fw.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return testcase;
	}

}
