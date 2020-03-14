package expression.generic;

import expression.exceptions.DivideByZeroException;

public class LongOperator extends AbstractOperator<Long> {
    public LongOperator(boolean flag) {
        needCheckExceptions = flag;
    }

    @Override
    public Long parseNumber(String number) {
        return super.parseNumber(number, Long::parseLong);
    }

    @Override
    public Long add(Long left, Long right) {
        return super.binary(left, right, Long::sum);
    }

    @Override
    public Long subtract(Long left, Long right) {
        return super.binary(left, right, (x, y) -> x - y);
    }

    @Override
    public Long multiply(Long left, Long right) {
        return super.binary(left, right, (x, y) -> x * y);
    }

    @Override
    public Long divide(Long left, Long right) {
        if (right == 0)
            throw new DivideByZeroException("Divide", left + "/" + right);
        return super.binary(left, right, (x, y) -> x / y);
    }

    @Override
    public Long negate(Long x) {
        return super.unary(x, y -> -y);
    }

    @Override
    public Long count(Long x) {
        return super.unary(x, y -> (long)Long.bitCount(y));
    }

    @Override
    public Long min(Long left, Long right) {
        return super.binary(left, right, Long::min);
    }

    @Override
    public Long max(Long left, Long right) {
        return super.binary(left, right, Long::max);
    }
}
