package netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 测试 bytebuf 写入 new byte[]
 * @author yq
 * @version v1.0 2023-02-13 11:33 AM
 */
public class TestMain {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.directBuffer();

        buf.writeBytes(new byte[]{});
    }
}
