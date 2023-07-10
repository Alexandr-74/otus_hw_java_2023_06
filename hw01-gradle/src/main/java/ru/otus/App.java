package ru.otus;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

class App {

    public static void main(String[] args) {

        List<String> list = Arrays.asList("one", "two", "three", "four");
        List<List<String>> partitionedList = Lists.partition(list,2);
        System.out.println(partitionedList);
    }
}
