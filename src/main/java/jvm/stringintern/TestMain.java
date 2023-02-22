package jvm.stringintern;

/**
 * @author yq
 * @version v1.0 2023-01-22 5:50 PM
 */
public class TestMain {
    public static void main(String[] args) {
        /*String a = "XY";

        String b = a + "Z";

        String c = new String("XYZ");

        String e = "XYZ";

        String d = b.intern();




        System.out.println(d == c);
        System.out.println(d == b);
        System.out.println(d == e);*/


        String x = "XYZ";

        String a = "XY";

        String b = a + "Z";

        String c = b.intern();

        System.out.println(c == b);

    }
}
