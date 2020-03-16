package expression;

public abstract class AbstractBinaryOperation implements Operation, Expression, TripleExpression, DoubleExpression {
    protected final Operation firstOperand;
    protected final Operation secondOperand;
    protected final String identifier;
    protected final boolean commutative;
    protected final int priority;
    protected final boolean needCheckException;

    public AbstractBinaryOperation(Operation first, Operation second, String id) {
        firstOperand = first;
        secondOperand = second;
        identifier = id;
        commutative = (id.equals("+") || id.equals("*"));
        priority = (id.equals("+") || id.equals("-") ? 1 : 2);
        needCheckException = !(id.equals(">>") || id.equals("<<"));
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("(");
        str.append(firstOperand.toString());
        str.append(" ");
        str.append(identifier);
        str.append(" ");
        str.append(secondOperand.toString());
        str.append(")");
        return str.toString();
    }

    private boolean needBracket(Operation operand, boolean isSecond) { // ужать формулу
        return (operand.getPriority() == this.priority && isSecond && !this.commutative)
                || (operand instanceof CheckedDivide && this instanceof CheckedMultiply && isSecond)
                || (operand.getPriority() > 0 && operand.getPriority() < this.priority);
    }

    private void operandRelax(Operation operand, StringBuilder str, boolean isSecond) {
        if (isSecond)
            str.append(" ");
        if (needBracket(operand, isSecond))
            str.append("(");
        str.append(operand.toMiniString());
        if (needBracket(operand, isSecond))
            str.append(")");
        if (!isSecond)
            str.append(" ");
    }

    @Override
    public String toMiniString() {
        StringBuilder str = new StringBuilder();
        operandRelax(firstOperand, str, false);
        str.append(identifier);
        operandRelax(secondOperand, str, true);
        return str.toString();
    }

    @Override
    public boolean equals(Object operation) {
        if (operation == null) {
            return false;
        }
        if (!(operation instanceof Operation))
            return false;
        if (!operation.getClass().getName().equals(this.getClass().getName()))
            return false;
        return operation.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public int getPriority() {
        return this.priority;
    }
}