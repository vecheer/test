package other.getmethodname;

/**
 * @author yq
 * @version v1.0 2023-01-16 4:15 PM
 */
public class TestMain {
    public static void main(String[] args) {
        func();
    }

    public static void func(){
        // 为什么是[1] 而不是[0] ?
        // 实际上[0]一直都是 getStackTrace() 方法自己, [1]才是调用getStackTrace()的这个方法
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(methodName);
    }
}
