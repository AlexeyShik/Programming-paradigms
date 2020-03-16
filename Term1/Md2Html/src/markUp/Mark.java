package markUp;
import java.util.List;

public class Mark extends AbstractParagraph implements ParagraphContent {
    public Mark(List<ParagraphContent> objects) {
        super(objects);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        super.toMarkDown(str, "~", "~");
    }

    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "<mark>", "</mark>");
    }

    @Override
    public String getLeft() {
        return "<mark>";
    }

    @Override
    public String getRight() {
        return "</mark>";
    }
}
