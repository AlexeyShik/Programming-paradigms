package expression.expressions;
import expression.generic.ArithmeticOperator;

public class CheckedNegate<T> extends AbstractUnaryOperation<T> {
    public CheckedNegate(Operation<T> expression, ArithmeticOperator<T> operator) {
        super(expression, operator);
    }

    @Override
    protected T calculate(T x) throws ArithmeticException {
        return operator.negate(x);
    }

    @Override
    public String toMiniString() {
        return new StringBuilder().append("-").append(super.operation.toMiniString()).toString();
    }
}