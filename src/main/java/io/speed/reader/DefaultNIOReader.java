package io.speed.reader;

import io.speed.addition.BigFileGenerator;
import io.speed.reader.support.AbstractFileReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class DefaultNIOReader extends AbstractFileReader {
    @Override
    protected void doReadFile(String filePth) {
        try {
            FileInputStream fis = new FileInputStream(filePth);
            FileChannel channel = fis.getChannel();

            int len = 1024*1024; // 每次读取1M
            byte[] temp = new byte[len];
            ByteBuffer buffer = ByteBuffer.allocateDirect(len);
            int read;
            while ((read = channel.read(buffer))!=-1){
                buffer.flip();
                buffer.get(temp,0,read);
                // System.out.println(new String(temp, StandardCharsets.UTF_8));
                buffer.clear();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        DefaultNIOReader reader = new DefaultNIOReader();
        long timeCost = reader.readFile(BigFileGenerator.getTestFilePath());
        System.out.println("共计花费: " + timeCost);
    }
}
