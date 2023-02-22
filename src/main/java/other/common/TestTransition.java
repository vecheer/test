package other.common;


import org.junit.jupiter.api.Test;

class Transition {
    /*public Transition() {

    }*/

    public Transition(int a) {

    }
}

public class TestTransition {

    @Test
    public void testFunc() throws NoSuchMethodException {
//        System.out.println("Transition.class.getDeclaredConstructor() = " + Transition.class.getDeclaredConstructor(int.class));
//        System.out.println("Transition.class.getDeclaredConstructor() = " + Transition.class.getDeclaredConstructor());
//        System.out.println("Transition.class.getDeclaredConstructor() = " + Transition.class.getDeclaredConstructor(String.class));
        System.out.println(int.class == Integer.class);
    }

    @Test
    public void testFunc2() throws NoSuchMethodException {
        System.out.println(test(null) == null);    // 结果为true
        System.out.println(test((Object) null) == null); // 结果为false
    }

    @Test
    public void testFunc3()  {
        test((Object) null);
    }


    @Test
    public void testFunc4()  {
        test();
    }


    private Object[] test(Object ...args){
        return args;
    }



    @Test
    public void cj(){
        System.out.println(test() == null);
    }













    private Object test2(Object arg){
        return arg;
    }

}
