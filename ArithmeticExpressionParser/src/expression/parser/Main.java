package expression.parser;

import expression.TripleExpression;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        ExpressionParser expressionParser = new ExpressionParser();
        TripleExpression expression = expressionParser.parse(scanner.nextLine());
        int t = expression.evaluate(0, 0, 0);
        System.out.println(t);
    }
}