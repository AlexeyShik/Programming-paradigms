package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public class LeftShift extends AbstractBinaryOperation {
    public LeftShift(Operation a, Operation b) {
        super(a, b, "<<");
    }

    @Override
    public int evaluate(int x) throws OverflowException, DivideByZeroException {
        return firstOperand.evaluate(x) << secondOperand.evaluate(x);
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) throws OverflowException, DivideByZeroException {
        return firstOperand.evaluate(x, y, z) << secondOperand.evaluate(x, y, z);
    }

    @Override
    public int getPriority() {
        return 0;
    }
}