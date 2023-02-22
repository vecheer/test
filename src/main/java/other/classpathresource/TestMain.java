package other.classpathresource;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class TestMain {
    public static void main(String[] args) {

        Class<?> clazz = TestMain.class;
        ClassLoader classLoader = clazz.getClassLoader();
        String path = "other/classpathresource/TestMain.class";
        getInputStream(clazz,classLoader,path);



    }


    public static InputStream getInputStream(Class<?> clazz, ClassLoader classLoader, String path){
        InputStream is;
        if (clazz != null) {
            is = clazz.getResourceAsStream(path);
        }
        else if (classLoader != null) {
            is = classLoader.getResourceAsStream(path);
        }
        else {
            is = ClassLoader.getSystemResourceAsStream(path);
        }
        if (is == null) {
            throw new RuntimeException("cannot be opened because it does not exist");
        }

        return is;
    }
}
