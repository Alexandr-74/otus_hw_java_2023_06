package ru.otus.testClasses;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestClass2 {

    int firstNumber;
    int secondNumber;

    @Test
    void checkSumm() {
        firstNumber = 4;
        secondNumber = 3;
        assert firstNumber+secondNumber == 7;
    }

    @After
    void checkSummAfter() {
        System.out.println("Тест завершен");
    }

    @Before
    void initNumners() {
        firstNumber=45;
        secondNumber = 5;
    }

    @Test
    void checkDevideNimbers() {
        assert firstNumber/secondNumber==9;
    }

    @Before
    void initNumbers() {
        firstNumber = 3;
        secondNumber = 3;
    }

    @Test
    void checkMultiply() {
        assert firstNumber*secondNumber == 9;
    }
}
