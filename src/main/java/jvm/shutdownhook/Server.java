package jvm.shutdownhook;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Server 是一个服务端，支持优雅停机
 *
 * @author yq
 * @version v1.0 2023-01-23 8:21 PM
 */
@Slf4j
public final class Server {

    private final long safeStopTimeout = 9 * 1000;

    /**
     * 记录当前正在执行的请求数（优雅停机的关键）
     */
    private final AtomicInteger currentReqCounts = new AtomicInteger(0);

    /**
     * server 端初始化
     */
    private void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gracefulShutdown(safeStopTimeout);
        }, "safeShutdown-T-0"));
    }

    /**
     * 服务启动
     */
    public void start() {
        // 初始化
        init();
        // 监听端口，开始服务
        onListen();
    }

    /**
     * 优雅停机
     *
     * @param timeout 停机超时时间，单位 ms
     */
    private void gracefulShutdown(long timeout) {
        long start = System.currentTimeMillis();

        offListen();

        // dead loop —— check current req counts
        // IF    counts > 0    THEN sleep 500 ms
        // ELIF  counts = 0    THEN stop
        // ELIF  timeout       THEN force stop
        while (currentReqCounts.get() > 0 && (System.currentTimeMillis() - start < timeout)) {
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException ignored) {
            }
        }

        if (currentReqCounts.get() > 0)
            log.error("停机超时! 正在强制停机......");

        System.out.println("stop now");
    }


    private void onListen() {

    }

    private void offListen() {

    }

    private void stop() {
        // executorService.shutdown();
        System.exit(0);
    }


    public static void main(String[] args) {


    }

}
