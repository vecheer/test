package other.common;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.util.Zip4jUtil;

import java.io.File;

public class Zip4JTest {
    public static void main(String[] args) throws ZipException {
        String targetDir = "C:\\Users\\Vecheer\\Desktop\\新建文件夹 (6)\\MyZip.zip";
        String sourceDir = "C:\\Users\\Vecheer\\Desktop\\sofastack笔记(paas)";
        ZipFile zipFile = new ZipFile(targetDir);
        zipFile.addFolder(new File(sourceDir));



    }
}
