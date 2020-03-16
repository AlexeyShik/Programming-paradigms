
package expression.parser;

import expression.exceptions.UnexpectedSymbolException;

public class StringSource implements Source {
    private final String data;
    private int pos;

    public int getPos() {
        return pos;
    }

    public int charAt(int i) {
        return data.charAt(i);
    }

    public StringSource(final String data) {
        this.data = data;
        this.pos = 0;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    public boolean hasNext(int offset) {
        return pos + offset < data.length();
    }

    public void backAt(int offset) {
        pos -= offset;
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    public char next(int offset) {
        return data.charAt(pos + offset);
    }

    @Override
    public UnexpectedSymbolException error(final String message) {
        return new UnexpectedSymbolException(message, getPos());
    }
}