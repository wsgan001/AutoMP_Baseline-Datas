package Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TypeCount {

    public static void main(String[] args)
    {
        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream("./datas/graph.node"));
            Set<Integer> s = new HashSet<Integer>(); s.clear();
            while(sc.hasNextLine())
            {
                String[] tmp = new String[5];
                tmp = sc.nextLine().split("\t");
                s.add(Integer.parseInt(tmp[1]));
            }
            System.out.println(s.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
