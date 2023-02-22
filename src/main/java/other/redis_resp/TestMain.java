package other.redis_resp;


import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 踩坑之—— redis 的 resp 协议，无论哪种操作系统，都应该使用 \r\n 作为 resp 协议分隔符
 * @author yq
 * @version v1.0 2023-01-19 9:28 PM
 */
public class TestMain {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.204.140", 6379);
            OutputStream os = socket.getOutputStream();
            os.write("*3\r\n$3\r\nSET\r\n$3\r\nOOO\r\n$3\r\n323\r\n".getBytes(StandardCharsets.UTF_8));
            os.flush();

            socket.close();
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
