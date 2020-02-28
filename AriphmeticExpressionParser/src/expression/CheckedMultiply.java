package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public class CheckedMultiply extends AbstractBinaryOperation {
    public CheckedMultiply(Operation first, Operation second) {
        super(first, second, "*");
    }

    private int checkException(int left, int right) {
        if (Settings.exceptions && left != 0 && right != 0) {
            int ans = left * right;
            if (ans / left != right || ans / right != left)
                throw new OverflowException("Multiply", left + "*" + right);
        }
        return left * right;
    }

    @Override
    public int evaluate(int x) {
        int left = firstOperand.evaluate(x);
        int right = secondOperand.evaluate(x);
        return checkException(left, right);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int left = firstOperand.evaluate(x, y, z);
        int right = secondOperand.evaluate(x, y, z);
        return checkException(left, right);
    }

    @Override
    public double evaluate(double x) {
        return firstOperand.evaluate(x) * secondOperand.evaluate(x);
    }
}
