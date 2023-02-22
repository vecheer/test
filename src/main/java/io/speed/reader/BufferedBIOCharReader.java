package io.speed.reader;

import io.speed.addition.BigFileGenerator;
import io.speed.reader.support.AbstractFileReader;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BufferedBIOCharReader extends AbstractFileReader {
    @Override
    protected void doReadFile(String filePth) {
        try {
            FileInputStream fis = new FileInputStream(filePth);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis,StandardCharsets.UTF_8));

            String str;
            while((str = br.readLine()) != null) {
                //System.out.println(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BufferedBIOCharReader reader = new BufferedBIOCharReader();
        long timeCost = reader.readFile(BigFileGenerator.getTestFilePath());
        System.out.println("共计花费: " + timeCost);
    }
}
