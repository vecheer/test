package jvm.thread_max_num;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class TestMain {
    private static final AtomicInteger num = new AtomicInteger(0);
    private static long tid = 0L;

    public static void main(String[] args) {

        try{
            for (int i = 0; ; i++) {
                new Thread(()->{
                    try {
                        tid = Thread.currentThread().getId();
                        num.incrementAndGet();
                        new CountDownLatch(1).await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

        }catch (Throwable t){
            System.out.print("出现了异常!!!!");
            System.out.println("最终出现的线程数为: [" + num.get() + "]");
            System.out.printf("最终的线程 id(十进制) 为：[%d]\n", Integer.parseInt(tid+"",16)); // 默认线程 id 是 16进制
            t.printStackTrace();

            try {
                TimeUnit.SECONDS.sleep(120);
                System.exit(-1);
            }catch (Throwable e){
                System.exit(-1);
            }

        }
    }
}