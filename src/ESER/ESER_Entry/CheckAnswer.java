package ESER.ESER_Entry;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CheckAnswer {

	private static Set<Integer> ret = new HashSet<Integer>();
	private static String[] names = new String[1005];

	public static void main(Integer testcase) {
		// TODO Auto-generated method stub
			for(Integer i = 0; i <= 1000; i ++) names[i] = "";
			InputStream is;
			Scanner sc;
			try {
				//GetNames
				File namefile = new File("./datas/Test/name.txt");
				if(namefile.exists())
				{
					is = new FileInputStream(namefile);
					sc = new Scanner(is);
					Integer cnt = 0;
					while(sc.hasNextLine())
					{
						String str = sc.nextLine();
						cnt ++;
						names[cnt] = str;
					}
				}

				//Check
				for(Integer test = 1; test <= testcase; ++ test)
				{
					is = new FileInputStream("./datas/Test/" + test.toString() + ".ans");
					sc = new Scanner(is);
					ret.clear();
					Integer cnt1 = 0, cnt2 = 0, cou = 0;
					while( sc.hasNextInt() )ret.add(sc.nextInt());
					is = new FileInputStream("./datas/Test/" + test.toString() + ".out");
					sc = new Scanner(is);
					while( sc.hasNextInt() )
					{
						cou ++;
						if( ret.contains(sc.nextInt()) )
						{
							if(cou <= 10) cnt1 ++;
							if(cou >10 && cou <= 20) cnt2 ++;
						}
					}
					cnt2 += cnt1;
					if(names[test].length() > 0) 
						System.out.print(names[test] + "\t");
					else
						System.out.print("TestCase : " + test.toString() + "\t"); 
					System.out.println(cnt1.toString() + "\t" + cnt2.toString() + "\t" + ret.size());
					sc.close();is.close();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
