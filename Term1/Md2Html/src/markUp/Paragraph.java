package markUp;
import java.util.List;

public class Paragraph extends AbstractParagraph implements ParagraphInstance{
    public Paragraph(List <ParagraphContent> objects) {
        super(objects);
    }

    public void toMarkdown(StringBuilder str) {
        super.toMarkDown(str, "", "");
    }

    public void toHtml(StringBuilder str) {
        super.toHtml(str, "", "");
    }

    @Override
    public String getLeft() {
        return "";
    }

    @Override
    public String getRight() {
        return "";
    }
}
