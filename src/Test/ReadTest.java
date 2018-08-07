package Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ReadTest {

	public static String[] tmp = new String[105];
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filename = "./datas/ReadTest.in";
		String str = "";
		try {
			InputStream is = new FileInputStream(filename);
			Scanner sc = new Scanner(is);
			while( sc.hasNextLine() )
			{
				str = sc.nextLine();
				if(str.length() < 2)System.out.println(str);
				else
				{
					tmp = str.split("\t");
					System.out.println("111   " + tmp.length);
					for(Integer i=0; i<tmp.length; ++i)
						System.out.println(tmp[i]);
				}
			}
			sc.close();is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
