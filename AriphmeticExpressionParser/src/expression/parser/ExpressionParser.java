package expression.parser;

import expression.*;
import expression.exceptions.*;
import expression.exceptions.IllegalArgumentException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ExpressionParser extends BaseParser implements Parser {
    private static final int MAX_PRIORITY = 4;
    private static boolean needOperation = false;
    private static final Map<String, Integer> PRIORITIES = Map.of(
            "+", 2,
            "-", 2,
            "*", 1,
            "/", 1,
            "<<", 3,
            ">>", 3,
            "**", 0,
            "//", 0,
            ")", MAX_PRIORITY
    );
    private static final Map<String, Class<? extends Operation>> BINARY = Map.of(
            "+", CheckedAdd.class,
            "-", CheckedSubtract.class,
            "*", CheckedMultiply.class,
            "/", CheckedDivide.class,
            "//", CheckedLog.class,
            "**", CheckedPow.class,
            "<<", LeftShift.class,
            ">>", RightShift.class
    );
    private static final Map<String, Class<? extends Operation>> UNARY = Map.of(
            "log2", CheckedLog2.class,
            "pow2", CheckedPow2.class,
            "-", CheckedNegate.class,
            "abs", Abs.class,
            "square", Square.class
    );
    private static Map<Character, String> FIRST_CHAR_TO_OPERATION = new HashMap<>();
    static {
        FIRST_CHAR_TO_OPERATION.put('l', "log2");
        FIRST_CHAR_TO_OPERATION.put('p', "pow2");
        FIRST_CHAR_TO_OPERATION.put('s', "square");
        FIRST_CHAR_TO_OPERATION.put('a', "abs");
        FIRST_CHAR_TO_OPERATION.put('>', ">>");
        FIRST_CHAR_TO_OPERATION.put('<', "<<");
        FIRST_CHAR_TO_OPERATION.put(')', ")");
        FIRST_CHAR_TO_OPERATION.put('(', "(");
        FIRST_CHAR_TO_OPERATION.put('/', "/");
        FIRST_CHAR_TO_OPERATION.put('*', "*");
        FIRST_CHAR_TO_OPERATION.put('-', "-");
        FIRST_CHAR_TO_OPERATION.put('+', "+");
    }
    private final Set<String> VARIABLES = Set.of(
            "x", "y", "z"
    );

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
                str.append(ch);
                nextChar();
                skipWhitespace();
            }
            return str.toString();
        }
    }

    private TripleExpression parseSimpleExpression() throws ExpressionException, ParsingException {
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
            if (!Character.isWhitespace(ch) && ch != '-' && ch != '(' && !token.equals("-")) {
                throw new IllegalArgumentException("Unexpected variable", source.getPos() - token.length() + 1);
            }
            return getUnaryExpression((Operation) parseSimpleExpression(), token);
        }
    }

    private TripleExpression parseBinaryExpression(int priority) throws ExpressionException, ParsingException{
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

    public TripleExpression parse(String expression) throws Exception {
        setSource(new StringSource(expression));
        TripleExpression ans = parseBinaryExpression(MAX_PRIORITY);
        if (!isEndOfInput()) {
            throw new ExtraSymbolException("Unexpected closing bracket", source.getPos());
        }
        return ans;
    }

    private TripleExpression getUnaryExpression(Operation argument, String operation) throws ExpressionException, ParsingException {
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
