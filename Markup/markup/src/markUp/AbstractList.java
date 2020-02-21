package markUp;
import java.util.List;

abstract class AbstractList extends AbstractAbstract implements ListContent {
    AbstractList (List<ListItem> objects) {
        super(objects);
    }

    @Override
    public void toHtml(StringBuilder str, String leftBound, String rightBound) {
        super.toHtml(str, leftBound, rightBound);
    }

    public void toHtml(StringBuilder str) {
        this.toHtml(str, "", "");
    }
}
