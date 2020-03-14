package expression.expressions;

import expression.generic.ArithmeticOperator;

public class Count<T> extends AbstractUnaryOperation<T> {
    public Count(Operation<T> expression, ArithmeticOperator<T> operator) {
        super(expression, operator);
    }

    @Override
    protected T calculate(T x) throws ArithmeticException {
        return operator.count(x);
    }

    @Override
    public String toMiniString() {
        return new StringBuilder().append("count").append(super.operation.toMiniString()).toString();
    }
}
