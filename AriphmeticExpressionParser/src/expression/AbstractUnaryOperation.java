package expression;

public abstract class AbstractUnaryOperation implements Operation, Expression, TripleExpression, DoubleExpression {
    protected Operation operation;
    private String identifier;
    
    public AbstractUnaryOperation(Operation op, String id) {
        operation = op;
        identifier = id;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(identifier);
        str.append("(");
        str.append(operation.toMiniString());
        str.append(")");
        return str.toString();
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
        if (!(operation instanceof AbstractUnaryOperation))
            return false;
        if (!(((AbstractUnaryOperation) operation).identifier.equals(this.identifier)))
            return false;
        return this.operation.equals(operation);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
