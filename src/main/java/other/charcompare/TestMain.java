package other.charcompare;


public class TestMain {
    public static void main(String[] args) {
        char c = ' ';
        int ascii = c;
        System.out.println(ascii);

        System.out.println((int)'\n');
        System.out.println((int)'\r');

        char[] lessThanBlank = new char[ascii];
        for (int i = 0; i < ascii; i++) {
            lessThanBlank[i] = (char)i;
        }

        System.out.println(lessThanBlank);
    }
}
