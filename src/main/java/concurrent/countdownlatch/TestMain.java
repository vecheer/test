package concurrent.countdownlatch;

import java.util.concurrent.CountDownLatch;

abstract class Super{
    private static final CountDownLatch latch = new CountDownLatch(1);

    public void countDown(){
        latch.countDown();
    }

    public void await(){
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Sub extends Super{

}

/**
 * @author yq
 * @version v1.0 2023-02-03 11:03 AM
 */
public class TestMain {
    public static void main(String[] args) {
        Sub son1 = new Sub();
        Sub son2 = new Sub();
        new Thread(()->{
            son1.await();
            System.out.println("son 1 wake up!");
        }).start();
        new Thread(()->{
            System.out.println("son 2 try to wake son1");
            son2.countDown();
        }).start();
    }
}
