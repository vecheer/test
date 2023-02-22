package jdk.api.enum_valueof;

/**
 * @author yq
 * @version v1.0 2023-02-16 1:45 PM
 */
public class TestMain {
    public static void main(String[] args) {
        BizCode messaging = BizCode.valueOf("Messaging");
        BizCode messaging1 = Enum.valueOf(BizCode.class, "Messaging");
    }
}
