package expression.parser;

import expression.common.CommonExpression;
import expression.exceptions.*;
import expression.expressions.*;
import expression.exceptions.IllegalArgumentException;
import expression.generic.ArithmeticOperator;
import org.jetbrains.annotations.NotNull;
import static expression.parser.Utility.*;

public class ExpressionParser<T extends Number> extends BaseParser {
    private final ArithmeticOperator<T> mode;

    public ExpressionParser(ArithmeticOperator<T> mode) {
        this.mode = mode;
    }

    @NotNull
    private String parseToken(boolean needOperation) throws ParsingException {
        skipWhitespace();
        if (isEndOfInput()) {
            return "";
        }
        if (Character.isDigit(ch)) {
            if (needOperation) {
                throw new IllegalConstException("Unexpected const", source.getPos());
            }
            return getConst().toString();
        }
        if (!isCorrectFirst(ch)) {
            throw new IllegalArgumentException("Unexpected symbol", source.getPos());
        }
        return getToken();
    }

    @NotNull
    private String getToken() {
        StringBuilder token = new StringBuilder(Character.toString(ch));
        while (isCorrectPrefix(token.toString())) {
            nextChar();
            if (isEndOfInput()) {
                return token.toString();
            }
            token.append(ch);
        }
        token.deleteCharAt(token.length() - 1);
        return token.toString();
    }


    private boolean isCorrectFirst(Character character) {
        return Character.isJavaIdentifierStart(character) || UNARY.contains(Character.toString(character))
                || BINARY.contains(Character.toString(character));
    }

    private boolean isCorrectPrefix(@NotNull String token) {
        if (Character.isJavaIdentifierPart(token.charAt(token.length() - 1)) && Character.isJavaIdentifierStart(token.charAt(0))) {
            return true;
        }
        for (String unary : UNARY) {
            if (unary.substring(0, Math.min(token.length(), unary.length())).equals(token)) {
                return true;
            }
        }
        for (String binary : BINARY) {
            if (binary.substring(0, Math.min(token.length(), binary.length())).equals(token)) {
                return true;
            }
        }
        return false;
    }

    private CommonExpression<T> parseSimpleExpression() throws ParsingException {
        String token = parseToken(false);
        if (token.isEmpty()) {
            throw new IllegalArgumentException("No argument found", source.getPos());
        }
        if (Character.isDigit(token.charAt(0))) {
            try {
                return new Const<>(mode.parseNumber(token));
            } catch (NumberFormatException e) {
                throw new IllegalConstException("Overflow constant", source.getPos());
            }
        } else if (token.equals("(")) {
            CommonExpression<T> result = parseBinaryExpression(MAX_PRIORITY);
            if (!test(')')) {
                throw new IllegalArgumentException("Missed closing bracket", source.getPos());
            }
            return result;
        } else if (VARIABLES.contains(token)) {
            return new Variable<>(token);
        } else {
            if (token.equals("-")) {
                StringBuilder current = getConst();
                if (current.length() > 0) {
                    return buildNegativeConst(current);
                }
            }
            return getUnaryExpression(parseSimpleExpression(), token);
        }
    }

    private CommonExpression<T> parseBinaryExpression(int priority) throws ParsingException{
        CommonExpression<T> left = parseSimpleExpression();
        while (true) {
            String operation = parseToken(true);
            if (operation.isEmpty()) {
                return left;
            }
            if (!BINARY.contains(operation))
                throw new IllegalArgumentException("Illegal binary operation found", source.getPos());
            if (PRIORITIES.get(operation) >= priority) {
                source.backAt(operation.length() + (isEndOfInput() ? 0 : 1));
                nextChar();
                return left;
            } else {
                CommonExpression<T> right = parseBinaryExpression(PRIORITIES.get(operation));
                left = getBinaryExpression(left, right, operation);
            }
        }
    }

    public CommonExpression<T> parse(String expression) throws ParsingException {
        setSource(new StringSource(expression));
        CommonExpression<T> ans = parseBinaryExpression(MAX_PRIORITY);
        if (!isEndOfInput()) {
            throw new ExtraSymbolException("Unexpected closing bracket", source.getPos());
        }
        return ans;
    }

    private CommonExpression<T> getUnaryExpression(CommonExpression<T> argument, @NotNull String operation) throws ParsingException {
        if (operation.equals("-") && argument instanceof Const) {
            return buildNegativeConst(new StringBuilder(argument.toMiniString()));
        }
        try {
            return UNARY_OP.get(operation).get(argument, mode);
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal unary operation found", source.getPos());
        }
    }

    private CommonExpression<T> getBinaryExpression(CommonExpression<T> left, CommonExpression<T> right, String operation) throws IllegalArgumentException {
        try {
            return BINARY_OP.get(operation).get(left, right, mode);
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal unary operation found", source.getPos());
        }
    }

    @NotNull
    private CommonExpression<T> buildNegativeConst(@NotNull StringBuilder current) throws IllegalConstException {
        try {
            return new Const<>(mode.parseNumber(new StringBuilder().append("-").append(current.toString()).toString()));
        } catch (NumberFormatException e) {
            throw new IllegalConstException("Overflow constant", source.getPos());
        }
    }

    @NotNull
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