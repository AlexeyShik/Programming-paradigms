package expression.generic;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

import java.util.function.Function;

public class IntegerOperator extends AbstractOperator<Integer> {
    public IntegerOperator(boolean flag) {
        needCheckExceptions = flag;
    }

    @Override
    public Integer parseNumber(String number) {
        return super.parseNumber(number, Integer::parseInt);
    }

    @Override
    public Integer add(Integer left, Integer right) {
        return super.binary(left, right, Integer::sum, (l, r) -> {
            if (needCheckExceptions && ((r > 0 && l > Integer.MAX_VALUE - r)
                    || (r < 0 && l < Integer.MIN_VALUE - r))) {
                throw new OverflowException("Add", l + "+" + r);
            }
        });
    }

    @Override
    public Integer subtract(Integer left, Integer right) {
        return super.binary(left, right, (x, y) -> x - y, (l, r) -> {
            if (needCheckExceptions && ((l >= 0 && r < 0 && l > Integer.MAX_VALUE + r)
                    || (l < 0 && r > 0 && l < Integer.MIN_VALUE + r))) {
                throw new OverflowException("Subtract", l + "-" + r);
            }
        });
    }

    @Override
    public Integer multiply(Integer left, Integer right) { // TODO: определять переполнение без умножения
        return super.binary(left, right, (x, y) -> x * y, (l, r) -> {
            if (needCheckExceptions) {
                if (l != 0 && r != 0) {
                    int ans = l * r;
                    if (ans / l != r || ans / r != l) {
                        throw new OverflowException("Multiply", l + "*" + r);
                    }
                }
            }
        });
    }

    @Override
    public Integer divide(Integer left, Integer right) {
        return super.binary(left, right, (x, y) -> x / y, (l, r) -> {
            if (r == 0) {
                throw new DivideByZeroException("Divide", l + "/" + r);
            }
            if (needCheckExceptions) {
                if (l == Integer.MIN_VALUE && r == -1) {
                    throw new OverflowException("Divide", l + "/" + r);
                }
            }
        });
    }

    @Override
    public Integer negate(Integer x) {
        return super.unary(x, y -> -y, arg -> {
            if (arg == Integer.MIN_VALUE && needCheckExceptions) {
                throw new OverflowException("Negate", "-" + arg);
            }
            return arg;
        });
    }

    @Override
    public Integer count(Integer x) {
        return super.unary(x, Integer::bitCount, Function.identity());
    }

    @Override
    public Integer min(Integer left, Integer right) {
        return super.binary(left, right, Integer::min, (l, r) -> {});
    }

    @Override
    public Integer max(Integer left, Integer right) {
        return super.binary(left, right, Integer::max, (l, r) -> {});
    }
}
