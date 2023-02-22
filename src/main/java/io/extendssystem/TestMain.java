package io.extendssystem;

import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * @author yq
 * @version v1.0 2023-01-17 4:13 PM
 */
public class TestMain {


    @SneakyThrows
    public static void main(String[] args) {
        // 序列化
        ByteArrayInputStream bais = new ByteArrayInputStream("hello".getBytes());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.readObject();

        // 读文件
        FileInputStream fis = new FileInputStream("filepath:xxx");
//        FileInputStream fis = new FileInputStream("filepath:xxx");
    }



}
