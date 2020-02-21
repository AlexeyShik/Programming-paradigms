package expression;

import expression.exceptions.DivideByZeroException;
import expression.exceptions.OverflowException;

public class Main {
    public static void main(String[] args) throws OverflowException, DivideByZeroException {
        Abs abs = new Abs(new CheckedNegate(new Const(5)));
        int ans = abs.evaluate(5);
        System.out.println(ans);
        System.out.println(abs.toMiniString());
    }
}
