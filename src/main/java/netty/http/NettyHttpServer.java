package netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

public class NettyHttpServer {

    private static final Logger log = LoggerFactory.getLogger(NettyHttpServer.class);

    // server endpoint
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup(1);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(
                    new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            // 设置日志打印handler
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ByteBuf buf = (ByteBuf) msg;
                                    if (buf == null)
                                        log.error("收到的是空数据!");
                                    else
                                        log.info("收到的数据是: " + new String(ByteBufUtil.getBytes(buf), StandardCharsets.UTF_8));
                                    super.channelRead(ctx, msg);
                                }
                            });
                            // 设置http编解码器（codec）
                            ch.pipeline().addLast(new HttpServerCodec());
                            // HttpObjectAggregator 可以生成 FullHttpRequest
                            // 单单一个 httpCodec 只能生成 HttpRequest
                            ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
                            // 该处理器只会处理 HttpRequest 类型的msg
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {

                                protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) {
                                    log.info("收到http请求: {}", msg.uri());
                                    // 下面开始响应
                                    DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                                            /* http版本 */msg.protocolVersion(),
                                            /* 响应状态码 */HttpResponseStatus.OK
                                    );
                                    byte[] respBytes = "<h1>? wdnmd</h1>".getBytes();
                                    // 应该在header中设置content长度，否则浏览器会一直转圈，等待服务端继续发数据
                                    response.headers().setInt(CONTENT_LENGTH, respBytes.length);
                                    response.content().writeBytes(respBytes);
                                    ctx.writeAndFlush(response);
                                }
                            });
                        }
                    }
            );
            ChannelFuture channelFuture = serverBootstrap.bind(8080);
            channelFuture.channel().closeFuture().addListener((ChannelFutureListener) future -> {
                log.info("SERVER NOW SHUT DOWN, SEE YOU NEXT TIME!");
            });

            channelFuture.sync();

            // 让main阻塞在这里
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("encountering error" + e.getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
