package netty.example;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.log.ConsoleLog;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyServer {
    static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    void start() {

        NioEventLoopGroup boss = new NioEventLoopGroup(1, new NamedFactory("boss",false)); // 1个即可
        NioEventLoopGroup worker = new NioEventLoopGroup(5, new NamedFactory("worker",true)); // 为0则netty自动设置

//        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss, worker);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ConsoleLog.info("connected {}", ctx.channel());
                            super.channelActive(ctx);
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            ConsoleLog.info("disconnected {}", ctx.channel());
                            super.channelInactive(ctx);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ConsoleLog.info("coming data {}", ctx.channel());
                            if (msg instanceof ByteBuf) {
                                ByteBuf buf = (ByteBuf) msg;
                                ctx.channel().writeAndFlush(buf.toString(StandardCharsets.UTF_8));
                            }
                        }
                    });
                }
            });
            System.out.println("bind...");
            ChannelFuture channelFuture = serverBootstrap.bind(this.port);
            channelFuture.addListener(
                    (ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            System.out.println("bind 成功！");
                        } else {
                            System.out.println("bind 失败！");
                            boss.shutdownGracefully();
                            worker.shutdownGracefully();
                        }
                    }
            );
            channelFuture.awaitUninterruptibly();
            // 关闭channel
            /*channelFuture.channel().closeFuture().sync();*/
        /*} catch (InterruptedException ignored) {
        }*/
    }

    /*public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.execute(()->{
            new NettyServer(8001).start();
        });
        pool.execute(()->{
            new NettyServer(8002).start();
        });
        pool.execute(()->{
            new NettyServer(8003).start();
        });
    }*/

    public static class NamedFactory implements ThreadFactory{

        private final AtomicInteger index = new AtomicInteger(0);

        private final String secondPrefix;

        private final boolean isDaemon;

        public NamedFactory(String secondPrefix, boolean isDaemon) {
            this.secondPrefix = secondPrefix;
            this.isDaemon = isDaemon;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "netty-" + secondPrefix + "-T-" + index.getAndIncrement());
            // 注意 eventloop 的线程到底是 daemon 还是 非-daemon
            // 非-daemon 可能导致服务是否直接自动消亡
            thread.setDaemon(isDaemon);
            return thread;
        }
    };

    public static void main(String[] args) {

        new NettyServer(8001).start();
/*        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}
