package by.KirillBukato.quizer.tasks.math;

import java.util.Objects;

public abstract class AbstractExpressionTask extends AbstractMathTask {

    public AbstractExpressionTask(int left, MathOperation operator, int right) {
        super(left, operator, right);
    }

    /**
     * Проверка на деление на ноль
     */
    @Override
    public boolean isValid() {
        return operator != MathOperation.DIVIDE || right != 0;
    }

    @Override
    public double computeAnswer() {
        return switch (operator) {
            case ADD -> left + right;
            case SUBTRACT -> left - right;
            case MULTIPLY -> left * right;
            case DIVIDE -> (double) left / right;
        };
    }

    public String getQuestion() {
        return Objects.toString(left) + " " + operator.getSymbol() + " " + Objects.toString(right) + " = ?";
    }
}
