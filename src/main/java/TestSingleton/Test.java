package TestSingleton;

public class Test {
    public static void main(String[] args) {
        A a1=new A();//je suis A
        A a2=new A();//je suis A
        B b1=B.getInstance(); //je suis B
        B b2=B.getInstance();
        System.out.println(a1);
        System.out.println(a2);
        System.out.println(b1);
        System.out.println(b2);

    }
}
