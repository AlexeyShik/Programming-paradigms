package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public class CheckedPow2 extends AbstractUnaryOperation implements Operation, Expression, TripleExpression, DoubleExpression {
    public CheckedPow2(Operation a) {
        super(a, "pow2");
    }

    private void checkExceptions(int argument) throws OverflowException {
        if (argument < 0)
            throw new IllegalArgumentException("Non-positive argument in pow2");
        if (argument > 31)
            throw new OverflowException("Pow2", "the degree in pow2 is too high");
    }

    private int calculate(int argument) {
        return 1 << argument;
    }

    @Override
    public int evaluate(int x) throws OverflowException, DivideByZeroException {
        int current = operation.evaluate(x);
        if (Settings.exceptions)
            checkExceptions(current);
        return calculate(current);
    }

    @Override
    public double evaluate(double x) {
        return operation.evaluate(x) * operation.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) throws OverflowException, DivideByZeroException {
        int current = operation.evaluate(x, y, z);
        if (Settings.exceptions)
            checkExceptions(current);
        return calculate(current);
    }
}
