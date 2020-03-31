package expression.generic;

import expression.exceptions.DivideByZeroException;
import org.jetbrains.annotations.NotNull;
import java.math.BigInteger;
import java.util.function.Function;

public class BigIntegerOperator extends AbstractOperator<BigInteger> {
    public BigIntegerOperator(boolean flag) {
        needCheckExceptions = flag;
    }

    @Override
    public BigInteger parseNumber(String number) {
        return super.parseNumber(number, BigInteger::new);
    }

    @Override
    public BigInteger add(@NotNull BigInteger left, BigInteger right) {
        return super.binary(left, right, BigInteger::add, (l, r) -> {});
    }

    @Override
    public BigInteger subtract(@NotNull BigInteger left, BigInteger right) {
        return super.binary(left, right, BigInteger::subtract, (l, r) -> {});
    }

    @Override
    public BigInteger multiply(@NotNull BigInteger left, BigInteger right) {
        return super.binary(left, right, BigInteger::multiply, (l, r) -> {});
    }

    @Override
    public BigInteger divide(BigInteger left, @NotNull BigInteger right) {
        return super.binary(left, right, BigInteger::divide, (l, r) -> {
            if (r.equals(BigInteger.ZERO)) {
                throw new DivideByZeroException("Divide", getMessage(l, "/", r));
            }
        });
    }

    @Override
    public BigInteger negate(@NotNull BigInteger x) {
        return super.unary(x, BigInteger::negate, Function.identity());
    }

    @Override
    public BigInteger count(@NotNull BigInteger x) {
        return super.unary(x, y -> BigInteger.valueOf(y.bitCount()), Function.identity());
    }

    @Override
    public BigInteger min(@NotNull BigInteger left, BigInteger right) {
        return super.binary(left, right, BigInteger::min, (l, r) -> {});
    }

    @Override
    public BigInteger max(@NotNull BigInteger left, BigInteger right) {
        return super.binary(left, right, BigInteger::max, (l, r) -> {});
    }
}
