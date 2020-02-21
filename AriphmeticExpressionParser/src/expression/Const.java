package expression;

public class Const implements Operation, Expression, TripleExpression, DoubleExpression {
    private Number value;

    public Const(Number value) {
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

    @Override
    public boolean equals(Object operation) {
        if (operation == null) {
            return false;
        }
        if (!(operation instanceof Operation))
            return false;
        if (!(operation instanceof Const))
            return false;
        return value.equals(((Const) operation).value);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
