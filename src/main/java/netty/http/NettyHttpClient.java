package netty.http;


import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;


@Slf4j
public class NettyHttpClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8080;

    public static void main(String[] args) throws URISyntaxException, InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(1));
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) {
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
                // 客户端解码器
                ch.pipeline().addLast(new HttpClientCodec());
                ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
                ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpResponse>(){
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, HttpResponse msg) throws Exception {
                        log.info("收到了服务端返回: {}",msg.decoderResult());
                        if (msg instanceof FullHttpResponse){
                            FullHttpResponse response = (FullHttpResponse)msg;
                            ByteBuf contentBuf = response.content();
                            String content = contentBuf.toString(StandardCharsets.UTF_8);
                            log.info("收到了服务端返回的内容: {}",content);
                        }
                    }
                });
            }
        });

        // 和服务端建立 tcp 连接
        Channel channel = bootstrap.connect(HOST, PORT).sync().channel();


        // 开始 构建 并 发送 http 请求
        log.info("下面开始发送 http 请求");

        URI uri = new URI("http://127.0.0.1:8080/test");
        String msg = "Are you ok?";
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString(),
                Unpooled.wrappedBuffer(msg.getBytes(StandardCharsets.UTF_8))
        );

        // 构建http请求
        request.headers().set(HttpHeaderNames.HOST, HOST);
        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

        // 发送http请求
        channel.write(request);
        channel.flush();
        channel.closeFuture().sync();
        /*
       */
    }
}
