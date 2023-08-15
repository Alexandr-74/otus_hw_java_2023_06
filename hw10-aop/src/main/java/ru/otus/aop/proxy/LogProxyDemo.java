package ru.otus.aop.proxy;

public class LogProxyDemo {
    public static void main(String[] args) {
        TestLoginInterface testLogin = (TestLoginInterface) Ioc.createMyClass(TestLogin.class);

        testLogin.calculation(6);
        testLogin.calculation(6, 2);
        testLogin.calculation(6, 2, 3);
    }
}
