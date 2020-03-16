package expression;

public abstract class AbstractBinaryOperation extends AbstractEquals implements Operation, Expression, TripleExpression, DoubleExpression{
    protected final Operation firstOperand;
    protected final Operation secondOperand;
    protected final String identifier;
    protected final boolean commutative;
    protected final int priority;

    AbstractBinaryOperation(Operation first, Operation second, String id) {
        firstOperand = first;
        secondOperand = second;
        identifier = id;
        commutative = (id.equals("+") || id.equals("*"));
        priority = (id.equals("+") || id.equals("-") ? 1 : 2);
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

    boolean needBracket(Operation operand, boolean isSecond) { // ужать формулу
        return (operand.getPriority() == this.priority && isSecond && !this.commutative)
                || (operand instanceof Divide && this instanceof Multiply && isSecond)
                || (operand.getPriority() > 0 && operand.getPriority() < this.priority);
    }

    public void operandRelax(Operation operand, StringBuilder str, boolean isSecond) {
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
    public int getPriority() {
        return this.priority;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
