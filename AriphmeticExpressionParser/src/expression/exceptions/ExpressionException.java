package expression.exceptions;

public class ExpressionException extends Exception {
    public ExpressionException(String type, String message) {
        super(type + ": " + message);
    }
}