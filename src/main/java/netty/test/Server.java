package netty.test;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.time.Timer;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    static final Logger log = LoggerFactory.getLogger(Server.class);
    void start() {
        ThreadFactory threadFactory = new ThreadFactory() {

            private final AtomicInteger prefix = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "YQ-BIZ-" + prefix.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            }
        };

        NioEventLoopGroup boss = new NioEventLoopGroup(1,threadFactory); // 1个即可
        NioEventLoopGroup worker = new NioEventLoopGroup(0,threadFactory); // 为0则netty自动设置

        System.out.println("event loop created");
        Timer.sleep(10*1000);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);


            System.out.println("event loop was bound with bootstrap");
            Timer.sleep(10*1000);


            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            // 连接建立时会执行该方法
                            log.debug("connected {}", ctx.channel());
                            super.channelActive(ctx);
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            // 连接断开时会执行该方法
                            log.debug("disconnect {}", ctx.channel());
                            super.channelInactive(ctx);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            // 客户端发送数据时会执行该方法
                            log.debug("coming data {}", ctx.channel());
                            super.channelRead(ctx, msg);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(8081);
            log.debug("{} binding...", channelFuture.channel());

            System.out.println("bind...");
            Timer.sleep(10*1000);

            channelFuture.sync();

            System.out.println("sync...");
            Timer.sleep(10*1000);

            log.debug("{} bound...", channelFuture.channel());
            // 关闭channel
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            log.debug("stopped");
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
