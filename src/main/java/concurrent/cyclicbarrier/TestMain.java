package concurrent.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author yq
 * @version v1.0 2023-02-01 2:05 PM
 */
public class TestMain {
    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(3);

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("t1已到达同步点1，正在等待...");
                cb.await();
                System.out.println("t1 已经跨越 同步点1!");
                TimeUnit.SECONDS.sleep(1);
                System.out.println("t1已到达同步点2，正在等待...");
                cb.await();
                System.out.println("t1 已经跨越 同步点2!");
                System.out.println("t1已到达同步点3，正在等待...");
                cb.await();
            } catch (InterruptedException | BrokenBarrierException ignored) {
            }
        }).start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
                cb.await();
                TimeUnit.SECONDS.sleep(5);
                System.out.println("t2 到达 2");
                cb.await();
            } catch (InterruptedException | BrokenBarrierException ignored) {
            }
        }).start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
                cb.await(10,TimeUnit.SECONDS);
                TimeUnit.SECONDS.sleep(5);
                System.out.println("t3 到达 2");
                cb.await();
            } catch (InterruptedException | BrokenBarrierException | TimeoutException ignored) {
            }
        }).start();
    }
}
