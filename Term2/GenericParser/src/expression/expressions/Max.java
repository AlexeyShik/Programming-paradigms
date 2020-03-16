package expression.expressions;

import expression.generic.ArithmeticOperator;

public class Max<T> extends AbstractBinaryOperation<T>{
    public Max(Operation<T> first, Operation<T> second, ArithmeticOperator<T> operator) {
        super(first, second, operator);
    }

    @Override
    protected T calculate(T left, T right) throws ArithmeticException {
        return operator.max(left, right);
    }

    @Override
    public String toMiniString() {
        return new StringBuilder().append(firstOperand.toMiniString()).append("max").append(secondOperand.toMiniString()).toString();
    }
}
