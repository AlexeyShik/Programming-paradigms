package expression.generic;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

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
        if (needCheckExceptions && ((right > 0 && left > Integer.MAX_VALUE - right)
                || (right < 0 && left < Integer.MIN_VALUE - right))) {
            throw new OverflowException("Add", left + "+" + right);
        }
        return super.binary(left, right, Integer::sum);
    }

    @Override
    public Integer subtract(Integer left, Integer right) {
        if (needCheckExceptions && ((left >= 0 && right < 0 && left > Integer.MAX_VALUE + right)
                || (left < 0 && right > 0 && left < Integer.MIN_VALUE + right))) {
            throw new OverflowException("Subtract", left + "-" + right);
        }
        return super.binary(left, right, (x, y) -> x - y);
    }

    @Override
    public Integer multiply(Integer left, Integer right) { // TODO: определять переполнение без умножения
        if (needCheckExceptions) {
            if (left != 0 && right != 0) {
                int ans = left * right;
                if (ans / left != right || ans / right != left) {
                    throw new OverflowException("Multiply", left + "*" + right);
                }
            }
        }
        return super.binary(left, right, (x, y) -> x * y);
    }

    @Override
    public Integer divide(Integer left, Integer right) {
        if (right == 0) {
            throw new DivideByZeroException("Divide", left + "/" + right);
        }
        if (needCheckExceptions) {
            if (left == Integer.MIN_VALUE && right == -1) {
                throw new OverflowException("Divide", left + "/" + right);
            }
        }
        return super.binary(left, right, (x, y) -> x / y);
    }

    @Override
    public Integer negate(Integer x) {
        if (x == Integer.MIN_VALUE && needCheckExceptions) {
            throw new OverflowException("Negate", "-" + x);
        }
        return super.unary(x, y -> -y);
    }

    @Override
    public Integer count(Integer x) {
        return super.unary(x, Integer::bitCount);
    }

    @Override
    public Integer min(Integer left, Integer right) {
        return super.binary(left, right, Integer::min);
    }

    @Override
    public Integer max(Integer left, Integer right) {
        return super.binary(left, right, Integer::max);
    }
}
