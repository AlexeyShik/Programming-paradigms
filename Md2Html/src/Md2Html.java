import markUp.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Md2Html {
    private static int level = 0;
    private static boolean IsSegmentHeader = false;
    private static boolean[] used;
    private static int rb = 0;
    private static final Set<Character> MARKABLE_ELEMENTS = Set.of('*', '`', '-', '_', '[', '+', '~', '!');
    private static final Set<Character> SPECIAL_HTML_ELEMENTS = Set.of('<', '>', '&');
    private static final Set<String> CORRECT_TOKENS = Set.of("*", "**", "_", "__", "--", "++", "`", "[", "]", "~");

    private static boolean checkHeader(String s) {
        if (s.isEmpty() || s.charAt(0) != '#')
            return false;
        level = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) != '#')
                break;
            level++;
        }
        return (s.length() != level && s.charAt(level) == ' ');
    }

    private static boolean IsMarkableSymbol(char c) {
        return MARKABLE_ELEMENTS.contains(c);
    }

    private static boolean IsCorrectTag(String s) {
        return CORRECT_TOKENS.contains(s);
    }

    private static boolean IsSpecialHtmlSymbol(char c) {
        return SPECIAL_HTML_ELEMENTS.contains(c);
    }

    private static String Translate(char c) {
        if (c == '<')
            return "&lt;";
        if (c == '>')
            return "&gt;";
        return "&amp;";
    }

    private static int findPairSymbol(String s, int l, int r, String leftSymbol) {
        while (l <= r) {
            if (leftSymbol.charAt(0) == s.charAt(l) && !used[l] &&
                    ((leftSymbol.length() == 1 && (l + 1 > r || s.charAt(l + 1) != s.charAt(l)))
                            || (leftSymbol.length() == 2 && leftSymbol.charAt(1) == s.charAt(l + 1)) && !used[l + 1])) {
                if (s.charAt(l - 1) == '\\') {
                    ++l;
                    continue;
                }
                used[l] = true;
                if (leftSymbol.length() == 2)
                    used[l + 1] = true;
                return l;
            }
            if (l + 1 <= r && s.charAt(l) == s.charAt(l + 1))
                ++l;
            ++l;
        }
        return -1;
    }

    private static void getConstSequence(String s, StringBuilder str, int l, int r) {
        for (int i = l; i <= r; ++i)
            str.append(s.charAt(i));
    }

    private static void parse(String s, StringBuilder str, int l, int r, int ind) {
        if (IsSegmentHeader && ind == 0) {
            while (s.charAt(l) == '#') {
                ++l;
            }
            if (s.charAt(l) == ' ')
                ++l;
        }
        while (l <= r) {
            if (s.charAt(l) == '\\' &&  l + 1 <= r && IsMarkableSymbol(s.charAt(l + 1))) {
                ++l;
                str.append(s.charAt(l));
                ++l;
                continue;
            }
            if (IsMarkableSymbol(s.charAt(l))) {
                if (used[l]) {
                    ++l;
                    continue;
                }
                String leftSymbol;
                if (l + 1 <= r && IsMarkableSymbol(s.charAt(l + 1)) && s.charAt(l) == s.charAt(l + 1)) {
                    leftSymbol = s.charAt(l) + String.valueOf(s.charAt(l + 1));
                } else {
                    leftSymbol = String.valueOf(s.charAt(l));
                }
                if (leftSymbol.equals("!")) {
                    leftSymbol = "]";
                    ++l;
                }
                if (!IsCorrectTag(leftSymbol)) {
                    str.append(leftSymbol);
                    l += leftSymbol.length();
                    continue;
                }
                int leftBound = l + (leftSymbol.length());
                int rightBound = findPairSymbol(s, leftBound, r, leftSymbol);
                if (rightBound == -1) {
                    str.append(leftSymbol);
                    l += leftSymbol.length();
                    continue;
                }
                StringBuilder sequenceToHtml = new StringBuilder();
                StringBuilder constReference = new StringBuilder();
                if (leftSymbol.equals("]")) {
                    int lReference = rightBound + 1;
                    int rReference = findPairSymbol(s, lReference, r, ")");
                    getConstSequence(s, constReference, lReference + 1, rReference - 1);
                    rb = rReference + 1;
                    getConstSequence(s, sequenceToHtml, leftBound, rightBound - 1);
                    sequenceToHtml.append("' ");
                } else {
                    parse(s, sequenceToHtml, leftBound, rightBound - 1, ind);
                }
                ParagraphContent content;
                switch(leftSymbol) {
                    case "**":
                    case "__": {
                        content = new Strong(List.of(new Text(sequenceToHtml.toString())));
                        break;
                    }
                    case "*":
                    case "_": {
                        content = new Emphasis(List.of(new Text(sequenceToHtml.toString())));
                        break;
                    }
                    case "--": {
                        content = new Strikeout(List.of(new Text(sequenceToHtml.toString())));
                        break;
                    }
                    case "`": {
                        content = new Code(List.of(new Text(sequenceToHtml.toString())));
                        break;
                    }
                    case "]": {
                        content = new Image(List.of(new Text(sequenceToHtml.toString())), constReference.toString());
                        break;
                    }
                    case "++": {
                        content = new Underline(List.of(new Text(sequenceToHtml.toString())));
                        break;
                    }
                    case "~": {
                        content = new Mark(List.of(new Text(sequenceToHtml.toString())));
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + leftSymbol);
                }
                sequenceToHtml = new StringBuilder();
                content.toHtml(sequenceToHtml);
                str.append(sequenceToHtml);
                if (constReference.length() == 0) {
                    l = rightBound + leftSymbol.length();
                } else {
                    l = rb;
                }
            } else {
                if (IsSpecialHtmlSymbol(s.charAt(l))) {
                    str.append(Translate(s.charAt(l)));
                } else {
                    str.append(s.charAt(l));
                }
                ++l;
            }
        }
    }

    public static void main(String[] args) {
        try (BufferedReader cin = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8))) {
            try (BufferedWriter cout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
                ArrayList<String> strings = new ArrayList<>();
                while (cin.ready()) {
                    String curr = cin.readLine();
                    if ((curr.isEmpty() && !strings.isEmpty()) || !cin.ready()) {
                        if (!cin.ready()) {
                            if (!curr.isEmpty())
                                strings.add(curr);
                            if (strings.size() == 1) {
                                IsSegmentHeader = checkHeader(curr);
                            }
                        }

                        StringBuilder bigString = new StringBuilder();
                        for (String string : strings)
                            bigString.append(string).append("/n");

                        StringBuilder str = new StringBuilder();
                        if (IsSegmentHeader)
                            str.append("<h").append(level).append(">");
                        else
                            str.append("<p>");

                        used = new boolean[bigString.length()];
                        for (int j = 0; j < bigString.length(); ++j)
                            used[j] = false;

                        parse(bigString.toString(), str, 0, bigString.length() - 1, 0);

                        if (IsSegmentHeader)
                            str.append("</h").append(level).append(">");
                        else
                            str.append("</p>");

                        String result = str.toString();
                        StringBuilder toWriteDown = new StringBuilder();
                        for (int i = 0; i < result.length(); ++i) {
                            if (result.charAt(i) == '/' && result.charAt(i + 1) == 'n') {
                                cout.write(toWriteDown.toString());
                                i += 2;

                                if (!(result.charAt(i) == '<' && result.charAt(i + 1) == '/')) {
                                    --i;
                                    cout.newLine();
                                    toWriteDown = new StringBuilder();
                                    continue;
                                }

                                toWriteDown = new StringBuilder();
                                while (result.charAt(i) != '>') {
                                    toWriteDown.append(result.charAt(i));
                                    ++i;
                                }

                                toWriteDown.append(result.charAt(i));
                                cout.write(toWriteDown.toString());
                                toWriteDown = new StringBuilder();
                                cout.newLine();
                            } else {
                                toWriteDown.append(result.charAt(i));
                            }
                        }

                        if (toWriteDown.length() != 0) {
                            cout.write(toWriteDown.toString());
                            cout.newLine();
                        }
                        strings.clear();
                    } else if (!curr.isEmpty()) {
                        strings.add(curr);
                        if (strings.size() == 1) {
                            IsSegmentHeader = checkHeader(curr);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}