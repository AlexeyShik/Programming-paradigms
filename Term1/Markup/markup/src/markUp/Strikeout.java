package markUp;
import java.util.List;

public class Strikeout extends AbstractParagraph implements ParagraphContent {
    public Strikeout(List<ParagraphContent> objects) {
        super(objects);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        super.toMarkDown(str, "~", "~");
    }

    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "", "");
    }

    @Override
    public String getLeft() {
        return "<s>";
    }

    @Override
    public String getRight() {
        return "</s>";
    }
}
