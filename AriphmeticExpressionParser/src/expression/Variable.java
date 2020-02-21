package expression;

public class Variable implements Operation, Expression, TripleExpression, DoubleExpression {
    private String name;

    public Variable(String name) {
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
    public int hashCode() {
        return this.toString().hashCode();
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

    @Override
    public boolean equals(Object operation) {
        if (operation == null) {
            return false;
        }
        if (!(operation instanceof Operation))
            return false;
        if (!(operation instanceof Variable))
            return false;
        return name.equals(((Variable) operation).name);
    }
}
