import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TaskK {
    private static char[][] v;

    public static void fill(int li, int ri, int lj, int rj) {
        for (int i = li + 1; i < ri; i++) {
            for (int j = lj; j < rj; j++) {
                if (v[i][j] == '.') {
                    v[i][j] = Character.toLowerCase(v[i - 1][j]);
                }
            }
        }
        for (int i = ri - 2; i >= li; i--) {
            for (int j = lj; j < rj; j++) {
                if (v[i][j] == '.') {
                    v[i][j] = Character.toLowerCase(v[i + 1][j]);
                }
            }
        }
        for (int i = li; i < ri; i++) {
            for (int j = lj + 1; j < rj; j++) {
                if (v[i][j] == '.') {
                    v[i][j] = Character.toLowerCase(v[i][j - 1]);
                }
            }
        }
        for (int i = li; i < ri; i++) {
            for (int j = rj - 2; j >= lj; j--) {
                if (v[i][j] == '.') {
                    v[i][j] = Character.toLowerCase(v[i][j + 1]);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintWriter cout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        int n = 0, m = 0;
        String wtfJava = cin.readLine(); // пояснение этого чуда: числа 6 и 8 из теста у меня считывались как совершенно другие, пофиксить это мне удалось лишь так, до сих пор не понимаю, как бытро и красиво осуществлять ввод на джаве.
        int hh;
        for (hh = 0; hh < wtfJava.length(); ++hh) {
            if (wtfJava.charAt(hh) == ' ')
                break;
            n = n * 10 + (wtfJava.charAt(hh) - '0');
        }
        ++hh;
        for (; hh < wtfJava.length(); ++hh) {
            if (wtfJava.charAt(hh) == ' ')
                break;
            m = m * 10 + (wtfJava.charAt(hh) - '0');
        }
        v = new char[n][m];
        ArrayList <Integer> is = new ArrayList<>();
        ArrayList <Integer> js = new ArrayList<>();
        int Ai = 0, Aj = 0;

        for (int i = 0; i < n; ++i) {
            char[] c = new char[m];
            final String curr = cin.readLine();
            for (int j = 0; j < m; ++j) {
                v[i][j] = curr.charAt(j);
                if (v[i][j] == 'A') {
                    Ai = i;
                    Aj = j;
                } else if (Character.isLetter(v[i][j])) {
                    is.add(i);
                    js.add(j);
                }
            }
        }
        int li = Ai, lj = Aj, ri = Ai, rj = Aj, maxProduct = 0;
        for (int i = 0; i <= Ai; ++i) {
            for (int j = Ai; j < n; ++j) {
                int lb = 0, rb = m - 1;

                for (int k = 0; k < is.size(); ++k) {
                    if (i <= is.get(k) && is.get(k) <= j) {
                        if (js.get(k) < Aj) {
                            lb = Math.max(lb, js.get(k) + 1);
                        } else {
                            rb = Math.min(rb, js.get(k) - 1);
                        }
                    }
                }

                if (lb > Aj || rb < Aj)
                    continue;

                if (rb - lb + 1 > 0 && (rb - lb + 1) * (j - i + 1) > maxProduct) {
                    maxProduct = (rb - lb + 1) * (j - i + 1);
                    li = i;
                    ri = j;
                    lj = lb;
                    rj = rb;
                }
            }
        }

        for (int i = li; i <= ri; ++i) {
            for (int j = lj; j <= rj; ++j) {
                if (v[i][j] != 'A') {
                    v[i][j] = 'a';
                }
            }
        }

        fill(0, li, 0, m);
        fill(ri + 1, n, 0, m);
        fill(li, ri + 1, 0, lj);
        fill(li, ri + 1, rj, m);

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j)
                cout.print(v[i][j]);
            cout.println();
        }
        cin.close();
        cout.close();
    }
}