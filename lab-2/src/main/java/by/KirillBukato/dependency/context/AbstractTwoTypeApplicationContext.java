package by.KirillBukato.dependency.context;

import by.KirillBukato.dependency.annotation.Inject;
import by.KirillBukato.dependency.annotation.PostConstruct;
import by.KirillBukato.dependency.exceptions.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractTwoTypeApplicationContext extends AbstractApplicationContext {

    protected final Map<String, Object> singletons = new HashMap<>();
    private final Function<Class<?>, String> beanClassToName;

    protected AbstractTwoTypeApplicationContext(Map<String, Class<?>> map, Predicate<Class<?>> singletonPredicate ,Function<Class<?>, String> beanClassToName) {
        super(map, singletonPredicate);
        this.beanClassToName = beanClassToName;
    }

    public void start() {
        beanDefinitions.entrySet().stream()
                .filter(entry -> singletonPredicate.test(entry.getValue()))
                .forEach(entry -> singletons.put(entry.getKey(), instantiateBean(entry.getValue())));
        contextStatus = ContextStatus.STARTED;
        singletons.forEach(
                (name, obj) -> inject(name, obj, new ArrayList<>())
        );
        singletons.forEach(this::callPostConstruct);
    }

    @Override
    public Object getBean(String name) {
        return getBeanRecursive(name, new ArrayList<>());
    }

    protected Object getBeanRecursive(String name, ArrayList<String> beanNames) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException(name);
        }
        if (singletons.containsKey(name)) {
            return singletons.get(name);
        } else {
            Object bean = instantiateBean(beanDefinitions.get(name));
            inject(name, bean, beanNames);
            callPostConstruct(name, bean);
            return bean;
        }
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return getBean(clazz, new ArrayList<>());
    }

    private <T> T getBean(Class<T> clazz, ArrayList<String> beanNames) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        String name = beanClassToName.apply(clazz);
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException(name);
        }
        if (singletons.containsKey(name)) {
            return (T) singletons.get(name);
        } else {
            T bean = (T) instantiateBean(beanDefinitions.get(name));
            inject(name, bean, beanNames);
            callPostConstruct(name, bean);
            return bean;
        }
    }

    private void inject(String beanName, Object bean, ArrayList<String> beanNames) {
        if (beanNames.contains(beanName)) {
            throw new RecursiveInjectionException(beanName, beanNames);
        }
        beanNames.add(beanName);
        Arrays.stream(beanDefinitions.get(beanName).getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .forEach(field -> setBeanField(bean, field, getBean(field.getType(), beanNames)));
        beanNames.remove(beanName);
    }

    private void setBeanField(Object bean, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(bean, value);
        } catch (IllegalAccessException e) {
            throw new InjectionFailedException(bean, field, value);
        }
    }

    private void callPostConstruct(String beanName, Object bean) {
        Arrays.stream(beanDefinitions.get(beanName).getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .forEach(method -> callMethod(method, bean));
    }

    private void callMethod(Method method, Object bean) {
        try {
            method.setAccessible(true);
            method.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new PostConstructFailedException(method, bean);
        }
    }
}
