package TestSingleton;

public class B {

    private static B b;

    private B() {
        System.out.println("je suis B");
    }


    public static B getInstance() {
        if (b == null) {
            b = new B();
        }
        return b;
    }
}
