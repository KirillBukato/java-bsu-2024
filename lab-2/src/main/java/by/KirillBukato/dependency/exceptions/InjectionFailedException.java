package by.KirillBukato.dependency.exceptions;

import java.lang.reflect.Field;

public class InjectionFailedException extends RuntimeException {
    public InjectionFailedException(Object bean, Field field, Object value) {
        super("Failed to inject value " + value + " to field " + field.getName() + " of bean " + bean.getClass().getSimpleName());
    }
}
