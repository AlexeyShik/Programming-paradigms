package expression.generic;

public interface ArithmeticOperator<T> {
    T parseNumber(final String number);
    T add(final T left, final T right);
    T subtract(final T left, final T right);
    T multiply(final T left, final T right);
    T divide(final T left, final T right);
    T negate(final T x);
    T count(final T x);
    T min(final T left, T right);
    T max(final T left, T right);
}