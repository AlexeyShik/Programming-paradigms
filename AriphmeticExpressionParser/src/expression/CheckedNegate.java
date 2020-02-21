package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public class CheckedNegate implements Operation, TripleExpression, DoubleExpression, Expression {
    private Operation expression;

    public CheckedNegate(Operation expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "-(" + expression.toString() + ")";
    }

    @Override
    public String toMiniString() {
        boolean hasBrackets = expression instanceof AbstractBinaryOperation;
        return "-" + (hasBrackets ? "(" : "") + expression.toMiniString() + (hasBrackets ? ")" : "");
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int evaluate(int x) throws OverflowException, DivideByZeroException {
        return -expression.evaluate(x);
    }

    @Override
    public double evaluate(double x) {
        return -expression.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) throws OverflowException, DivideByZeroException {
        int argument = expression.evaluate(x, y, z);
        if (Settings.exceptions)
            checkException(argument);
        return -argument;
    }

    private void checkException(int argument) throws OverflowException {
        if (argument == Integer.MIN_VALUE)
            throw new OverflowException("Negate", "-" + argument);
    }
}