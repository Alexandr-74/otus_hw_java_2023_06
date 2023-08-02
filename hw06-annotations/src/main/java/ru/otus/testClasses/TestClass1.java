package ru.otus.testClasses;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.dto.SomeDto;

import java.util.Objects;

public class TestClass1 {
    SomeDto someDto;
    SomeDto someSecondDto;

    public TestClass1() {
    }

    @After
    void someAfter() {
        throw new IndexOutOfBoundsException();
//        System.out.println("Doing something");
    }

    @Before
    void initObj() {
        System.out.println("Create test object");
        someDto = new SomeDto();
        someDto.setAddress("someAddr");
        someDto.setName("someName");
        someDto.setAge(12);
    }

    @Test
    void checkObj() {
        System.out.println("Check test object");
        assert someDto.getAge() == 12;
        assert Objects.equals(someDto.getAddress(), "someAddr");
        assert Objects.equals(someDto.getName(), "someName");
    }

    @Before
    void initSecondTest() {
        System.out.println("Init second object");

        someSecondDto = new SomeDto();
        someSecondDto.setAddress("someSecondAddr");
        someSecondDto.setName("someSecondName");
        someSecondDto.setAge(54);
    }


    @After
    void deleteObj() {
        someDto.delete();
    }

    @Before
    void initOneMoreSecondTest() {
        System.out.println("Init more something for second test");
    }



    @Test
    void checkSecondObj() {
        System.out.println("Check test object");
        assert someSecondDto.getAge() == 54;
        assert Objects.equals(someSecondDto.getAddress(), "someSecondAddr");
        assert Objects.equals(someSecondDto.getName(), "someSecondName");
        throw new UnsupportedOperationException();
    }

    @After
    void deleteSecondObj() {
        someSecondDto.delete();
    }
}
