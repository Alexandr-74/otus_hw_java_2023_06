package ru.otus.dto;

public class SomeDto {
    private String name;
    private int age;
    private String address;


    public String getName() {
        return name;
    }

    public SomeDto() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public void delete() {
        System.out.println("Object was deleted!");
    }
}
