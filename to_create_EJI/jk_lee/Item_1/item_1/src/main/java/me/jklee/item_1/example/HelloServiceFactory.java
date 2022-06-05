package me.jklee.item_1.example;

public class HelloServiceFactory {
    public static HelloService of(String lang) {
        if (lang.equals("ko")) {
            return new KoreaHelloService();
        } else {
            return new EnglishHelloService();
        }

    }




    public static void main(String[] args) {
        HelloService ko = HelloServiceFactory.of("en");
        System.out.println(ko);
    }

}
