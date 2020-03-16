package markUp;
import java.util.List;

public class Image extends AbstractParagraph implements ParagraphContent {
    Link link;
    public Image(List<ParagraphContent> objects, String txt) {
        super(objects);
        link = new Link(txt);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
    }

    @Override
    public void toHtml(StringBuilder str) {
        StringBuilder marked = new StringBuilder();
        link.toHtml(marked);
        super.toHtml(str, "<img alt='", marked + "'>");
    }

    @Override
    public String getLeft() {
        return null;
    }

    @Override
    public String getRight() {
        return null;
    }
}
