package markUp;
import java.util.List;

public class Strong extends AbstractParagraph implements ParagraphContent {
    public Strong(List <ParagraphContent> objects) {
        super(objects);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        super.toMarkDown(str, "__", "__");
    }

    @Override
    public void toHtml(StringBuilder str) {
        super.toHtml(str, "<strong>", "</strong>");
    }

    @Override
    public String getLeft() {
        return "<strong>";
    }

    @Override
    public String getRight() {
        return "</strong>";
    }
}
