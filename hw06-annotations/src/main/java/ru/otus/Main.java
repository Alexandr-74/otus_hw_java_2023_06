package ru.otus;

import ru.otus.testClasses.TestClass1;
import ru.otus.testClasses.TestClass2;
import ru.otus.testRunner.TestRunner;

public class Main {
    public static void main(String[] args) {
        TestRunner testRunner = new TestRunner();
        testRunner.startTest(TestClass1.class);
        testRunner.startTest(TestClass2.class);
    }
}
