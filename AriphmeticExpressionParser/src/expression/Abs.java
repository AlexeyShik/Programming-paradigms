package expression;

public class Abs extends AbstractUnaryOperation implements Operation, Expression, TripleExpression, DoubleExpression {
    public Abs(Operation a) {
        super(a, "Abs");
    }

    @Override
    public int evaluate(int x) {
        return Math.abs(operation.evaluate(x));
    }

    @Override
    public double evaluate(double x) {
        return Math.abs(operation.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return Math.abs(operation.evaluate(x, y, z));
    }


}
