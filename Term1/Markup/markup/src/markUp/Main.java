package markUp;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UnorderedList ull = new UnorderedList(
          List.of(new ListItem(List.of(
                  new Strong(List.of(new Text("abacaba"))))
          ))
        );
        StringBuilder str = new StringBuilder();
        ull.toHtml(str);
        System.out.println(str.toString());
    }
}
