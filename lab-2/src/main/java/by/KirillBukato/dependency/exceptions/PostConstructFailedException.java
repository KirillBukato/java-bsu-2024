package by.KirillBukato.dependency.exceptions;

import java.lang.reflect.Method;

public class PostConstructFailedException extends RuntimeException {
    public PostConstructFailedException(Method method, Object bean) {
        super("Failed to call post construct method " + method.getName() + " on bean " + bean.getClass().getName());
    }
}
