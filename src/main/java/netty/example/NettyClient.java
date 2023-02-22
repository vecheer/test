package netty.example;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.log.ConsoleLog;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NettyClient {
    static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    private Bootstrap bootstrap = new Bootstrap();

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

        NioEventLoopGroup worker = new NioEventLoopGroup(3, threadFactory); // 为0则netty自动设置


        bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(worker);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        // 连接建立时会执行该方法
                        ConsoleLog.info("connected {}", ctx.channel());
                        super.channelActive(ctx);
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        // 连接断开时会执行该方法
                        ConsoleLog.info("disconnect {}", ctx.channel());
                        super.channelInactive(ctx);
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        // 客户端发送数据时会执行该方法
                        ConsoleLog.info("coming data {}", ctx.channel());
                        super.channelRead(ctx, msg);
                    }
                });
            }
        });

    }

    Channel connectTo(String ip, int port) {
        ChannelFuture connect = bootstrap.connect(ip, port);
        connect.awaitUninterruptibly();

        return connect.channel();
    }

   /* public static void main(String[] args) {
        NettyClient client = new NettyClient();
        client.start();
        ExecutorService bizPool = Executors.newFixedThreadPool(3);
        bizPool.execute(() -> {
            Channel channel = client.connectTo("localhost", 8001);
            channel.writeAndFlush("8001 --- hello!");
            ConsoleLog.info("数据已发送");

        });
        bizPool.execute(() -> {
            Channel channel = client.connectTo("localhost", 8002);
            channel.writeAndFlush("8002 --- hello!");
            ConsoleLog.info("数据已发送");
        });
        bizPool.execute(() -> {
            Channel channel = client.connectTo("localhost", 8002);
            channel.writeAndFlush("8002 --- hello!");

        });


    }*/

    public static void main(String[] args) throws InterruptedException {
        NettyClient client = new NettyClient();
        client.start();
        Channel channel = client.connectTo("localhost", 8001);
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeBytes("hello".getBytes(StandardCharsets.UTF_8));
        ChannelFuture future = channel.writeAndFlush(buf).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    ConsoleLog.info("数据已发送");
                }
            }
        });

        future.awaitUninterruptibly();
        future.channel().close();
    }
}
