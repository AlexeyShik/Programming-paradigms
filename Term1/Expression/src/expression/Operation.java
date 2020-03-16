package expression;

/**
 * @author Alexey Shik (salexey@salexey.info)
 */

public interface Operation {
    int evaluate(int x);
    double evaluate(double x);
    int evaluate(int x, int y, int z);
    String toString();
    String toMiniString();
    int getPriority();
}
