package expression.expressions;

import expression.common.CommonExpression;

public class Variable<T> extends CommonExpression<T> {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public T evaluate(T x) {
        return x;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return name.equals("x") ? x : name.equals("y") ? y : z;
    }

    @Override
    public String toMiniString() {
        return name;
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
