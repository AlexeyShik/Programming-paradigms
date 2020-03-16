package expression;

public class CheckedLog extends AbstractBinaryOperation {
    public CheckedLog(Operation first, Operation second) {
        super(first, second, "//");
    }

    private int calculateLog(int x, int a) {
        int ans = 0;
        while (x >= a) {
            x /= a;
            ans++;
        }
        return ans;
    }

    private int checkException(int left, int right) {
        if (Settings.exceptions) {
            if (left <= 0 || right <= 0 || right == 1) {
                throw new IllegalArgumentException("Log:" + left + "//" + right);
            }
        }
        return calculateLog(left, right);
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
