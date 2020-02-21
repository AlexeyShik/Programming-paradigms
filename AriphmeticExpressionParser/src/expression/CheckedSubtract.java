package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public class CheckedSubtract extends AbstractBinaryOperation {
    public CheckedSubtract(Operation first, Operation second) {
        super(first, second, "-");
    }

    private int checkException(int left, int right) throws OverflowException {
        if (Settings.exceptions && ((left >= 0 && right < 0 && left > Integer.MAX_VALUE + right)
                || (left < 0 && right > 0 && left < Integer.MIN_VALUE + right)))
            throw new OverflowException("Subtract", left + "-" + right);
        return left - right;
    }

    @Override
    public int evaluate(int x) throws OverflowException, DivideByZeroException {
        int left = firstOperand.evaluate(x);
        int right = secondOperand.evaluate(x);
        return checkException(left, right);
    }

    @Override
    public int evaluate(int x, int y, int z) throws OverflowException, DivideByZeroException {
        int left = firstOperand.evaluate(x, y, z);
        int right = secondOperand.evaluate(x, y, z);
        return checkException(left, right);
    }

    @Override
    public double evaluate(double x) {
        return firstOperand.evaluate(x) - secondOperand.evaluate(x);
    }
}
