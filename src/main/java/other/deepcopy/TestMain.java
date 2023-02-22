package other.deepcopy;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

/**
 * @author yq
 * @version v1.0 2023-01-16 2:43 PM
 */
public class TestMain {
    public static void main(String[] args) {
        HashMap<String, User> old = new HashMap<>();
        old.put("Zhangsan",new User("Zhangsan","junior student"));
        old.put("lisi",new User("Lisi","senior student"));

        HashMap<String, User> newMap = (HashMap<String, User>) old.clone();
        newMap.replace("Zhangsan",new User("Zhangsan","XXXX"));

        System.out.println(old.get("Zhangsan"));
        System.out.println(newMap.get("Zhangsan"));
    }

    @Data
    @AllArgsConstructor
    static class User{
        String name;
        String info;
    }
}
