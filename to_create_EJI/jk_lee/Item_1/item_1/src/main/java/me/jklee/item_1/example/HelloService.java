package me.jklee.item_1.example;

public interface HelloService {
    String hello();


    // static public String hi()
    static String hi() {
        prepareMessage();
        return "hi";
    }


    static String hi1() {
        prepareMessage();
        return "hi";
    }
    static void prepareMessage() {
    }

    default String bye() {
        return "bye";
    }
}
