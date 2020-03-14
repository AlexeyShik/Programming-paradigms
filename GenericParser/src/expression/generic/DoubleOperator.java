package expression.generic;

public class DoubleOperator extends AbstractOperator<Double> {
    public DoubleOperator(boolean flag) {
        needCheckExceptions = flag;
    }

    @Override
    public Double parseNumber(String number) {
        return super.parseNumber(number, Double::parseDouble);
    }

    @Override
    public Double add(Double left, Double right) {
        return super.binary(left, right, Double::sum);
    }

    @Override
    public Double subtract(Double left, Double right) {
        return super.binary(left, right, (x, y) -> x - y);
    }

    @Override
    public Double multiply(Double left, Double right) {
        return super.binary(left, right, (x, y) -> x * y);
    }

    @Override
    public Double divide(Double left, Double right) {
        return super.binary(left, right, (x, y) -> x / y);
    }

    @Override
    public Double negate(Double x) {
        return super.unary(x, y -> -y);
    }

    @Override
    public Double count(Double x) {
        return super.unary(x, y -> (double)Long.bitCount(Double.doubleToLongBits(y)));
    }

    @Override
    public Double min(Double left, Double right) {
        return super.binary(left, right, Math::min);
    }

    @Override
    public Double max(Double left, Double right) {
        return super.binary(left, right, Math::max);
    }
}
