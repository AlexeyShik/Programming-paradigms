package markUp;
import java.util.List;

public class UnorderedList extends AbstractList{
    public UnorderedList(List<ListItem> objects) {
        super(objects);
    }

    @Override
    public void toHtml(StringBuilder str, String leftBound, String rightBound) {
        super.toHtml(str, "<ul>", "</ul>");
    }

    @Override
    public String getLeft() {
        return "<ul>";
    }

    @Override
    public String getRight() {
        return "</ul>";
    }

    public void toHtml(StringBuilder str) {
        super.toHtml(str);
    }
}
