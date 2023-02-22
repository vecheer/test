package concurrent.threadex;


import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * 线程出现异常，是否死亡？
 * @author yq
 * @version v1.0 2023-02-08 10:05 AM
 */
public class TestMain {
    @SneakyThrows
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                throw new RuntimeException("hha");
            }
        },"xxxxxxxxxxxxxxxxxxxxxxx").start();

        TimeUnit.SECONDS.sleep(100);
    }


}
