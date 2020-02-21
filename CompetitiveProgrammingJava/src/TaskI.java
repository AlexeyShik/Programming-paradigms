import java.util.Scanner;

public class TaskI {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int n = cin.nextInt();
        long xL = 1000000000, xR = -1000000000, yL = 1000000000, yR = -100000000;
        for (int i = 0; i < n; ++i) {
            long x = cin.nextLong(), y = cin.nextLong(), h = cin.nextLong();
            xL = Math.min(xL, x - h);
            xR = Math.max(xR, x + h);
            yL = Math.min(yL, y - h);
            yR = Math.max(yR, y + h);
        }
        long h = (Math.max(xR - xL, yR - yL) + 1) / 2;
        System.out.println((xL + xR) / 2 + " " + (yL + yR) / 2 + " " + h);
    }
}
