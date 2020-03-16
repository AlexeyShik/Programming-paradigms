package markUp;

public class Text implements ParagraphContent {
    String value;

    public Text(String str) {
        value = str;
    }

    @Override
    public void toHtml(StringBuilder str, String leftBound, String rightBound) {
        str.append(value);
    }

    @Override
    public String getLeft() {
        return "";
    }

    @Override
    public String getRight() {
        return "";
    }

    @Override
    public void toHtml(StringBuilder str) {
        str.append(value);
    }

    @Override
    public void toMarkdown(StringBuilder str) {
        str.append(value);
    }
}
