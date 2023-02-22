package io.speed.reader.support;


public abstract class AbstractFileReader implements FileReader {
    @Override
    public long readFile(String filePth) {
        long start = System.currentTimeMillis();
        doReadFile(filePth);
        long end = System.currentTimeMillis();
        return (end - start);
    }


    /**
     * BIO 读取套路一般是:
     *      1.建立输入流 bis
     *      2.bis.read(数组) != -1
     *
     * NIO 读取套路一般是:
     *      1.建立 channel
     *      2.新建 buffer
     *      3.channel.read(buffer)
     *      4.buffer.flip(), buffer.get(数组), buffer.clear();
     *
     * 其中 NIO 的内存映射读取特殊一些:
     *      1.建立 channel
     *      2.MappedByteBuffer buffer = channel.map(读写模式,0,channel.size())
     *      3.直接对 buffer 进行 get (由于 buffer 是一块超大直接内存，需要循环多次 get, 无需 flip, 全程 get)
     * @param filePth
     */
    protected abstract void doReadFile(String filePth);
}
