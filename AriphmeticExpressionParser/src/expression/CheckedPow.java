package expression;

import expression.exceptions.OverflowException;

public class CheckedPow extends AbstractBinaryOperation {
    public CheckedPow(Operation first, Operation second) {
        super(first, second, "**");
    }

    private int checkMultiply(int x, int y, int n) {
        int t = x * y;
        if ((x != 0 && t / x != y) || (y != 0 && t / y != x)) {
            throw new OverflowException("Pow:", x + "**" + n);
        }
        return t;
    }

    private int calculatePow(int x0, int n0) {
        int x = x0, n = n0;
        int ans = 1;
        while (n > 0) {
            if (n % 2 == 1) {
                ans = checkMultiply(x, ans, n0);
                n--;
                if (n == 0)
                    break;
            }
            x = checkMultiply(x, x, n0);
            n /= 2;
        }
        return ans;
    }

    private int checkException(int left, int right) {
        if (Settings.exceptions) {
            if ((left == 0 && right == 0) || right < 0) {
                throw new IllegalArgumentException("Pow:" + left + "**" + right);
            }
        }
        return calculatePow(left, right);
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
        return 0;
    }
}
