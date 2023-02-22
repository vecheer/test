package io.speed.reader;

import io.speed.addition.BigFileGenerator;
import io.speed.reader.support.AbstractFileReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * NIO mem map 内存映射的方式
 * 将 磁盘文件 直接映射到 用户内存空间下，减少了 磁盘 到 内核 IO 缓存区 的拷贝操作
 * 和零拷贝的思想类似(zero copy是减少了内核IO缓冲区到用户空间的拷贝操作)
 */
public class MemMapReader extends AbstractFileReader {
    @Override
    protected void doReadFile(String filePth) {
        try {
            FileInputStream fis = new FileInputStream(filePth);
            FileChannel channel = fis.getChannel();
            long fileSize = channel.size();

            // map memory
            MappedByteBuffer source = channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    0,
                    fileSize
            );

            // read from user mapped memory
            // 每次读取 len 大小的内容
            int len = 1024*1024;
            byte[] temp = new byte[len];
            // 循环读取 buffer，循环条件是 off+len < channel.size() , 这样可以保证每次可以读取len个字节
            for (long off = 0; off+len < fileSize; off=off+len) {
                source.get(temp,0,len);
                //System.out.println(new String(temp, StandardCharsets.UTF_8));
            }

            // 循环结束之后，可能仍剩余一小部分内容没有读完，这部分内容长度不到 len
            int remain = source.remaining();
            if (remain > 0){
                byte[] last = new byte[remain];
                source.get(last);
                //System.out.println(new String(last, StandardCharsets.UTF_8));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MemMapReader reader = new MemMapReader();
        long timeCost = reader.readFile(BigFileGenerator.getTestFilePath());
        System.out.println("共计花费: " + timeCost);
    }
}
