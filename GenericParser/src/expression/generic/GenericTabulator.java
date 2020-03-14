package expression.generic;


import expression.common.CommonExpression;
import expression.exceptions.ExpressionException;
import expression.exceptions.IllegalArgumentException;
import expression.exceptions.ParsingException;
import expression.parser.ExpressionParser;
import expression.parser.Utility;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        if (!Utility.MODES.containsKey(mode)) {
            throw new IllegalArgumentException("Illegal mode found", 0);
        }
        return buildTabulate(Utility.MODES.get(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Object[][][] buildTabulate(ArithmeticOperator<T> operator, String expression,
                                                          int x1, int x2, int y1, int y2, int z1, int z2) throws ParsingException {
        ExpressionParser<T> parser = new ExpressionParser<>(operator);
        CommonExpression<T> result = parser.parse(expression);
        Object[][][] table = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = 0; i < x2 - x1 + 1; ++i) {
            for (int j = 0; j < y2 - y1 + 1; ++j) {
                for (int k = 0; k < z2 - z1 + 1; ++k) {
                    try {
                        table[i][j][k] = result.evaluate(operator.parseNumber(Integer.toString(x1 + i)),
                                operator.parseNumber(Integer.toString(y1 + j)), operator.parseNumber(Integer.toString(z1 + k)));
                    } catch (ExpressionException e) {
                        table[i][j][k] = null;
                    }
                }
            }
        }
        return table;
    }
}
