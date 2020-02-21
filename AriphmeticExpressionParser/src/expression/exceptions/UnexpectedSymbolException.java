package expression.exceptions;

public class UnexpectedSymbolException extends ParsingException {
    public UnexpectedSymbolException(String message, int pos) {
        super(message, pos);
    }
}
