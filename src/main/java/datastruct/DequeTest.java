package datastruct;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author yq
 * @version v1.0 2023-02-08 4:09 PM
 */
public class DequeTest {
    public static void main(String[] args) {
        Deque<String> queue = new ArrayDeque<>(5);
        queue.add("A");
        queue.add("B");
        queue.add("C");

        String s = queue.pollFirst();
        System.out.println(s);

    }
}
