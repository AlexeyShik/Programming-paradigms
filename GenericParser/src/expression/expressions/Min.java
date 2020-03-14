package expression.expressions;

import expression.generic.ArithmeticOperator;

public class Min<T> extends AbstractBinaryOperation<T>{
    public Min(Operation<T> first, Operation<T> second, ArithmeticOperator<T> operator) {
        super(first, second, operator);
    }

    @Override
    protected T calculate(T left, T right) throws ArithmeticException {
        return operator.min(left, right);
    }

    @Override
    public String toMiniString() {
        return new StringBuilder().append(firstOperand.toMiniString()).append("min").append(secondOperand.toMiniString()).toString();
    }
}
