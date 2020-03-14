package expression.exceptions;

public class ExpressionException extends RuntimeException {
    public ExpressionException(String type, String message) {
        super(new StringBuilder().append(type).append(": ").append(message).toString());
    }
}