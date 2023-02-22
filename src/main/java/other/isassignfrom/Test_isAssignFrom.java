package other.isassignfrom;

interface Interface {

}

class Parent {

}

class Sub extends Parent implements Interface {

}


public class Test_isAssignFrom {
    public static void main(String[] args) {
        System.out.println("Sub.class.isAssignableFrom(Sub.class) = "
                + Sub.class.isAssignableFrom(Sub.class));

        System.out.println("Parent.class.isAssignableFrom(Sub.class) = "
                + Parent.class.isAssignableFrom(Sub.class));

        System.out.println("Interface.class.isAssignableFrom(Sub.class) = "
                + Interface.class.isAssignableFrom(Sub.class));
    }
}
