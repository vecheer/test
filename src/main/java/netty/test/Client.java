package netty.test;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    static final Logger log = LoggerFactory.getLogger(Client.class);

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
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
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

    }

    ChannelFuture connect(String ip, int port){
        return bootstrap.connect(ip, port);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connect("localhost",8888);
    }
}
