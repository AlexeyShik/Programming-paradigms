package expression.common;

import expression.exceptions.ParsingException;
import expression.generic.IntegerOperator;
import expression.parser.ExpressionParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws ParsingException {
        ExpressionParser<Integer> parser = new ExpressionParser<>(new IntegerOperator(true));
        Scanner cin = new Scanner(System.in);
        System.out.println(parser.parse(cin.nextLine()).toMiniString());
    }
}
