package io.nioreadfile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 并发读的要点：
 * - 大多数场景是csv文件，文件内容由一行行数据组成
 * - 一个完整的行，结束时最后一个字符是'\n'
 *
 * - 多个线程一起读，每个线程都会分得一定的内容区域
 * - 内容区域划分时，大概率会发生这样的事件：某一行数据被分割成两半。那么被分割开的这一行，应该由哪个线程处理？
 *
 * - 当前线程分得的内容区域，开头部分可能是：①一个断开的行的后半部分 ②刚好一个完整的行
 * - 当前线程分得的内容区域，结尾部分可能是：①一个断开的行的前半部分 ②刚好一个完整的行
 *
 * - 可以选择的半行处理策略是：
 *      - 线程处理自己内容的开头时，遇到断开的行，交给上个线程处理，自身不断右移开头标记，直到找到下一行数据起始点
 *      - 线程处理自己内容的结束时，遇到断开的行，则继续往下处理，直到把断开的这一行处理完
 */
public class MemMapFileReader {

    public void readFile(String filePath){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            FileChannel channel = fis.getChannel();

            MappedByteBuffer mappedBuffer = channel.map(
                    FileChannel.MapMode.READ_ONLY,
                    0,
                    channel.size()
            );


            CharBuffer result = StandardCharsets.UTF_8.decode(mappedBuffer);
            System.out.println(result);
            System.out.println(result.length());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final String filePath = "C:\\Users\\Vecheer\\Desktop\\bigFile.txt";
        MemMapFileReader reader = new MemMapFileReader();
        reader.readFile(filePath);

        /*final String test = "D:\\!prof分析\\新建文件夹\\ifs-dump-uat.hprof";
        MemMapFileReader reader = new MemMapFileReader();
        reader.readFile(test);*/
    }
}
