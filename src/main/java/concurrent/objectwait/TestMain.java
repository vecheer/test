package concurrent.objectwait;

import java.util.concurrent.TimeUnit;

/**
 * @author yq
 * @version v1.0 2023-01-16 11:05 AM
 */
public class TestMain {
    public static void main(String[] args) {
        final Object lock = new Object();

        new Thread(()->{
            synchronized (lock){
                System.out.println("t1 got the lock");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ignored) {
                }
                System.out.println("now t1 going to wait");
                try {
                    lock.wait();
                } catch (InterruptedException ignored) {
                }
                System.out.println("t1 awake (t2 return the lock to t1)");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ignored) {
                }
            }
        },"t1").start();

        new Thread(()->{
            synchronized (lock){
                System.out.println("t2 got the lock");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ignored) {
                }
                System.out.println("now t2 going to return the lock");
                lock.notifyAll();
            }
        },"t2").start();
    }
}
