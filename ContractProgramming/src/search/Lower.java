package search;

public class Lower implements BinarySearchComparator {
    @Override
    public boolean compare(int a, int b) {
        return a < b;
    }
}
