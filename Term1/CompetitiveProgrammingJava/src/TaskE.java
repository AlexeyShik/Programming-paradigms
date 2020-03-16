import java.util.*;

public class TaskE {
    private static int[] d;
    private static int[] v;
    private static int[] par;
    private static boolean[] used;
    private static ArrayList[] g;
    private static void bfs(int u, int p) {
        par[u] = p;
        LinkedList<Integer> q = new LinkedList<>();
        LinkedList<Integer> id = new LinkedList<>();
        q.addLast(u);
        id.addLast(0);
        while (!q.isEmpty()) {
            int cur = q.pollFirst();
            int curD = id.pollFirst();
            used[cur] = true;
            for (int i = 0; i < g[cur].size(); ++i)
                if (!used[(int) g[cur].get(i)]) {
                    par[(int) g[cur].get(i)] = cur;
                    q.addLast((Integer) g[cur].get(i));
                    d[(int) g[cur].get(i)] = curD + 1;
                    id.addLast(curD + 1);
                }
        }
    }

    public static void main(String[] args) {
        Random random = new Random(124);
        Scanner cin = new Scanner(System.in);
        int n = cin.nextInt(), m = cin.nextInt();
        g = new ArrayList[n];
        used = new boolean[n];
        for (int i = 0; i < n; ++i) {
            g[i] = new ArrayList();
            used[i] = false;
        }
        for (int i = 1; i < n; ++i) {
            int u = cin.nextInt(), v = cin.nextInt();
            u--;
            v--;
            g[u].add(v);
            g[v].add(u);
        }
        v = new int[m];
        for (int i = 0; i < m; ++i) {
            v[i] = cin.nextInt();
            v[i]--;
        }
        d = new int[n];
        for (int i = 0; i < n; ++i)
            d[i] = 0;
        par = new int[n];
        bfs(v[0], -1);
        int max = -1, maxId= -1;
        for (int i = 0; i < m; ++i)
            if (max < d[v[i]]) {
                max = d[v[i]];
                maxId = v[i];
            }
        if (max % 2 != 0) {
            System.out.println("NO");
            return;
        }
        for (int i = 0; i < n; ++i) {
            d[i] = 0;
            used[i] = false;
        }
        for (int len = 0; len * 2 < max; ++len) {
            maxId = par[maxId];
        }
        bfs(maxId, -1);
        boolean ans = true;
        int e = d[v[0]];
        for (int i = 0; i < m; ++i) {
            if (d[v[i]] != e)
                ans = false;
        }
        if (ans) {
            System.out.println("YES\n" + (maxId + 1));
        } else {
            System.out.println("NO");
        }
    }
}