package expression.parser;

import expression.common.CommonExpression;
import expression.expressions.*;
import expression.generic.*;
import java.util.*;

@FunctionalInterface
interface OpBinary{
    <T> CommonExpression<T> get(CommonExpression<T> left, CommonExpression<T> right, ArithmeticOperator<T> op);
}

@FunctionalInterface
interface OpUnary {
    <T> CommonExpression<T> get(CommonExpression<T> argument, ArithmeticOperator<T> op);
}

public class Utility {
    static final int MAX_PRIORITY = 4;
    static final Map<String, Integer> PRIORITIES = Map.of(
            "+", 2,
            "-", 2,
            "*", 1,
            "/", 1,
            "max", 3,
            "min", 3,
            ")", MAX_PRIORITY
    );
    static final Set<String> BINARY = Set.of(
            "+", "-", "*", "/", "max", "min", "(", ")"
    );
    static final Set<String> UNARY = Set.of(
            "-", "count"
    );
    static final Set<String> VARIABLES = Set.of(
            "x", "y", "z"
    );
    public static final Map<String, ArithmeticOperator<? extends Number>> MODES = Map.of(
            "i", new IntegerOperator(true),
            "d", new DoubleOperator(false),
            "bi", new BigIntegerOperator(false),
            "s", new ShortOperator(false),
            "l", new LongOperator(false),
            "u", new IntegerOperator(false)
    );
    static final Map<String, OpBinary> BINARY_OP = Map.of(
            "+", CheckedAdd::new,
            "-", CheckedSubtract::new,
            "*", CheckedMultiply::new,
            "/", CheckedDivide::new,
            "min", Min::new,
            "max", Max::new
    );
    static final Map<String, OpUnary> UNARY_OP = Map.of(
            "-", CheckedNegate::new,
            "count", Count::new
    );
}
