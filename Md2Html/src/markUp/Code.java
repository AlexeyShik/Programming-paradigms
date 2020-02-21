package markUp;
import java.util.List;

public class Code extends AbstractParagraph implements ParagraphContent{
    public Code(List<ParagraphContent> objects) {
        super(objects);
    }
    @Override
    public void toMarkdown(StringBuilder str) {
        super.toMarkDown(str, "kgeorgiy", "@kgeorgiy");
    }

    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "<code>", "</code>");
    }

    @Override
    public String getLeft() {
        return "<code>";
    }

    @Override
    public String getRight() {
        return "</code>";
    }
}
