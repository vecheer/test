package other.getmethodname;

import java.util.Arrays;

/**
 * @author yq
 * @version v1.0 2023-01-16 4:16 PM
 */
public class Test {
    public static void main(String[] args) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        System.out.println(Arrays.toString(stackTrace));
    }
}
