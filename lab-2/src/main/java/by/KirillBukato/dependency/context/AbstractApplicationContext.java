package by.KirillBukato.dependency.context;

import by.KirillBukato.dependency.exceptions.ApplicationContextNotStartedException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public abstract class AbstractApplicationContext implements ApplicationContext {

    protected enum ContextStatus {
        NOT_STARTED,
        STARTED
    }

    protected final Map<String, Class<?>> beanDefinitions;

    public AbstractApplicationContext(Map<String, Class<?>> map) {
        this.beanDefinitions = map;
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

    protected ContextStatus contextStatus = ContextStatus.NOT_STARTED;
}
