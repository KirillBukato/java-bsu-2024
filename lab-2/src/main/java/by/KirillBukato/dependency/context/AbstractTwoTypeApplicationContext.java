package by.KirillBukato.dependency.context;

import by.KirillBukato.dependency.annotation.Inject;
import by.KirillBukato.dependency.annotation.PostConstruct;
import by.KirillBukato.dependency.exceptions.ApplicationContextNotStartedException;
import by.KirillBukato.dependency.exceptions.NoSuchBeanDefinitionException;
import by.KirillBukato.dependency.exceptions.RecursiveInjectionException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractTwoTypeApplicationContext extends AbstractApplicationContext {

    protected final Map<String, Object> singletons = new HashMap<>();

    public AbstractTwoTypeApplicationContext(Map<String, Class<?>> map) {
        super(map);
    }

    public void start(Predicate<Class<?>> singletonPredicate) {
        beanDefinitions.forEach(
                (beanName, beanClass) -> {
                    if (singletonPredicate.test(beanClass)) {
                        singletons.put(beanName, instantiateBean(beanClass));
                    }
                }
        );
        contextStatus = ContextStatus.STARTED;
        singletons.forEach(
                (name, obj) -> inject(name, obj, new ArrayList<>())
        );
        singletons.forEach(this::callPostConstruct);
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
    public Object getBean(String name) {
        return getBeanRecursive(name, new ArrayList<>());
    }

    public <T> T getBean(Class<T> clazz, Function<Class<T>, String> function, ArrayList<String> beanNames) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        String name = function.apply(clazz);
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

    protected abstract <T> T getBean(Class<T> clazz, ArrayList<String> beanNames);

    private void inject(String beanName, Object bean, ArrayList<String> beanNames) {
        if (beanNames.contains(beanName)) {
            throw new RecursiveInjectionException(beanName, beanNames);
        }

        beanNames.add(beanName);
        Arrays.stream(beanDefinitions.get(beanName).getDeclaredFields())
                .toList()
                .forEach(
                        field -> {
                            if (field.isAnnotationPresent(Inject.class)) {
                                try {
                                    field.setAccessible(true);
                                    field.set(bean, getBean(field.getType(), beanNames));
                                } catch (IllegalAccessException ignored) {

                                }
                            }
                        }
                );
        beanNames.remove(beanName);
    }

    private void callPostConstruct(String beanName, Object bean) {
        Arrays.stream(beanDefinitions.get(beanName).getDeclaredMethods())
                .toList()
                .forEach(
                        method -> {
                            if (method.isAnnotationPresent(PostConstruct.class)) {
                                try {
                                    method.setAccessible(true);
                                    method.invoke(bean);
                                } catch (IllegalAccessException | InvocationTargetException ignored) {

                                }
                            }
                        }
                );
    }
}
