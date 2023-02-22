package other;


import lombok.SneakyThrows;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class CommonTest {
    public static void main(String[] args) {
        main0(args);

    }

    @SneakyThrows
    @SuppressWarnings("rawtypes")
    public static void main0(String[] args) {

        Class<?> clazz = Class.forName("java.util.concurrent.ConcurrentHashMap");
        Class<ConcurrentHashMap> mapClass = ConcurrentHashMap.class;

        System.out.println(clazz == mapClass);
    }
}
