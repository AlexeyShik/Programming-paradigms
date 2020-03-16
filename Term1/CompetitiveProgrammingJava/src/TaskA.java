import java.util.Scanner;

public class TaskA {
    public static void main(String[] args) {
        long a, b, n;
        Scanner cin = new Scanner(System.in);
        a = cin.nextLong();
        b = cin.nextLong();
        n = cin.nextLong();
        System.out.println(2 * ((n - a - 1) / (b - a)) + 1);
    }
}
