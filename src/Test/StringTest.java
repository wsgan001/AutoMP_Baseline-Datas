package Test;

public class StringTest {
    public static void main(String[] args)
    {
        String now="0123456789";
        Integer pos = now.indexOf(':');
        System.out.println(now.substring(0,pos));
    }
}
