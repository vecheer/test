package netty.test;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import utils.time.Timer;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class EventLoopGroupTest {
    public static void main(String[] args) {
        EventLoopGroup eventExecutors = new NioEventLoopGroup(1, new ThreadFactory() {

            private final AtomicInteger prefix = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r,"BIZ-" + prefix.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }
        });

        Timer.sleep(1000*1000);
    }
}
