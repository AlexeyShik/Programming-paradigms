package expression;

public class Main {
    public static void main(String[] args) {
        Multiply sub = new Multiply(new Variable("x"), new Divide(new Variable("x"), new Const(2)));
        int ans = sub.evaluate(5);
        System.out.println(ans);
        System.out.println(sub.toMiniString());
    }
}
