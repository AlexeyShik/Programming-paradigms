import java.io.*;
import java.util.*;

public class TaskH {
    public static void main(String[] args) throws IOException {
        Scanner cin = new Scanner(System.in);
        int n = cin.nextInt();
        int[] v = new int[n];
        int[] pref = new int[n];
        int max = (int) -1e9;
        for (int i = 0; i < n; ++i) {
            v[i] = cin.nextInt();
            max = Math.max(max, v[i]);
            if (i == 0)
                pref[0] = v[0];
            else
                pref[i] = pref[i - 1] + v[i];
        }
        int m = cin.nextInt();
        Map<Integer, Integer> f = new HashMap<>();
        for (int i = 0; i < m; ++i) {
            int t = cin.nextInt();
            if (t < max) {
                System.out.println("Impossible");
                continue;
            }
            if (f.containsKey(t)) {
                System.out.println(f.get(t));
                continue;
            }
            int j = 0, ans = 0;
            while (j < n) {
                int l = j, r = n;
                while (r - l > 1) {
                    int mid = (l + r) / 2;
                    int cur = pref[mid];
                    if (j > 0)
                        cur -= pref[j - 1];
                    if (cur <= t) {
                        l = mid;
                    } else {
                        r = mid;
                    }
                }
                j = r;
                ans++;
            }
            f.put(t, ans);
            System.out.println(ans);
        }
    }
}