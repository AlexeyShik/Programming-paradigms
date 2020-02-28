package expression;

public class RightShift extends AbstractBinaryOperation {
    public RightShift(Operation a, Operation b) {
        super(a, b, ">>");
    }

    @Override
    public int evaluate(int x) {
        return firstOperand.evaluate(x) >> secondOperand.evaluate(x);
    }

    @Override
    public double evaluate(double x) {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return firstOperand.evaluate(x, y, z) >> secondOperand.evaluate(x, y, z);
    }

    @Override
    public int getPriority() {
        return 0;
    }
}