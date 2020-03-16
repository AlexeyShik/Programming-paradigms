package expression.exceptions;

public class DivideByZeroException extends ExpressionException {
    public DivideByZeroException(String type, String message) {
        super(type, message);
    }
}
