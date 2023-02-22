package jvm.string;

/**
 * @author yq
 * @version v1.0 2023-02-07 8:19 PM
 */
public class TestMain {
    public static void main(String[] args) {
        String s = "AB";
        String s2 = s;
        s = s + 2;
        System.out.println(s2);
    }
}
