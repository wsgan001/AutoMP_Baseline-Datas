package ESER.ESER_Entry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PreTest {

	private static String[] tmp = new String[1005];

    private static void CreateFolder(String add)
    {
        File dir = new File(add);
        if (dir.exists()) return; else dir.mkdirs();
    }

	public static ArrayList<Integer> main(Integer SampleNumber, ArrayList<String> FolderName) {
        CreateFolder("./datas/ESER/");
        ArrayList<Integer> cases = new ArrayList<Integer>(); cases.clear();
	    try
        {
            for(String nm : FolderName)
            {
                String foldnm = "./datas/ESER/" + nm.toString() + "/";
                CreateFolder(foldnm);
                Scanner sc = null;
                FileWriter fw = null;
                Integer casecnt = 0;
                Integer linecnt = 0;
                Integer nowcent = 0;
                sc = new Scanner(new FileInputStream( "./datas/ESER/OriginData/" + nm + ".txt" ));
                while(sc.hasNextLine())
                {
                    String str = sc.nextLine();
                    linecnt ++;
                    if((linecnt&1) > 0)
                    {
                        casecnt ++;
                        CreateFolder(foldnm + casecnt.toString() + "/");
                        nowcent = Integer.parseInt(str);
                    }else
                    {
                        tmp = str.split("\t");
                        for(Integer spnum = 1; spnum <= SampleNumber; ++ spnum)
                        {
                            fw = new FileWriter(new File(foldnm + casecnt.toString() + "/" + "Query_" + spnum.toString() + ".txt"));
                            fw.write(nowcent.toString() + "\r\n");
                            for(Integer id = 0; id < spnum; ++id)
                            {
                                Integer pos = tmp[id].indexOf(':');
                                if(pos < 0) pos = tmp[id].length();
                                fw.write(tmp[id].substring(0, pos) + "\t");
                            }
                            fw.close();
                        }
                    }
                }
                cases.add(casecnt);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cases;
	}

}
