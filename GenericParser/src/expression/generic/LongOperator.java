package expression.generic;

import expression.exceptions.DivideByZeroException;

import java.util.function.Function;

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
        return super.binary(left, right, Long::sum, (l, r) -> {});
    }

    @Override
    public Long subtract(Long left, Long right) {
        return super.binary(left, right, (x, y) -> x - y, (l, r) -> {});
    }

    @Override
    public Long multiply(Long left, Long right) {
        return super.binary(left, right, (x, y) -> x * y, (l, r) -> {});
    }

    @Override
    public Long divide(Long left, Long right) {
        return super.binary(left, right, (x, y) -> x / y, (l, r) -> {
            if (r == 0)
                throw new DivideByZeroException("Divide", l + "/" + r);
        });
    }

    @Override
    public Long negate(Long x) {
        return super.unary(x, y -> -y, Function.identity());
    }

    @Override
    public Long count(Long x) {
        return super.unary(x, y -> (long)Long.bitCount(y), Function.identity());
    }

    @Override
    public Long min(Long left, Long right) {
        return super.binary(left, right, Long::min, (l, r) -> {});
    }

    @Override
    public Long max(Long left, Long right) {
        return super.binary(left, right, Long::max, (l, r) -> {});
    }
}
