package expression.exceptions;

public class OverflowException extends ExpressionException {
    public OverflowException(String type, String message) {
        super(type, message);
    }
}
