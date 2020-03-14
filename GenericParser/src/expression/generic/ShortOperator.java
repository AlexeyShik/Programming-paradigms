package expression.generic;

import expression.exceptions.DivideByZeroException;

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
        return super.binary(left, right, (x, y) -> (short)(x + y));
    }

    @Override
    public Short subtract(Short left, Short right) {
        return super.binary(left, right, (x, y) -> (short)(x - y));
    }

    @Override
    public Short multiply(Short left, Short right) {
        return super.binary(left, right, (x, y) -> (short)(x * y));
    }

    @Override
    public Short divide(Short left, Short right) {
        if (right == 0)
            throw new DivideByZeroException("Divide", left + "/" + right);
        return super.binary(left, right, (x, y) -> (short)(x / y));
    }

    @Override
    public Short negate(Short x) {
        return super.unary(x, y -> (short)-y);
    }

    @Override
    public Short count(Short x) {
        return super.unary(x, y -> (short)Integer.bitCount(Short.toUnsignedInt(y)));
    }

    @Override
    public Short min(Short left, Short right) {
        return super.binary(left, right, (x, y) -> (short)Integer.min(x, y));
    }

    @Override
    public Short max(Short left, Short right) {
        return super.binary(left, right, (x, y) -> (short)Integer.max(x, y));
    }
}
