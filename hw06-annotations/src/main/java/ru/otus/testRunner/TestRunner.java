package ru.otus.testRunner;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class TestRunner {

    public TestRunner() {
    }

    public void startTest(Class testClass) {

        try {
            //Нахожу список всех методов в классе
            List<Method> methods = Arrays.stream(testClass.getDeclaredMethods())
                    .collect(Collectors.toList());


            //Нахожу сами тесты
            List<Method> testMethods = methods
                    .stream()
                    .filter(method -> Arrays.stream(method.getDeclaredAnnotations())
                            .anyMatch(methodAnnotations -> methodAnnotations.annotationType().equals(Test.class)))
                    .collect(Collectors.toList());

            //Нахожу индексы тестов
            List<Integer> testIndexes = testMethods
                    .stream()
                    .map(methods::indexOf)
                    .collect(Collectors.toList());

            //Список для каждой тройки
            List<List<Method>> threes = new ArrayList<>();

            int lastTestMethod = 0;
            int currentTestMethod = 0;


            //группирую методы по тройкам
            do {
                threes.add(findThrees(methods, lastTestMethod, testIndexes.size() > currentTestMethod + 1 ? testIndexes.get(currentTestMethod + 1) : methods.size() - 1, testIndexes.get(currentTestMethod)));
                lastTestMethod = testIndexes.get(currentTestMethod);
                currentTestMethod += 1;
            } while (testIndexes.size() - 1 >= currentTestMethod);


            execute(threes, testClass);
        } catch (Exception e) {
            System.err.println("Вызов тестового класса завершился с ошибкой: " + e);
            e.printStackTrace();
        }
    }

    private void execute(List<List<Method>> threes, Class<?> testClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        int errors = 0;
        int success = 0;

        for (List<Method> three : threes) {
            //создаю объект тестового класса
            Constructor<?> constructor = testClass.getConstructor();
            Object testObject = constructor.newInstance();
            //выполняю методы
            for (Method meth : three) {
                try {
                    meth.setAccessible(true);
                    meth.invoke(testObject);
                    if (meth.isAnnotationPresent(Test.class)) {
                        success+=1;
                    }
                } catch (Exception e) {
                    if (meth.isAnnotationPresent(Test.class)) {
                        errors+=1;
                    }
                    System.err.println("Во время выполнения метода " + meth.getName() + " произошла ошибка " + e.getCause());
                    e.printStackTrace();
                }
            }
        }

        System.out.println("В результате запуска тестов было выполнено " + threes.size() + " тестов, из них: " + success + " выполнен(ы) успешно, " + errors + " неудачно.");

    }

    private List<Method> findThrees(List<Method> methods, int start, int finish, int testMethodIndex) {

        //возвращаю методы до самого теста с аннотацией before и после теста с аннотацией after плюс сам тест
        List<Method> result = new ArrayList<>();
        for (int i = start; i <= finish; i++) {
            Method currentMethod = methods.get(i);
            if (i < testMethodIndex && currentMethod.isAnnotationPresent(Before.class)) {
                result.add(currentMethod);
            } else if (testMethodIndex == i) {
                result.add(currentMethod);
            } else if (i > testMethodIndex && currentMethod.isAnnotationPresent(After.class)) {
                result.add(currentMethod);
            }
        }

        return result;
    }
}
