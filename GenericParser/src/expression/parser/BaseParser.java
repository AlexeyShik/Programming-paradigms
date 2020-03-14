package expression.parser;

import expression.exceptions.ParsingException;

public class BaseParser {
    protected StringSource source;
    protected char ch;

    protected void setSource(StringSource source) {
        this.source = source;
        nextChar();
    }

    protected void nextChar() {
        ch = source.hasNext() ? source.next() : '\0';
    }

    protected boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    protected boolean test(final String expected) {
        for (int i = 0; i < expected.length(); ++i) {
            if (!source.hasNext(i) || source.next(i) != expected.charAt(i))
                return false;
        }
        return true;
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch)) {
            nextChar();
        }
    }

    protected ParsingException error(final String message) {
        return source.error(message);
    }
}