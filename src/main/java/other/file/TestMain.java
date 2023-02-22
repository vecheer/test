package other.file;

import sun.security.action.GetPropertyAction;

import java.io.File;
import java.security.AccessController;

/**
 * @author yq
 * @version v1.0 2023-01-17 10:44 AM
 */
public class TestMain {
    public static void main(String[] args) {
        System.getProperties().put("file.separator","=-=");
        System.out.println("System.getProperty(\"file.separator\") = " + System.getProperty("file.separator"));
        System.out.println("AccessController.doPrivileged(new GetPropertyAction(\"file.separator\")).charAt(0) = " + AccessController.doPrivileged(new GetPropertyAction("file.separator")).charAt(0));
        System.out.println("File.separator = " + File.separator);
    }
}
