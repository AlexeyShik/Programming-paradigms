package markUp;

public class Link implements ParagraphContent {
    Text link;
    public Link(String txt) {
        link = new Text(txt);
    }

    @Override
    public void toHtml(StringBuilder str) {
        StringBuilder marked = new StringBuilder();
        link.toHtml(marked);
        str.append("src='").append(marked);
    }

    @Override
    public void toMarkdown(StringBuilder str) {

    }

    @Override
    public void toHtml(StringBuilder str, String leftBound, String rightBound) {

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
