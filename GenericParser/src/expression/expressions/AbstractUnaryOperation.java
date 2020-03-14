package expression.expressions;
import expression.common.CommonExpression;
import expression.generic.ArithmeticOperator;

public abstract class AbstractUnaryOperation<T> extends CommonExpression<T> {
    protected Operation<T> operation;
    ArithmeticOperator<T> operator;

    public AbstractUnaryOperation(Operation<T> operation, ArithmeticOperator<T> op) {
        this.operation = operation;
        operator = op;
    }

    protected abstract T calculate(T x) throws ArithmeticException;

    public T evaluate(T x) throws ArithmeticException {
        return calculate(operation.evaluate(x));
    }

    public T evaluate(T x, T y, T z) throws ArithmeticException {
        return calculate(operation.evaluate(x, y, z));
    }
}
