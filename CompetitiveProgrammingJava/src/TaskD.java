import java.util.Scanner;

public class TaskD {
    private static long mod = 998_244_353;
    private static long[] pow;
    private static long[] d;
    private static boolean[] used;

    public static long r(int n) {
        if (n % 2 == 0)
            return ((long)(n / 2) * pow[n / 2] + (long)(n / 2) * pow[n / 2 + 1]) % mod;
        return ((long)n * pow[(n + 1) / 2]) % mod;
    }

    public static long calc(int l) {
        if (used[l])
            return d[l];
        used[l] = true;
        d[l] = r(l) - ((long)l * d[1]) % mod;
        d[l] = (d[l] + mod) % mod;
        int i = 1;
        for (i = 2; i * i < l; ++i)
            if (l % i == 0) {
                d[l] -= (((long)(l / i) * calc(i)) % mod + ((long)i * calc(l / i)) % mod) % mod;
                d[l] = (d[l] + mod) % mod;
            }
        if (i * i == l)
            d[l] -= ((long)i * calc(i)) % mod;
        d[l] = (d[l] + mod) % mod;
        return d[l];
    }

    public static long sum(int n) {
        long ans = 0;
        int l = 1;
        for (l = 1; l * l < n; ++l)
            if (n % l == 0) {
                ans += d[l] + d[n / l];
                ans %= mod;
            }
        if (l * l == n)
            ans += d[l];
        return ans % mod;
    }

    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int n = cin.nextInt(), k = cin.nextInt();
        if (k == 1) {
            System.out.println(n);
            return;
        }
        pow = new long[n + 1];
        used = new boolean[n + 1];
        pow[0] = 1;
        for (int i = 1; i <= n; ++i)
            pow[i] = (pow[i - 1] * k) % mod;
        d = new long[n + 1];
        for (int i = 0; i <= n; ++i) {
            d[i] = 0;
            used[i] = false;
        }
        d[0] = 1;
        d[1] = k;
        used[0] = used[1] = true;
        for (int i = 2; i <= n; ++i)
            calc(i);
        long ans = 0;
        for (int i = 1; i <= n; ++i) {
            ans += sum(i);
            ans = (ans + mod) % mod;
        }
        System.out.println(ans);
    }
}
