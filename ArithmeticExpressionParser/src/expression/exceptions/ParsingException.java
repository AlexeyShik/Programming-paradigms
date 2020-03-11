package expression.exceptions;

public class ParsingException extends Exception {
    public ParsingException(String message, int pos) {
        super(message + ". Crushed at index " + (pos - 1)  + ".");
    }
}
