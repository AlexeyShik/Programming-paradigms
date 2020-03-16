package markUp;

public interface ListContent {
    void toHtml(StringBuilder str, String leftBound, String rightBound);
    String getLeft();
    String getRight();
}
