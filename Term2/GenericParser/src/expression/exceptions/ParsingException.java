package expression.exceptions;

public class ParsingException extends Exception {
    public ParsingException(String message, int pos) {
        super(new StringBuilder().append(message).append(". Crushed at index ").append(pos - 1).append(".").toString());
    }
}