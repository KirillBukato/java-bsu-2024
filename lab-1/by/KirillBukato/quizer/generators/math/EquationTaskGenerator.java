package by.KirillBukato.quizer.generators.math;

import by.KirillBukato.quizer.exceptions.InvalidGeneratorException;
import by.KirillBukato.quizer.tasks.math.EquationTask;
import by.KirillBukato.quizer.tasks.math.MathTask;

import java.util.EnumSet;
import java.util.Random;

public class EquationTaskGenerator extends AbstractMathTaskGenerator<EquationTask> {

    /**
     * Расширенная валидация генератора.
     * Если генерируются примеры с числами на отрезке [0,0] и операцией деления, то они всегда будут невалидны.
     * Иначе есть шанс, что сгенерируется валидный.
     */
    @Override
    public InvalidGeneratorException validateGenerator() {
        if (operationsIsDivisionAndMultiplication() && getMinNumber() == 0 && getMaxNumber() == 0) {
            return new InvalidGeneratorException("Task will always have zero division");
        } else return null;
    }

    /**
     * @param minNumber    минимальное число
     * @param maxNumber    максимальное число
     * @param operationSet множество разрешённых операций
     */
    public EquationTaskGenerator(int minNumber, int maxNumber, EnumSet<MathTask.Operation> operationSet) {
        super(minNumber, maxNumber, operationSet);
    }

    @Override
    public EquationTask generateUnvalidated() {
        Random random = new Random();
        return new EquationTask(getRandomNumber(), getRandomOperation(), getRandomNumber(),
                random.nextInt(2) == 0);
    }
}
