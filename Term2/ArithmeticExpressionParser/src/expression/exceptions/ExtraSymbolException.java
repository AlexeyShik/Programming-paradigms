package expression.exceptions;

public class ExtraSymbolException extends ParsingException {
    public ExtraSymbolException(String message, int pos) {
        super(message, pos);
    }
}
