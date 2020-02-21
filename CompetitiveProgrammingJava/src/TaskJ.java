import java.io.IOException;
import java.util.Scanner;

public class TaskJ {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int n = cin.nextInt();
        char[][] str = new char[n][n];
        char[][] gr = new char[n][n];
        String strangeThings = cin.nextLine();
        for (int i = 0; i < n; ++i) {
            String s = cin.nextLine();
            for (int j = 0; j < n; ++j) {
                str[i][j] = s.charAt(j);
                gr[i][j] = '0';
            }
        }
        for (int i = 0; i < n; ++i)
            for (int j = i + 1; j < n; ++j) {
                int cur = str[i][j] - '0';
                for (int k = i + 1; k < j; ++k) {
                    cur -= (int)(gr[i][k] - '0') * (int)(str[k][j] - '0');
                    cur %= 10;
                    if (cur < 0)
                        cur += 10;
                }
                if (cur != 0)
                    gr[i][j] = '1';
            }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j)
                System.out.print(gr[i][j]);
            System.out.println();
        }
    }
}
