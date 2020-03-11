package expression;

public class Square extends AbstractUnaryOperation implements Operation, Expression, TripleExpression, DoubleExpression {
    public Square(Operation a) {
        super(a, "square");
    }

    @Override
    public int evaluate(int x) {
        return operation.evaluate(x) * operation.evaluate(x);
    }

    @Override
    public double evaluate(double x) {
        return operation.evaluate(x) * operation.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return operation.evaluate(x, y, z) * operation.evaluate(x, y, z);
    }
}
