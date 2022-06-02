package me.jklee.item_1.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListQuiz {
    public static void main(String[] args) {
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
    }
}
