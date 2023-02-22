package utils.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleLog {

    public static void info(String format,Object ... args){
        System.out.println(formatLog(format,args));
    }

    public static void error(String format,Object ... args){

        System.err.println(formatLog(format,args));
    }

    private static String formatLog(String format,Object ... args){
        StringBuilder sb = new StringBuilder();
        sb.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        sb.append(" [");
        sb.append(Thread.currentThread().getName());
        sb.append("] ");
        sb.append(Thread.currentThread().getStackTrace()[3]);
        sb.append(" ");
        sb.append("[info]");
        sb.append(" ");
        sb.append(format.replace("{}", "[%s]"));
        return String.format(sb.toString(),args);
    }

    public static void main(String[] args) {
        ConsoleLog.info("hello {} and {} !","zhangsan","lisi");
        ConsoleLog.error("发现了异常! 异常信息是: {} ","未找到加载类！");
    }
}
