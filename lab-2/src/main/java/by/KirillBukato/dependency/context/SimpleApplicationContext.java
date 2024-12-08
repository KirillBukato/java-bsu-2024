package by.KirillBukato.dependency.context;

import by.KirillBukato.dependency.annotation.Bean;
import by.KirillBukato.dependency.annotation.BeanScope;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SimpleApplicationContext extends AbstractTwoTypeApplicationContext {

    /**
     * Создает контекст, содержащий классы, переданные в параметре.
     * <br/>
     * Если на классе нет аннотации {@code @Bean}, имя бина получается из названия класса, скоуп бина по дефолту
     * считается {@code Singleton}.
     * <br/>
     * Подразумевается, что у всех классов, переданных в списке, есть конструктор без аргументов.
     *
     * @param beanClasses классы, из которых требуется создать бины
     */

    public SimpleApplicationContext(Class<?>... beanClasses) {
        super(Arrays.stream(beanClasses).collect(
                Collectors.toMap(
                        beanClass ->
                                beanClass.isAnnotationPresent(Bean.class) ?
                                        beanClass.getAnnotation(Bean.class).name() :
                                        generateBeanName(beanClass),
                        Function.identity())),
                beanClass ->
                        !beanClass.isAnnotationPresent(Bean.class) ||
                                beanClass.getAnnotation(Bean.class).scope() == BeanScope.SINGLETON,
                clazz2 -> clazz2.isAnnotationPresent(Bean.class) ?
                        clazz2.getAnnotation(Bean.class).name() :
                        generateBeanName(clazz2)
        );
    }

    private static String generateBeanName(Class<?> clazz) {
        String name = clazz.getSimpleName();
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}
