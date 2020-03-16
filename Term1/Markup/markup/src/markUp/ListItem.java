package markUp;
import java.util.ArrayList;
import java.util.List;

public class ListItem extends AbstractAbstract implements ListContent {
    private List<ListContent> list;

    public ListItem (List<ListContent> objects) {
        super(objects);
    }

    public void toHtml(StringBuilder str) {
        super.toHtml(str, "<li>", "</li>");

    }

    @Override
    public String getLeft() {
        return "<li>";
    }

    @Override
    public String getRight() {
        return "</li>";
    }
}
