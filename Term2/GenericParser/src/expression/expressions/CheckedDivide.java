package expression.expressions;
import expression.generic.ArithmeticOperator;

public class CheckedDivide<T> extends AbstractBinaryOperation<T> {
    public CheckedDivide(Operation<T> first, Operation<T> second, ArithmeticOperator<T> operator) {
        super(first, second, operator);
    }

    @Override
    protected T calculate(T left, T right) throws ArithmeticException {
        return operator.divide(left, right);
    }

    @Override
    public String toMiniString() {
        return new StringBuilder().append(firstOperand.toMiniString()).append("/").append(secondOperand.toMiniString()).toString();
    }
}
