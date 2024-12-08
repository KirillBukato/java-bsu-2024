package by.KirillBukato.quizer.generators;

import by.KirillBukato.quizer.exceptions.NoSuitableGeneratorFoundException;
import by.KirillBukato.quizer.tasks.Task;
import by.KirillBukato.quizer.exceptions.InvalidGeneratorException;

import java.util.*;

public class GroupTaskGenerator<T extends Task> implements TaskGenerator<T> {
    /**
     * Конструктор с переменным числом аргументов
     *
     * @param generators генераторы, которые в конструктор передаются через запятую
     */
    @SafeVarargs
    public GroupTaskGenerator(TaskGenerator<? extends T>... generators) {
        this.generators = new ArrayList<>(Arrays.asList(generators));
        if (this.generators.isEmpty()) {
            throw new InvalidGeneratorException("GroupTaskGenerator must have at least one generator");
        }
    }

    /**
     * Конструктор, который принимает коллекцию генераторов
     *
     * @param generators генераторы, которые передаются в конструктор в Collection (например, {@link ArrayList})
     */
    public GroupTaskGenerator(Collection<TaskGenerator<? extends T>> generators) {
        this.generators = new ArrayList<>(generators);
    }

    /**
     * @return результат метода generate() случайного генератора из списка.
     * Если этот генератор выбросил исключение в методе generate(), выбирается другой.
     * Если все генераторы выбрасывают исключение, то и тут выбрасывается исключение.
     */
    @Override
    public T generate() {
        Collections.shuffle(generators);
        for (TaskGenerator<? extends T> generator : generators) {
            try {
                return generator.generate();
            } catch (RuntimeException ignored) {

            }
        }
        throw new NoSuitableGeneratorFoundException();
    }

    private final ArrayList<TaskGenerator<? extends T>> generators;
}
