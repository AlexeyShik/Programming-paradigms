package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public interface Operation {
    int evaluate(int x) throws OverflowException, DivideByZeroException;
    double evaluate(double x);
    int evaluate(int x, int y, int z) throws OverflowException, DivideByZeroException;
    String toString();
    String toMiniString();
    int getPriority();
}
