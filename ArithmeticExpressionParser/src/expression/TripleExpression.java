package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleExpression extends ToMiniString {
    int evaluate(int x, int y, int z) throws OverflowException, DivideByZeroException;
}
