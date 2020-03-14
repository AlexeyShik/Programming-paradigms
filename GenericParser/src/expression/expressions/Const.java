package expression.expressions;
import expression.common.CommonExpression;

public class Const<T> extends CommonExpression<T> {
    private T value;

    public Const(T value) {
        this.value = value;
    }

    @Override
    public T evaluate(T x) {
        return value;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }

    @Override
    public String toMiniString() {
        return String.valueOf(value);
    }
}
