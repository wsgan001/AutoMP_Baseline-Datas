package ESER.ESER_Entry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class RandomNumberGenerator {

	private static final Integer maxnum = 4869452;
	private static final Set<Integer> c = new HashSet<Integer>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			FileWriter fw = new FileWriter(new File("./datas/Test/Test.in"));
			c.clear();
			Integer cnt = 50;
			Random rd = new Random();
			while(cnt > 0)
			{
				cnt --;
				Integer t = rd.nextInt(maxnum) + 1;
				while(c.contains(t))t = rd.nextInt(maxnum) + 1;
				c.add(t);
				fw.write(t.toString() + "\r\n1\r\n1\r\n1\r\n1\r\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
