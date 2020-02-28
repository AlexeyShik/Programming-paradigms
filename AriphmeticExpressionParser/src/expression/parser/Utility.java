package expression.parser;

import expression.*;
import java.util.*;

class Utility {
    static final int MAX_PRIORITY = 4;
    static boolean needOperation = false;
    static final Map<String, Integer> PRIORITIES = Map.of(
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
    static final Map<String, Class<? extends Operation>> BINARY = Map.of(
            "+", CheckedAdd.class,
            "-", CheckedSubtract.class,
            "*", CheckedMultiply.class,
            "/", CheckedDivide.class,
            "//", CheckedLog.class,
            "**", CheckedPow.class,
            "<<", LeftShift.class,
            ">>", RightShift.class
    );
    static final Map<String, Class<? extends Operation>> UNARY = Map.of(
            "log2", CheckedLog2.class,
            "pow2", CheckedPow2.class,
            "-", CheckedNegate.class,
            "abs", Abs.class,
            "square", Square.class
    );
    static final Set<String> VARIABLES = Set.of(
            "x", "y", "z"
    );
    static final Set<Character> USABLE_SYMBOLS = new TreeSet<>();
    static {
        for (String s : BINARY.keySet()) {
            for (Character c : s.toCharArray()) {
                USABLE_SYMBOLS.add(c);
            }
        }
        for (String s : UNARY.keySet()) {
            for (Character c : s.toCharArray()) {
                USABLE_SYMBOLS.add(c);
            }
        }
        USABLE_SYMBOLS.add('x');
        USABLE_SYMBOLS.add('y');
        USABLE_SYMBOLS.add('z');
    }
    static Map<Character, String> FIRST_CHAR_TO_OPERATION = new HashMap<>();
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
}
