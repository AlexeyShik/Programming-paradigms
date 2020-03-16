package expression.common;

import expression.expressions.Operation;

public abstract class CommonExpression<T> implements Operation<T> {
    @Override
    public abstract T evaluate(final T x);

    @Override
    public abstract T evaluate(final T x, final T y, final T z);

    @Override
    public abstract String toMiniString();
}
