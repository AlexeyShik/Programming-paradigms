package expression.expressions;

import expression.common.Expression;
import expression.common.TripleExpression;
import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public interface Operation<T> extends TripleExpression<T>, Expression<T> {
    T evaluate(final T x) throws OverflowException, DivideByZeroException;
    T evaluate(final T x, final T y, final T z) throws OverflowException, DivideByZeroException;}
