package by.KirillBukato.dependency.context;

import by.KirillBukato.dependency.exceptions.ApplicationContextNotStartedException;
import by.KirillBukato.dependency.exceptions.NoSuchBeanDefinitionException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Predicate;

public abstract class AbstractApplicationContext implements ApplicationContext {

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    protected final Map<String, Class<?>> beanDefinitions;
    protected final Predicate<Class<?>> singletonPredicate;
    protected ContextStatus contextStatus = ContextStatus.NOT_STARTED;

    protected AbstractApplicationContext(Map<String, Class<?>> map, Predicate<Class<?>> singletonPredicate) {
        this.beanDefinitions = map;
        this.singletonPredicate = singletonPredicate;
    }

    protected <T> T instantiateBean(Class<T> beanClass) {
        try {
            return beanClass.getConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRunning() {
        return contextStatus == ContextStatus.STARTED;
    }

    public boolean containsBean(String name) {
        if (!isRunning()) {
            throw new ApplicationContextNotStartedException();
        }
        return beanDefinitions.containsKey(name);
    }

    public boolean isPrototype(String name) {
        return !isSingleton(name);
    }

    public boolean isSingleton(String name) {
        if (!beanDefinitions.containsKey(name)) {
            throw new NoSuchBeanDefinitionException(name);
        }
        return singletonPredicate.test(beanDefinitions.get(name));
    }
}
