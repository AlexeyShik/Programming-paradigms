package expression.expressions;

import expression.generic.ArithmeticOperator;

public class CheckedSubtract<T> extends AbstractBinaryOperation<T> {
    public CheckedSubtract(Operation<T> first, Operation<T> second, ArithmeticOperator<T> operator) {
        super(first, second,  operator);
    }

    @Override
    protected T calculate(T left, T right) throws ArithmeticException {
        return operator.subtract(left, right);
    }

    @Override
    public String toMiniString() {
        return new StringBuilder().append(firstOperand.toMiniString()).append("-").append(secondOperand.toMiniString()).toString();
    }
}
