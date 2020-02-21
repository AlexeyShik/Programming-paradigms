package expression;

public class Variable extends AbstractEquals implements Operation, Expression, TripleExpression, DoubleExpression {
    protected String name;

    Variable(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return name.equals("x") ? x : name.equals("y") ? y : z;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String toMiniString() {
        return name;
    }

    @Override
    public int getPriority() {
        return 0;
    }
}
