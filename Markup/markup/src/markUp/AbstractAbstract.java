package markUp;

import java.util.List;

public abstract class AbstractAbstract {
    private List<? extends ListContent> list;

    AbstractAbstract(List<? extends ListContent> objects) {
        list = objects;
    }

    public void toHtml(StringBuilder str, String leftBound, String rightBound) {
        str.append(leftBound);
        for (ListContent curr : list)
            curr.toHtml(str, curr.getLeft(), curr.getRight());
        str.append(rightBound);
    }
}
