package io.printstream;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * @author yq
 * @version v1.0 2023-01-17 3:04 PM
 */
public class TestMain {

    @SneakyThrows
    @ParameterizedTest
    @ValueSource(strings = {"D:\\study_test\\test\\src\\main\\resources\\test"})
    public void printWriter(String filePath){
        PrintWriter printWriter = new PrintWriter(new FileOutputStream(filePath));
        printWriter.print("  XXX  ");
        printWriter.println("  ====  ");
        printWriter.print("  YYY  ");

        printWriter.close();
    }


}
