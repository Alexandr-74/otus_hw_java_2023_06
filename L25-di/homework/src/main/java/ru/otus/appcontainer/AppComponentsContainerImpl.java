package ru.otus.appcontainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sun.jdi.InterfaceType;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.services.GameProcessor;

import static java.util.stream.Collectors.toMap;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... configs) {
        processConfig(configs);
    }

    private void processConfig(Class<?>... configs) {

        //Собираю методы в мапу с конфигурационным классом для каждого метода, чтобы потом была возможность для каждого метода вызвать его конфигурационный класс
        Map<Method, Class<?>> methodsWithConfigMap = new HashMap<>();
        for (Class<?> clazz : configs) {
            checkConfigClass(clazz);
            for (Method m : clazz.getMethods()) {
                methodsWithConfigMap.put(m, clazz);
            }
        }

        Map<Method,Integer> methodsWithOrder = new HashMap<>();
        Map<Method, String> methodsWithNames = new HashMap<>();

        //Заполняю мапы метод + его порядковый номер (для вызова по порядку) и мапы метод + его имя (для заполнении мапы с именами по порядку)
        extractNamesAndOrder(methodsWithOrder, methodsWithNames, methodsWithConfigMap);

        //Сортирую методы по порядку
         var orderedMethods = methodsWithOrder.entrySet().stream()
                 .sorted(Map.Entry.comparingByValue())
                 .map(Map.Entry::getKey)
                 .toList();

        //Вызываю методы по порядку, используя уже вызванные методы ранее методы как аргументы
        initContext(orderedMethods, methodsWithNames, methodsWithConfigMap);

    }

    private void extractNamesAndOrder(Map<Method,Integer> methodsWithOrder, Map<Method, String> methodsWithNames, Map<Method, Class<?>> methodsWithConfigMap) {
        for (Method method :  methodsWithConfigMap.keySet()) {
            if (Arrays.stream(method.getDeclaredAnnotations()).anyMatch(an->an.annotationType().equals(AppComponent.class))) {
                Annotation annotation = Arrays.stream(method.getDeclaredAnnotations())
                        .filter(an->an.annotationType().equals(AppComponent.class))
                        .findAny()
                        .orElse(null);
                if (annotation != null) {
                    String annotationName = ((AppComponent)annotation).name();
                    Integer annotationOrder = ((AppComponent)annotation).order();
                    methodsWithOrder.put(method, annotationOrder);
                    methodsWithNames.put(method, annotationName);
                }
            }
        }
    }
    private void initContext(List<Method> methods, Map<Method, String> methodsWithNames, Map<Method, Class<?>> methodsWithConfigMap) {
        for (Method method : methods) {
            try {
                List<Object> arguments = new ArrayList<>();
                for (var type : method.getParameterTypes()) {
                    arguments.add(appComponents.stream().filter(comp-> comp.getClass().equals(type)
                            || List.of(comp.getClass().getInterfaces()).contains(type)).findAny().orElseThrow());
                }
                Object inv;
                if (arguments.isEmpty()) {
                    inv = method.invoke(methodsWithConfigMap.get(method).getConstructor().newInstance());
                } else {
                    inv = method.invoke(methodsWithConfigMap.get(method).getConstructor().newInstance(), arguments.toArray());
                }
                if (appComponentsByName.containsKey(methodsWithNames.get(method))) {
                    throw new IllegalArgumentException();
                }
                appComponents.add(inv);
                appComponentsByName.put(methodsWithNames.get(method), inv);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
       var res = appComponents.stream()
               .filter(comp->comp.getClass().equals(componentClass)
                       || List.of(comp.getClass().getInterfaces()).contains(componentClass))
               .collect(Collectors.toList());
       if (res.size() != 1) {
           throw new IllegalArgumentException();
       } else {
           return componentClass.cast(res.get(0));
       }
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        var res = appComponentsByName.get(componentName);
        if (res == null) {
            throw new IllegalArgumentException();
        } else return (C) res;
    }
}
