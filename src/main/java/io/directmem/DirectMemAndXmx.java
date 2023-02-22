package io.directmem;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.LockSupport;

public class DirectMemAndXmx {
    public static void main(String[] args) {
        try{
            // -Xms100M -Xmx200M  -XX:MaxDirectMemorySize=201M
            ByteBuffer buffer = ByteBuffer.allocateDirect(200*1024*1024);  // 200MB
        }catch (OutOfMemoryError oom){
            System.out.println("OOM !");
            oom.printStackTrace();
            System.exit(-1);
        }

        System.out.println("没有发生 OOM !");
        LockSupport.park();
    }
}
