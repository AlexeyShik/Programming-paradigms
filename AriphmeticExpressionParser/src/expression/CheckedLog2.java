package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public class CheckedLog2 extends AbstractUnaryOperation implements Operation, Expression, TripleExpression, DoubleExpression {
    public CheckedLog2(Operation a) {
        super(a, "log2");
    }

    private void checkExceptions(int argument) {
        if (argument <= 0)
            throw new IllegalArgumentException("Non-positive argument in log2");
    }

    private int calculate(int argument) {
        int ans = 0;
        while (argument > 1) {
            ans++;
            argument /= 2;
        }
        return ans;
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
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) throws OverflowException, DivideByZeroException {
        int current = operation.evaluate(x, y ,z);
        if (Settings.exceptions)
            checkExceptions(current);
        return calculate(current);
    }
}
