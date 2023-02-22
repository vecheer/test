package other.common;
import java.io.*;
import java.nio.file.Paths;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class zipFile {

    public enum DeviceAdTypeEnum {

        INSIDE(0, "内部"),

        BAIDU_POLY_PANEL(1, "百度聚屏"),

        QUICK_HAIR_CLOUD(2, "快发云");

        private Integer type;

        private String msg;

        private DeviceAdTypeEnum(Integer type, String msg) {

            this.type = type;

            this.msg = msg;

        }

        public static DeviceAdTypeEnum instance(Integer type) {

            for (DeviceAdTypeEnum deviceAdType : DeviceAdTypeEnum.values()) {

                if(deviceAdType.getType().equals(type)) {

                    return deviceAdType;

                }

            }

            return null;

        }

        /**

         * 根据code获取value

         * @param type

         * @return

         */

        public static String getMsgByType(Integer type){

            for(DeviceAdTypeEnum adTypeEnum:DeviceAdTypeEnum.values()){

                if(type.equals(adTypeEnum.getType())){

                    return adTypeEnum.getMsg();

                }

            }

            return null;

        }

        /**

         * 根据name获取code

         * @param msg

         * @return

         */

        public static Integer getTypeByMsg(String msg){

            for(DeviceAdTypeEnum adTypeEnum:DeviceAdTypeEnum.values()){

                if(msg.equals(adTypeEnum.getMsg())){

                    return adTypeEnum.getType();

                }

            }

            return null;

        }

        public Integer getType() {

            return type;

        }

        public void setType(Integer type) {

            this.type = type;

        }

        public String getMsg() {

            return msg;

        }

        public void setMsg(String msg) {

            this.msg = msg;

        }

    }

/*    //compress方法需要传入2个参数，是两个地址
//第一个地址是目标打包文件的地址，第二个是zip包输出的地址
    public static void main(String[] args) throws IOException {
        String hallFilePath = "E:/" +  "packs";
        compress(Paths.get(hallFilePath).toString(), hallFilePath + ".zip");
    }

    //由此开始是所有相关的工具方法
    public static void compress(String fromPath, String toPath) throws IOException {
        File fromFile = new File(fromPath);
        File toFile = new File(toPath);
        if (!fromFile.exists()) {
            throw new RuntimeException(fromPath + "不存在！");
        }
        try (FileOutputStream outputStream = new FileOutputStream(toFile); CheckedOutputStream checkedOutputStream = new CheckedOutputStream(outputStream, new CRC32()); ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream)) {
            String baseDir = "";
            compress(fromFile, zipOutputStream, baseDir);
        }
    }

    private static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (!file.exists()) {
            return;
        }
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte[] data = new byte[1024];
            while ((count = bis.read(data, 0, 1024)) != -1) {
                zipOut.write(data, 0, count);
            }
        }
    }

    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
        File[] files = dir.listFiles();
        if (files != null && files.length>0) {
            for (File file : files) {
                compress(file, zipOut, baseDir + dir.getName() + File.separator);
            }
        }
    }*/
}
