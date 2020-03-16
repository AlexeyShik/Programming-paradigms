import java.util.Scanner;

public class TaskB {
    public static void main(String[] args) {
        int n;
        Scanner cin = new Scanner(System.in);
        n = cin.nextInt();
        final int magicConst = 710;
        for (int i = -magicConst * 25000; n > 0; --n, i += magicConst)
            System.out.println(i);
    }
}
