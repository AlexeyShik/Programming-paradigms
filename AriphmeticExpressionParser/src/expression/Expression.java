package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public interface Expression extends ToMiniString {
    int evaluate(int x) throws OverflowException, DivideByZeroException;
}
