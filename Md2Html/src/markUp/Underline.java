package markUp;
import java.util.List;

public class Underline extends AbstractParagraph implements ParagraphContent {
    public Underline(List<ParagraphContent> objects) {
        super(objects);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        super.toMarkDown(str, "++", "++");
    }

    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "<u>", "</u>");
    }

    @Override
    public String getLeft() {
        return "<u>";
    }

    @Override
    public String getRight() {
        return "</u>";
    }
}
