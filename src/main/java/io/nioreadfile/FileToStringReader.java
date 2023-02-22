package io.nioreadfile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class FileToStringReader {
    /**
     * NIO channel 形式直接读取
     * @param filePath
     * @return
     */
    public String fileToString(String filePath){

        int bytesNum = 0;

        StringBuilder fileContent = new StringBuilder();
        // read file
        try(FileChannel fileChannel = new FileInputStream(filePath).getChannel()){

            ByteBuffer buffer = ByteBuffer.allocateDirect(4*1024); // 4k every time

            while (fileChannel.read(buffer) != -1){
                // change to read
                buffer.flip();
                // read
                fileContent.append(StandardCharsets.UTF_8.decode(buffer));
                // clean
                buffer.clear();

                bytesNum += 4;
                System.out.println("当前内存字符串占用内存大小是 : " + fileContent.toString().length() / 1024 + "KB");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable t){
            System.out.println("可能出现了 OOM, 当前已读取 [" + bytesNum +"] KB");
        }


        return fileContent.toString();
    }


    public static void main(String[] args) {
        String filePath = "C:\\Users\\Vecheer\\Desktop\\zc.txt";
    }
}
