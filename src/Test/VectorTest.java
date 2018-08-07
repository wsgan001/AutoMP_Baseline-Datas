package Test;

import java.util.Vector;

public class VectorTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector<Integer> s1 = new Vector<Integer>();
		s1.clear();s1.add(1);
		Vector<Integer> s2 = (Vector<Integer>) s1.clone();
		s2.add(2);
		System.out.println(s1.size() + "  " + s2.size());
		Integer p1 = 1;
		Integer p2 = p1; p2 ++;
		System.out.println(p1 + "  " + p2);
	}

}
