package search;

import java.util.Arrays;

public class BinarySearch {
    static int iterativeSearch(int x, int[] a, BinarySearchComparator cmp) {
        //  Pre: a отсортирован по невозрастанию.
        if (a.length == 0 || cmp.compare(a[a.length - 1], x)) {
            return a.length;
        }
        // a.length > 0
        // а по невозрастанию && a[a.length - 1] <= x
        int left = -1, right = a.length - 1;
        // left <= i && i <= right && (left == -1 || !cmp(x, a[left])) && cmp(x, a[right]) - инвариант цикла
        while (right - left > 1) {
            int mid = left + (right - left) / 2;
            // Так как right >= 0 && left >= 0, то right - left <= right не переполнится =>
            // mid не переполнится, так как mid - среднее арифметическое, значит mid <= right
            if (cmp.compare(a[mid], x)) {
                // cmp(a[mid], x) && инвариант
                right = mid;
                // инвариант выполнен
            } else {
                // !cmp(a[mid], x) && инвариант
                left = mid;
                // инвариант выполнен
            }
            // цикл конечен, так как на каждой итерации right - left строго уменьшается
        }
        // left <= i && i <= right && (left == -1 || !cmp(x, a[left])) && cmp(x, a[right])
        // right - left <= 1
        if (cmp.compare(a[right], x)) {
            // Для i = right выполнено постусловие, поэтому right - ответ
            return right;
        }
        // Для i = right + 1 выполнено постусловие, поэтому right + 1 - ответ
        return right + 1;
        // Post: a не изменился, вернули
        // Post: i == a.length, если a.length == 0 || a[a.length - 1] > x,
        // Post: i == 0, если cmp(a[0], x),
        // Post: i : !cmp(a[i - 1], x) && cmp(a[i], x), иначе
    }

    private static int clearRecursiveSearch(int x, int[] a, int left, int right, BinarySearchComparator cmp) {
        // Pre: существует left < t <= right :    (]             инвариант функции
        // Pre: (t == 0 || !cmp(a[t - 1], x)) && cmp(a[t], x)    инвариант функции
        // Pre: a отсортирован по невозрастанию.
        if (left + 1 == right) {
            // right - left <= 1
            if (cmp.compare(a[right], x)) {
                // t = right подходит под постусловие
                return right;
            }
            // t = right + 1 подходит под постусловие
            return right + 1;
        }
        // right - left > 1
        int mid = left + (right - left) / 2;
        // Так как right >= 0 && left >= 0, то right - left <= right не переполнится =>
        // mid не переполнится, так как mid - среднее арифметическое, значит mid <= right
        // left <= mid < right
        if (cmp.compare(a[mid], x)) {
            // Для любого j > mid : cmp(a[j], a[mid]) => j не удовлетворяет постусловию
            // 0 <= left <= mid < a.length
            // Предусловие выполнено и инвариант сохранен
            return clearRecursiveSearch(x, a, left, mid, cmp);
        } else {
            // Для любого j < mid: !cmp(a[j], x) => j не удовлетворяет постусловию
            // 0 <= mid <= right < a.length
            // Преусловие выполнено и инвариант сохранен
            return clearRecursiveSearch(x, a, mid, right, cmp);
        }
        // рекурсия конечна, так как на каждой итерации right - left строго уменьшается
        // Post: существует left < t <= right :          инвариант функции
        // Post: (t == 0 || a[t - 1] > x) && a[t] <= x   инвариант функции
        // Post: a не изменился
        // Post: t : a[t - 1] > x >= a[t], иначе
    }

     static int recursiveSearch(int x, int[] a, int left, int right, BinarySearchComparator cmp) {
        // Pre: 0 <= left <= right < a.length || a.length = 0
        // Pre: a отсортирован по невозрастанию.

        if (a.length == 0 || !cmp.compare(a[right], x)) {
            return a.length;
        }
        // если вышли из функции, инвариант функции тоже сохранился
        // a.length > 0 && cmp(a[right], x)

        // a.length > 1 и в частности right - left > 1
        // либо, существует left < t <= right :
        // !cmp(a[t - 1], x) && cmp(a[t], x),
        // либо cmp(a[left], x)
        // предусловие выполнено
        return clearRecursiveSearch(x, a, left - 1, right, cmp);
        // Post: 0 <= left <= right < a.length
        // Post: a не изменился, вернули
        // Post: t == a.length, если a.length == 0 || !cmp(a[right], x),
        // Post: t == 0, если cmp(a[left], x),
        // Post: t : !cmp(a[t - 1], x) && cmp(a[t], x), иначе
    }

    public static void main(String[] args) {
        // Pre: x - число, а - массив чисел, отсортированный по невозрастанию

        /*int ans = recursiveSearch(Integer.parseInt(args[0]), Arrays.stream(
                Arrays.copyOfRange( args, 1, args.length)).mapToInt(
                        Integer :: parseInt).toArray(), 0, args.length - 2, new LowerEqual());
        */
        int ans = iterativeSearch(Integer.parseInt(args[0]), Arrays.stream(
                Arrays.copyOfRange( args, 1, args.length)).mapToInt(
                        Integer :: parseInt).toArray(), new LowerEqual());

        System.out.println(ans);
        // Post: i - первое вхождение x в массив а или insertion point, если его нет в массиве.
    }
}