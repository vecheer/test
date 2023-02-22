package io.speed.reader;

import io.speed.addition.BigFileGenerator;
import io.speed.reader.support.AbstractFileReader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BufferedBIOReader extends AbstractFileReader {
    @Override
    protected void doReadFile(String filePth) {
        try {
            FileInputStream fis = new FileInputStream(filePth);
            BufferedInputStream bufferedIS = new BufferedInputStream(fis);

            int len = 1024*1024;
            byte[] temp = new byte[len];
            int read;
            while ((read = bufferedIS.read(temp))!=-1){
                //System.out.println(new String(temp, StandardCharsets.UTF_8));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BufferedBIOReader reader = new BufferedBIOReader();
        long timeCost = reader.readFile(BigFileGenerator.getTestFilePath());
        System.out.println("共计花费: " + timeCost);
    }
}
