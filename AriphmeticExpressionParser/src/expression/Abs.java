package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public class Abs extends AbstractUnaryOperation implements Operation, Expression, TripleExpression, DoubleExpression {
    public Abs(Operation a) {
        super(a, "Abs");
    }

    @Override
    public int evaluate(int x) throws OverflowException, DivideByZeroException {
        return Math.abs(operation.evaluate(x));
    }

    @Override
    public double evaluate(double x) {
        return Math.abs(operation.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) throws OverflowException, DivideByZeroException {
        return Math.abs(operation.evaluate(x, y, z));
    }


}
