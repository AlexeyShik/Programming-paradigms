package markUp;

public interface ParagraphInstance extends ListContent{
    void toHtml(StringBuilder str);
    void toMarkdown(StringBuilder str);
}
