package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) {


        User u = new User();
        Say uProxy = (Say) Proxy.newProxyInstance(
                User.class.getClassLoader(),
                User.class.getInterfaces(),
                (proxy, method, mArgs) -> {
                    System.out.println("hello before");
                    method.invoke(u,mArgs);
                    System.out.println("hello after");
                    return null;
                }
        );
        uProxy.sayHi();

    }

    static class MyHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    static interface Say{
        void sayHi();
    }
    static class User implements Say{
        int name;

        public void sayHi(){
            System.out.println("hi");
        }
    }
}
