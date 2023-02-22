package jdk.api.string_split;

import java.util.Arrays;

/**
 * 想知道 split 分割函数，在输入为空时，返回的是啥
 * @author yq
 * @version v1.0 2023-02-09 12:39 PM
 */
public class TestMain {
    public static void main(String[] args) {

        String delimiter = "~";

        String in2  = "";
        System.out.println(Arrays.toString(in2.split(delimiter)));

        String in1  = "~";
        System.out.println(Arrays.toString(in1.split(delimiter)));

        String in3  = "123";
        System.out.println(Arrays.toString(in3.split(delimiter)));

    }

}
