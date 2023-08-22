package ru.otus.aop.proxy;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("java:S106")
class Ioc {

    private Ioc() {}

    static Object createMyClass(Class<?> clazz, Class<?> parentInterface) {
        Constructor<?> constructor = clazz.getConstructors()[0];
        if (Arrays.asList(clazz.getInterfaces()).contains(parentInterface)) {
            InvocationHandler handler = null;
            List<Method> logMethods = Arrays.stream(clazz.getMethods())
                    .filter(m -> Arrays.stream(m.getAnnotations())
                            .anyMatch(an -> an.annotationType()
                                            .equals(Log.class)))
                    .toList();
            try {
                handler = new DemoInvocationHandler(constructor.newInstance(), logMethods);
                return Proxy.newProxyInstance(
                        Ioc.class.getClassLoader(),
                        clazz.getInterfaces(),
                        handler);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return constructor.newInstance();
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Object myClass;
        private final List<Method> logMethod;

        DemoInvocationHandler(Object myClass, List<Method> meth) {
            this.myClass = myClass;
            this.logMethod = meth;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logMethod.contains(myClass.getClass().getMethod(method.getName(), method.getParameterTypes()))) {
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
