package expression.common;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public interface Expression<T> extends ToMiniString {
    T evaluate(T x) throws OverflowException, DivideByZeroException;
}