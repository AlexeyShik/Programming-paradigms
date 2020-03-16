package queue;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class MyQueueTest {
    public static void main(String[] args) {
        Queue<Object> test = new ArrayDeque<>();
        ArrayQueue queue = new ArrayQueue();
        for (int t = 1; t <= 100; ++t) {
            Random rand = new Random(123456789 * t * t * t);
            for (int i = 0; i < 1e6; ++i) {
                Object current;
                if (i % 3 == 0) {
                    current = rand.nextInt();
                } else if (i % 3 == 1) {
                    current = rand.nextDouble();
                } else {
                    current = rand.nextLong();
                }
                test.add(current);
                queue.enqueue(current);
                assert test.peek() == queue.element();
                assert !queue.isEmpty();
                assert test.size() == queue.size();
            }
            for (int i = 0; i < 1e5; ++i) {
                Object p1 = test.poll();
                Object p2 = queue.dequeue();
                assert (p1 == null && p2 == null) || (p1 != null && p2 != null);
                assert p1 == null || p1.equals(p2);
            }
            queue.clear();
            test.clear();
            assert queue.isEmpty();
            System.err.println("Test " + t + ": Successfully");
        }
        System.err.println("Test run: 100, passed: 100, failed: 0");
    }
}
