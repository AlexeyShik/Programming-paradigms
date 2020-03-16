package markUp;
import java.util.List;

public class OrderedList extends AbstractList {
    public OrderedList(List<ListItem> objects) {
        super(objects);
    }

    @Override
    public void toHtml(StringBuilder str, String leftBound, String rightBound) {
        super.toHtml(str, "<ol>", "</ol>");
    }

    @Override
    public String getLeft() {
        return "<ol>";
    }

    @Override
    public String getRight() {
        return "</ol>";
    }

    public void toHtml(StringBuilder str) {
        super.toHtml(str);
    }
}
