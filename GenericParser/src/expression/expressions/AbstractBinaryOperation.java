package expression.expressions;
import expression.common.CommonExpression;
import expression.generic.ArithmeticOperator;

public abstract class AbstractBinaryOperation<T> extends CommonExpression<T> {
    protected final Operation<T> firstOperand;
    protected final Operation<T> secondOperand;
    protected final ArithmeticOperator<T> operator;

    public AbstractBinaryOperation(Operation<T> first, Operation<T> second, ArithmeticOperator<T> op) {
        firstOperand = first;
        secondOperand = second;
        operator = op;
    }

    protected abstract T calculate(T left, T right) throws ArithmeticException;

    public T evaluate(T x) throws ArithmeticException {
        return calculate(firstOperand.evaluate(x), secondOperand.evaluate(x));
    }

    public T evaluate(T x, T y, T z) throws ArithmeticException {
        return calculate(firstOperand.evaluate(x, y, z), secondOperand.evaluate(x, y, z));
    }
}