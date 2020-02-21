package markUp;
import java.util.List;

public class Emphasis extends AbstractParagraph implements ParagraphContent{
    public Emphasis(List<ParagraphContent> objects) {
        super(objects);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        super.toMarkDown(str, "*", "*");
    }

    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "", "");
    }

    @Override
    public String getLeft() {
        return "<em>";
    }

    @Override
    public String getRight() {
        return "</em>";
    }
}
