package expression.generic;

import expression.exceptions.DivideByZeroException;

import java.util.function.Function;

public class ShortOperator extends AbstractOperator<Short> {
    public ShortOperator(boolean flag) {
        needCheckExceptions = flag;
    }

    @Override
    public Short parseNumber(String number) {
        return super.parseNumber(number, (s) -> (short)Integer.parseInt(s));
    }

    @Override
    public Short add(Short left, Short right) {
        return super.binary(left, right, (x, y) -> (short)(x + y), (l, r) -> {});
    }

    @Override
    public Short subtract(Short left, Short right) {
        return super.binary(left, right, (x, y) -> (short)(x - y), (l, r) -> {});
    }

    @Override
    public Short multiply(Short left, Short right) {
        return super.binary(left, right, (x, y) -> (short)(x * y), (l, r) -> {});
    }

    @Override
    public Short divide(Short left, Short right) {
        return super.binary(left, right, (x, y) -> (short) (x / y), (l, r) -> {
            if (r == 0) {
                throw new DivideByZeroException("Divide", l + "/" + r);
            }
        });
    }

    @Override
    public Short negate(Short x) {
        return super.unary(x, y -> (short)-y, Function.identity());
    }

    @Override
    public Short count(Short x) {
        return super.unary(x, y -> (short)Integer.bitCount(Short.toUnsignedInt(y)), Function.identity());
    }

    @Override
    public Short min(Short left, Short right) {
        return super.binary(left, right, (x, y) -> (short)Integer.min(x, y), (l, r) -> {});
    }

    @Override
    public Short max(Short left, Short right) {
        return super.binary(left, right, (x, y) -> (short)Integer.max(x, y), (l, r) -> {});
    }
}
