package markUp;
import java.util.List;

abstract class AbstractParagraph extends AbstractAbstract {
    private List<ParagraphContent> list;

    AbstractParagraph(List<ParagraphContent> object) {
        super(object);
        list = object;
    }

    public void toHtml(StringBuilder str, String leftBound, String rightBound) {
        super.toHtml(str, leftBound, rightBound);
    }

    public void toMarkDown(StringBuilder str, String leftBound, String rightBound) {
        str.append(leftBound);
        for (ParagraphContent curr : list)
            curr.toMarkdown(str);
        str.append(rightBound);
    }
}
