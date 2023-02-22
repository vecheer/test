package concurrent.threadpool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Test {
    public static void main(String[] args) {
        ExecutorService pool = new ThreadPoolExecutor(
                8,
                10,
                60*1000,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20),
                new ThreadFactory(){
                    final AtomicInteger id = new AtomicInteger();
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r,"" + id);
                        t.setDaemon(true);
                        return t;
                    }
                }
        );
    }


    private void hello(int a,int b){

    }

    public void  test(){

        int a = 2;
        int b = 3;

        ExecutorService pool = Executors.newFixedThreadPool(3);

        pool.submit(new Runnable() {
            @Override
            public void run() {
                hello(a,b);
            }
        });
    }
}
