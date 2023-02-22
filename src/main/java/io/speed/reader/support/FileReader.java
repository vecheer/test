package io.speed.reader.support;

public interface FileReader {
    /**
     * 读文件的每一行，返回消耗时间
     * @param filePth file to read
     * @return time cost
     */
    long readFile(String filePth);
}
