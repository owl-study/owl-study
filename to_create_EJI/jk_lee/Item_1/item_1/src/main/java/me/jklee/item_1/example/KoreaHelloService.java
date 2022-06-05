package me.jklee.item_1.example;

public class KoreaHelloService implements HelloService {

    private String lang = "Korea";
    @Override
    public String hello() {
        return lang;
    }

    @Override
    public String bye() {
        return HelloService.super.bye();
    }

    @Override
    public String toString() {
        return "KoreaHelloService{" +
                "lang='" + lang + '\'' +
                '}';
    }
}
