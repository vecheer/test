package other.common;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

public class ByteBuddyTest {

    @Test
    public void testIntercept() throws InstantiationException, IllegalAccessException {
        String helloWorld = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World"))
                .make()
                .load(ByteBuddyTest.class.getClassLoader())
                .getLoaded()
                .newInstance()
                .toString();
        System.out.println(helloWorld);
    }


    @Test
    public void testGenerateClass() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        // 创建类信息
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .name("com.yq.test.MyClassByByteBuddy")
                .defineMethod("showInfo",void.class, Modifier.PUBLIC)
                .intercept(FixedValue.value("这是一个自动生成的class"))
                .make();

        byte[] bytesOfClassFile = dynamicType.getBytes();


        String currentPath = ByteBuddyTest.class.getResource("/").getPath();
        String path = currentPath + "MyClassByByteBuddy.class";
       try(FileOutputStream fos = new FileOutputStream(new File(path))) {
           fos.write(bytesOfClassFile);
       } catch (IOException e) {
           e.printStackTrace();
       }

       Class<?> clazz = dynamicType.load(ClassLoader.getSystemClassLoader()).getLoaded();

        clazz.getMethod("showInfo").invoke(clazz.newInstance());

    }
}
