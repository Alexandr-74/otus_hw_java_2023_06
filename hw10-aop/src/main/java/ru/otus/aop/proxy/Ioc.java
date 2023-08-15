package ru.otus.aop.proxy;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("java:S106")
class Ioc {

    private Ioc() {}

    static Object createMyClass(Class<?> clazz) {
        Constructor<?> constructor = clazz.getConstructors()[0];
        InvocationHandler handler = null;
        try {
            handler = new DemoInvocationHandler(constructor.newInstance());
            return Proxy.newProxyInstance(
                    Ioc.class.getClassLoader(),
                    clazz.getInterfaces(),
                    handler);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Object myClass;

        DemoInvocationHandler(Object myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method currMeth = myClass.getClass().getMethod(method.getName(),method.getParameterTypes());
            if (myClass instanceof TestLogin
                    && Arrays.asList(myClass.getClass().getInterfaces()).contains(TestLoginInterface.class)
                    && Stream.of(currMeth.getDeclaredAnnotations())
                                .anyMatch(annotation -> Objects.equals(Log.class, annotation.annotationType()))) {
                System.out.println("executed method: " + method.getName() + ", param: " + Arrays.toString(args));
            }

            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
