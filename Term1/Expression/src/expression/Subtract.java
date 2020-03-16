package expression;

public class Subtract extends AbstractBinaryOperation {
    Subtract(Operation first, Operation second) {
        super(first, second, "-");
    }

    @Override
    public int evaluate(int x) {
        return firstOperand.evaluate(x) - secondOperand.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return firstOperand.evaluate(x, y, z) - secondOperand.evaluate(x, y, z);
    }

    @Override
    public double evaluate(double x) {
        return firstOperand.evaluate(x) - secondOperand.evaluate(x);
    }
}
