package io.speed.addition;

import utils.log.ConsoleLog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成文件的格式
 * id(8), 创建日期(10,yyyy-MM-dd), 账号(10), 注册年限(2), 所属分类(5), 简介(30)
 */
public class BigFileGenerator {

    private static final String FIlE_PATH = "C:\\Users\\Vecheer\\Desktop\\bigFile.txt";

    private static final int LINES_TO_WRITE = 20_000_000; // 2000 w     约2G

    public static void main(String[] args) {
        BigFileGenerator generator = new BigFileGenerator();
        generator.generateFile(FIlE_PATH,LINES_TO_WRITE);
    }

    public void generateFile(String filePath,int size){
        int rate = 0;
        try(FileChannel channel = new FileOutputStream(filePath).getChannel()){
            ByteBuffer buffer = ByteBuffer.allocate(100);
            for (int i = 0; i < size; i++) {
                String line = generateLine();
                buffer.put(line.getBytes(StandardCharsets.UTF_8));
                buffer.flip();
                channel.write(buffer);
                buffer.clear();

                if (i%(size/100)==0){
                    rate += 1;
                    ConsoleLog.info("当前进度: %s",rate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String generateLine(){
        StringBuilder line = new StringBuilder(100);

        // id
        // 长度为 8
        String id = randomNumString(8);
        line.append(id).append(",");

        // 创建日期
        // 格式为 yyyy-MM-dd
        String formatTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        line.append(formatTime).append(",");

        // 账号
        // 长度固定 10
        String account = randomString(10);
        line.append(account).append(",");

        // 注册年限
        // 长度固定 2
        String years = randomNumString(2);
        line.append(years).append(",");

        // 所属分类
        // 长度固定 5
        String[] type = {
                "默认分类A",
                "默认分类B",
                "默认分类C",
                "默认分类D",
                "默认分类E",
        };
        int typeIndex = (int)(Math.random()*type.length);
        line.append(type[typeIndex]).append(",");

        // 简介
        // 长度固定 30
        String intro = randomString(30);
        line.append(intro).append('\n');

        return line.toString();
    }

    public String randomString(int len){
        StringBuilder result = new StringBuilder(len);
        for (int i = 0; i < len; i++) {

            int charType = (int) (Math.random() * 3) + 1;

            if (charType == 1){
                result.append((int) (Math.random() * 10));
            }else if (charType == 2){
                int factor = (int) (Math.random() * 26);
                char character = (char) ((int)'A' + factor);
                result.append(character);
            }else if (charType == 3) {
                int factor = (int) (Math.random() * 26);
                char character = (char) ((int)'a' + factor);
                result.append(character);
            }
        }
        return result.toString();
    }

    public String randomNumString(int len){
        StringBuilder result = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            result.append((int) (Math.random() * 10));
        }
        return result.toString();
    }




    public static String getTestFilePath(){
        return FIlE_PATH;
    }
}
