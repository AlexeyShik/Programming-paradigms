package expression.generic;

import org.jetbrains.annotations.NotNull;
import java.util.function.Function;

@FunctionalInterface
interface BinaryFunction<T> {
    T apply(T left, T right);
}

@FunctionalInterface
interface Checker<T> {
    void checkExceptions(T left, T right);
}

public abstract class AbstractOperator<T extends Number> implements ArithmeticOperator<T> {
    protected boolean needCheckExceptions;

    protected T parseNumber(String number, @NotNull Function<String, T> F) {
        return F.apply(number);
    }

    protected T binary(T left, T right, @NotNull BinaryFunction<T> F, @NotNull Checker<T> checker) {
        checker.checkExceptions(left, right);
        return F.apply(left, right);
    }

    protected T unary(T x, @NotNull Function<T, T> F, @NotNull Function<T, T> checker) {
        checker.apply(x);
        return F.apply(x);
    }

    protected String getMessage(T l, String c, T r) {
        return new StringBuilder().append(l).append(c).append(r).toString();
    }
}