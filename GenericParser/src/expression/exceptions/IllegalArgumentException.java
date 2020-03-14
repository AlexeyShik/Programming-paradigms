package expression.exceptions;

public class IllegalArgumentException extends ParsingException {
    public IllegalArgumentException(String message, int pos) {
        super(message, pos);
    }
}