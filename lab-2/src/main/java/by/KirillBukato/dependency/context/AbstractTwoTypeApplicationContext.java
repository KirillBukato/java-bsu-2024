package by.KirillBukato.dependency.context;

import by.KirillBukato.dependency.annotation.Inject;
import by.KirillBukato.dependency.annotation.PostConstruct;
import by.KirillBukato.dependency.exceptions.ApplicationContextNotStartedException;
import by.KirillBukato.dependency.exceptions.NoSuchBeanDefinitionException;

import java.lang.reflect.InvocationTargetException;
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
        singletons.forEach(this::inject);
        singletons.forEach(this::callPostConstruct);
    }

    @Override
    public Object getBean(String name) {
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
            inject(name, bean);
            callPostConstruct(name, bean);
            return bean;
        }
    }

    public <T> T getBean(Class<T> clazz, Function<Class<T>, String> function) {
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
            inject(name, bean);
            callPostConstruct(name, bean);
            return bean;
        }
    }

    private void inject(String beanName, Object bean) {
        Arrays.stream(beanDefinitions.get(beanName).getDeclaredFields())
                .toList()
                .forEach(
                        field -> {
                            if (field.isAnnotationPresent(Inject.class)) {
                                try {
                                    field.setAccessible(true);
                                    field.set(bean, getBean(field.getType()));
                                } catch (IllegalAccessException ignored) {

                                }
                            }
                        }
                );
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
