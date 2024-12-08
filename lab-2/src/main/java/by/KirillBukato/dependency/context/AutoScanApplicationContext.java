package by.KirillBukato.dependency.context;


import by.KirillBukato.dependency.annotation.Bean;
import by.KirillBukato.dependency.annotation.BeanScope;
import org.reflections.Reflections;

import java.util.function.Function;
import java.util.stream.Collectors;

public class AutoScanApplicationContext extends AbstractTwoTypeApplicationContext {

    /**
     * Создает контекст, содержащий классы из пакета {@code packageName}, помеченные аннотацией {@code @Bean}.
     * <br/>
     * Если имя бина в анноации не указано ({@code name} пустой), оно берется из названия класса.
     * <br/>
     * Подразумевается, что у всех классов, переданных в списке, есть конструктор без аргументов.
     *
     * @param packageName имя сканируемого пакета
     */
    public AutoScanApplicationContext(String packageName) {
         super(new Reflections(packageName)
                .getTypesAnnotatedWith(Bean.class)
                .stream()
                .collect(
                        Collectors.toMap(
                                beanClass ->
                                beanClass.getAnnotation(Bean.class).name(),
                                Function.identity()
                        )
                ),
                 beanClass -> beanClass.getAnnotation(Bean.class).scope() == BeanScope.SINGLETON,
                 clazz2 -> clazz2.getAnnotation(Bean.class).name()
         );
    }
}
