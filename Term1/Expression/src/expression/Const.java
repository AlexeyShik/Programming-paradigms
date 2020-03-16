package expression;

public class Const extends AbstractEquals implements Operation, Expression, TripleExpression, DoubleExpression {
    protected Number value;

    Const(Number value) {
        this.value = value;
    }

    @Override
    public int evaluate(int x) {
        return value.intValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }

    @Override
    public double evaluate(double x) {
        return value.doubleValue();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public String toMiniString() {
        return this.toString();
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
