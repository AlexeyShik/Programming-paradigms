package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public class CheckedDivide extends AbstractBinaryOperation {
    public CheckedDivide(Operation first, Operation second) {
        super(first, second, "/");
    }

    private int checkException(int left, int right) throws DivideByZeroException, OverflowException {
        if (Settings.exceptions) {
            if (right == 0)
                throw new DivideByZeroException("Divide", left + "/" + right);
            if (left == Integer.MIN_VALUE && right == -1)
                throw new OverflowException("Divide", left + "/" + right);
        }
        return left / right;
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
        return firstOperand.evaluate(x) / secondOperand.evaluate(x);
    }
}
