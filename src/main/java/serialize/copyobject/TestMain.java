package serialize.copyobject;

import lombok.SneakyThrows;
import serialize.copyobject.domain.User;
import serialize.copyobject.domain.UserAddress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author yq
 * @version v1.0 2023-01-16 8:28 PM
 */
public class TestMain {

    @SneakyThrows
    public static void main(String[] args) {
        User user = new User(
                "u123456",
                "ZhangSan",
                new UserAddress(
                        "CN",
                        "anhui",
                        "hefei",
                        "shushan"
                )
        );


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(user);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        User copy = (User) ois.readObject();

        System.out.println("user.hashCode() = " + user.hashCode());
        System.out.println("copy.hashCode() = " + copy.hashCode());

        System.out.println("user = " + user);
        System.out.println("copy = " + copy);


    }
}
