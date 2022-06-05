package me.jklee.item_1.example;

public class EnglishHelloService implements HelloService {
    private String lang = "English";
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
        return "EnglishHelloService{" +
                "lang='" + lang + '\'' +
                '}';
    }
}
