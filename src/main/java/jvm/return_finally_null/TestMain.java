package jvm.return_finally_null;

/**
 * @author yq
 * @version v1.0 2023-02-10 3:43 PM
 */
public class TestMain {

    private Object obj = null;

    public static void main(String[] args) {
        TestMain client = new TestMain();
        client.set(new Object());
        System.out.println(client.get());
        System.out.println(client.get());
    }

    public void set(Object obj){
        this.obj = obj;
    }

    public Object get(){
        try {
            return this.obj;
        }finally {
            this.obj = null;
        }
    }
}
