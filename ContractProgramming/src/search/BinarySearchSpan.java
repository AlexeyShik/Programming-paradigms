package search;

import java.util.Arrays;

public class BinarySearchSpan {
    private static class Pair {
        int i;
        int length;
        Pair(int pos, int len) {
            i = pos;
            length = len;
        }
    }

    private static Pair iterativeSearch(int x, int[] a) {
        // Pre: a отсортирован по невозрастанию.
        // Предусловие выполнено
        int i = BinarySearch.iterativeSearch(x, a, new LowerEqual());
        // i == a.length, если a.length == 0 || a[a.length - 1] > x,
        // i == 0, если a[0] <= x,
        // i : a[i - 1] > x >= a[i], иначе
        if (i == a.length) {
            return new Pair(a.length, 0);
        }
        // a.length > 0 && i < a.length
        // Предусловие выполнено
        int j = BinarySearch.iterativeSearch(x, a, new Lower());
        // j == a.length, если a[a.length - 1] > x - 1,
        // j == 0, если a[0] <= x - 1,
        // j : a[j - 1] > x - 1 >= a[j], иначе
        // Покажем, что t + 1 = j.
        // Если j = a.length, то на суффиксе массива находятся иксы,
        // значит (j - 1)-й элемент массива тоже х
        // Иначе j : a[j] <= x - 1 < x.
        // Если a[i] = x, то j : a[j - 1] = x, a[j] <= x => j - 1 - последнее вхождение х
        // Если (a[i] < x <=> х нет в массиве а), то j = i, так как a[t] <= x
        // <=> a[t] <= (x - 1), при отсутствии х в массиве а.
        return new Pair(i, j - i);
        // Post: i == a.length, если a.length == 0 || a[a.length - 1] > x,
        // Post: i == 0, если a[0] <= x,
        // Post: i : a[i - 1] > x >= a[i], иначе
        // Post: length = 0, если x не встречается в массиве а,
        // Post: length = (t + 1) - i, где i - первое вхождение х, t
        // Post: - последнее вхождение х в массив а.
    }

    private static Pair recursiveSearch(int x, int[] a, int left, int right) {
        // Pre: a отсортирован по невозрастанию.
        // Pre: 0 <= left <= right < a.length || a.length = 0
        if (a.length == 0) {
            return new Pair(0, 0);
        }
        // a.length > 0
        // Предусловие выполнено
        int i = BinarySearch.recursiveSearch(x, a, left, right, new LowerEqual());
        // i == a.length, если a.length == 0 || a[right] > x,
        // i == 0, если a[0] <= x,
        // i : a[i - 1] > x >= a[i], иначе
        if (i == a.length) {
            return new Pair(a.length, 0);
        }
        // a.length > 0 && i < a.length
        int j = BinarySearch.recursiveSearch(x, a, left, right, new Lower());
        // j == a.length, если a.length == 0 || a[right] > x - 1,
        // j == 0, если a[0] <= x - 1,
        // j : a[j - 1] > x - 1 >= a[i], иначе
        // Покажем, что t + 1 = j.
        // Если j = a.length, то на суффиксе массива находятся иксы,
        // значит (j - 1)-й элемент массива тоже х
        // Иначе j : a[j] <= x - 1 < x.
        // Если a[i] = x, то j : a[j - 1] = x, a[j] <= x => j - 1 - последнее вхождение х
        // Если (a[i] < x <=> х нет в массиве а), то j = i, так как a[t] <= x
        // <=> a[t] <= (x - 1), при отсутствии х в массиве а.
        return new Pair(i, j - i);
        // Post: i == a.length, если a.length == 0 || a[a.length - 1] > x,
        // Post: i == 0, если a[0] <= x,
        // Post: i : a[i - 1] > x >= a[i], иначе
        // Post: length = 0, если x не встречается в массиве а,
        // Post: length = (t + 1) - i, где i - первое вхождение х, t
        // Post: - последнее вхождение х в массив а.
    }

    public static void main(String[] args) {
        // Pre: args[0] - число, аrgs[1..args.length-1] - массив чисел, отсортированный по невозрастанию

        Pair ans = recursiveSearch(Integer.parseInt(args[0]), Arrays.stream(
                Arrays.copyOfRange(args, 1, args.length)).mapToInt(
                Integer::parseInt).toArray(), 0, args.length - 2);


        /*Pair ans = iterativeSearch(Integer.parseInt(args[0]), Arrays.stream(
                Arrays.copyOfRange( args, 1, args.length)).mapToInt(
                Integer :: parseInt).toArray());
*/

        System.out.println(ans.i + " " + ans.length);

        // Post: i - первое вхождение x в массив а или insertion point, если его нет в массиве.
        // Post: length - количество элементов х в массиве
    }
}

