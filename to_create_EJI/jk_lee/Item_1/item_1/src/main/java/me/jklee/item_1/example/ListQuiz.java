package me.jklee.item_1.example;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Class.forName;

public class ListQuiz {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        /*
        Quiz : Comparator<Integer> desc = (o1, o2) -> o2-o1; 이 코드를 수정하지 않고
                내림차순에서 오름차순으로 변경하기

        */
        List<Integer> numbers = new ArrayList<>();
        numbers.add(100);
        numbers.add(20);
        numbers.add(44);
        numbers.add(3);

        System.out.println(numbers);

        Comparator<Integer> desc = (o1, o2) -> o2-o1;
        numbers.sort(desc);
        System.out.println(numbers);


        Class<?> aClass1 = forName("me.jklee.item_1.example.test");

        System.out.println(aClass1);
        Annotation[] annotations = aClass1.getAnnotations();
        AnnotatedType annotatedSuperclass = aClass1.getAnnotatedSuperclass();


/*
        Constructor<?> constructor = aClass.getConstructor();

        Object o = constructor.newInstance();
*/

    }
}
