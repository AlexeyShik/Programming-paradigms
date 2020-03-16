package expression.parser;

import expression.exceptions.ParsingException;

public interface Source {
    ParsingException error(String message);
    boolean hasNext();
    char next();
}
