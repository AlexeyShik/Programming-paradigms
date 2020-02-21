package expression;

public abstract class AbstractEquals {
    @Override
    public boolean equals(Object operation) {
        if (operation == null) {
            return false;
        }
        if (operation.getClass() != this.getClass()) {
            return false;
        }
        if (this instanceof Variable) {
            return ((Variable) operation).name.equals(((Variable) this).name);
        }
        if (this instanceof Const) {
            return ((Const) operation).value.equals(((Const) this).value);
        }
        if (!((AbstractBinaryOperation) this).identifier.equals(((AbstractBinaryOperation) operation).identifier)) {
            return false;
        }
        return ((AbstractBinaryOperation) this).firstOperand.equals(((AbstractBinaryOperation) operation).firstOperand)
                && ((AbstractBinaryOperation) this).secondOperand.equals(((AbstractBinaryOperation) operation).secondOperand);
    }

    @Override
    public int hashCode() {
        if (this instanceof Const) {
            return 31 * ((Const) this).value.hashCode();
        }
        if (this instanceof Variable) {
            return 37 * ((Variable) this).name.hashCode();
        }
        return 41 * ((AbstractBinaryOperation) this).identifier.hashCode()
                + 47 * ((AbstractBinaryOperation) this).firstOperand.hashCode()
                + 51 * ((AbstractBinaryOperation) this).secondOperand.hashCode();
    }
}