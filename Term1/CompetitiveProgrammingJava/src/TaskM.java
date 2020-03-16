import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TaskM {
    public static void main(String[] args) {
        int t;
        Scanner cin = new Scanner(System.in);
        t = cin.nextInt();
        while (t-- > 0) {
            int n = cin.nextInt();
            int a[] = new int[n];
            for (int i = 0; i < n; ++i)
                a[i] = cin.nextInt();
            Map<Integer, Integer> cnt = new HashMap<>();
            int ans = 0;
            for (int j = n - 1; j >= 0; --j) {
                for (int i = 0; i < j; ++i)
                    if (cnt.containsKey(2 * a[j] - a[i]))
                        ans += cnt.get(2 * a[j] - a[i]);
                if (cnt.containsKey(a[j])) {
                    cnt.put(a[j], cnt.get(a[j]) + 1);
                } else {
                    cnt.put(a[j], 1);
                }
            }
            System.out.println(ans);
        }
    }
}
