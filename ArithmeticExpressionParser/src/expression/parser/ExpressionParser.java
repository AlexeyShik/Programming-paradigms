package expression.parser;

import expression.*;
import expression.exceptions.*;
import expression.exceptions.IllegalArgumentException;
import static expression.parser.Utility.*;

public class ExpressionParser extends BaseParser implements Parser {
    private String parseToken() throws ParsingException {
        skipWhitespace();
        if (Character.isDigit(ch)) {
            return getConst().toString();
        } else if (checkOperation()) {
            if (!needOperation && ch != '-' && ch != '(') {
                throw new IllegalArgumentException("No argument expected", source.getPos());
            }
            return getOperation();
        } else {
            StringBuilder str = new StringBuilder();
            while (!isEndOfInput() && !checkOperation()) {
                if (ch != '\0' && !USABLE_SYMBOLS.contains(ch)) {
                    throw new UnexpectedSymbolException("Unexpected symbol expected", source.getPos());
                }
                str.append(ch);
                nextChar();
                skipWhitespace();
            }
            return str.toString();
        }
    }

    private TripleExpression parseSimpleExpression() throws ParsingException {
        String token = parseToken();
        if (token.isEmpty()) {
            throw new IllegalArgumentException("No argument expected", source.getPos());
        }
        if (Character.isDigit(token.charAt(0))) {
            try {
                return new Const(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                throw new IllegalConstException("Overflow constant", source.getPos());
            }
        } else if (token.equals("(")) {
            expect('(');
            TripleExpression result = parseBinaryExpression(MAX_PRIORITY);
            if (!test(')')) {
                throw new IllegalArgumentException("Missed closing bracket", source.getPos());
            }
            return result;
        } else if (VARIABLES.contains(token)) {
            return new Variable(token);
        } else {
            if (token.equals("-")) {
                expect(token);
                StringBuilder current = getConst();
                if (current.length() > 0) {
                    return buildNegativeConst(current);
                }
            }
            return getUnaryExpression((Operation) parseSimpleExpression(), token);
        }
    }

    private TripleExpression parseBinaryExpression(int priority) throws ParsingException{
        TripleExpression left = parseSimpleExpression();
        while (true) {
            needOperation = true;
            String operation = parseToken();
            needOperation = false;
            if (operation.isEmpty()) {
                return left;
            }
            if (!BINARY.containsKey(operation) && !operation.equals(")"))
                throw new IllegalArgumentException("Illegal binary operation expected", source.getPos());
            if (PRIORITIES.get(operation) >= priority) {
                source.backAt(operation.length());
                updateChar();
                return left;
            } else {
                expect(operation);
                TripleExpression right = parseBinaryExpression(PRIORITIES.get(operation));
                left = getBinaryExpression((Operation) left, (Operation) right, operation);
            }
        }
    }

    public TripleExpression parse(String expression) throws ParsingException {
        setSource(new StringSource(expression));
        needOperation = false;
        TripleExpression ans = parseBinaryExpression(MAX_PRIORITY);
        if (!isEndOfInput()) {
            throw new ExtraSymbolException("Unexpected closing bracket", source.getPos());
        }
        return ans;
    }

    private TripleExpression getUnaryExpression(Operation argument, String operation) throws ParsingException {
        if (operation.equals("-") && argument instanceof Const)
            return new Const(-1 * argument.evaluate(0, 0, 0));
        try {
            return (TripleExpression) UNARY.get(operation).getConstructor(Operation.class).newInstance((Operation)argument);
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal unary operation expected", source.getPos());
        }
    }

    private TripleExpression getBinaryExpression(Operation left, Operation right, String operation) throws IllegalArgumentException {
        try {
            return (TripleExpression) BINARY.get(operation).getConstructor(
                    Operation.class, Operation.class).newInstance((Operation)left, (Operation)right);
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal binary operation expected", source.getPos());
        }
    }

    private boolean checkOperation() {
        return FIRST_CHAR_TO_OPERATION.containsKey(ch);
    }

    private String getOperation() {
        if (isEndOfInput() || !checkOperation())
            return "";
        String ans = FIRST_CHAR_TO_OPERATION.get(ch);
        if (ans.equals("*") && source.charAt(source.getPos()) == '*')
            ans = "**";
        if (ans.equals("/") && source.charAt(source.getPos()) == '/')
            ans = "//";
        return ans;
    }

    private TripleExpression buildNegativeConst(StringBuilder current) throws IllegalConstException {
        StringBuilder toCast = new StringBuilder();
        toCast.append('-');
        toCast.append(current.toString());
        try {
            return new Const(Integer.parseInt(toCast.toString()));
        } catch (NumberFormatException e) {
            throw new IllegalConstException("Overflow constant", source.getPos());
        }
    }

    private StringBuilder getConst() throws IllegalConstException {
        skipWhitespace();
        StringBuilder str = new StringBuilder();
        if (Character.isDigit(ch)) {
            while (Character.isDigit(ch)) {
                str.append(ch);
                nextChar();
            }
        }
        skipWhitespace();
        if (Character.isDigit(ch)) {
            throw new IllegalConstException("Unexpected const", source.getPos());
        }
        return str;
    }

    private boolean isEndOfInput() {
        return test('\0');
    }
}
