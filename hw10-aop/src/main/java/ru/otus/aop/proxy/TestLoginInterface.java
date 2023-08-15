package ru.otus.aop.proxy;

public interface TestLoginInterface {

    void calculation(int param);
    public void calculation(int param, int param1);

    @Log
    public void calculation(int param, int param1, int param2);
}
