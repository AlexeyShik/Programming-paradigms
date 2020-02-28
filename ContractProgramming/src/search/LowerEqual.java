package search;

public class LowerEqual implements BinarySearchComparator {
    @Override
    public boolean compare(int a, int b) {
        return a <= b;
    }
}
