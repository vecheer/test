package io.file.common_read;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author yq
 * @version v1.0 2023-02-09 1:43 PM
 */
public class TestMain {
    private static final String FILE_PATH = "D:\\我的文件\\作业实验\\编译原理\\编译原理实验\\LL(1)分析法\\main.cpp" ;
    private static Charset CHARSET = Charset.forName("GBK");

    @SneakyThrows
    public static void main(String[] args) {
        File file = new File(FILE_PATH);

        FileChannel ch = new FileInputStream(file).getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * 1024); //4k

        while (ch.read(buffer)!=-1){
            buffer.flip();

            String s = CHARSET.decode(buffer).toString();
            System.out.print(s);

            buffer.clear();
        }


    }

}
