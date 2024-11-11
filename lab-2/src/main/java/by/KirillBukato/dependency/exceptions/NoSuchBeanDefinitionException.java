package by.KirillBukato.dependency.exceptions;

public class NoSuchBeanDefinitionException extends RuntimeException {
    public NoSuchBeanDefinitionException(String name) {
        super("Bean " + name + " is not defined");
    }
}
