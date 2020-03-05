package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue implements Queue {
    //  Инвариант структуры:
    //  Элементы хранятся в односвязном списке в порядке добавления
    //  от tail до head, причем:
    //  Либо head = tail = null <=> очередь пуста
    //  Либо tail != null && head != null <=> очередь не пуста

    private static class Node {
        //  Элемент очереди.
        private Node next;
        private Object value;

        private Node(Object object, Node next) {
            value = object;
            this.next = next;
        }

    }
    private Node head;
    private Node tail;

    public LinkedQueue() {
        //  Pre:
        clear();
        //  Post: создана пустая очередь
    }

    @Override
    protected void doEnqueue(Object object) {
        Node tmp = new Node(object, null);
        if (tail == null) {
            tail = tmp;
        } else {
            head.next = tmp;
        }
        head = tmp;
    }

    @Override
    protected Object doElement() {
        return tail.value;
    }

    @Override
    protected Object doDequeue() {
        Node tmp = tail;
        Object ans = tail.value;
        tail = tmp.next;
        return ans;
    }

    protected LinkedQueue makeFilterAndPredicate(Predicate<Object> P, Function<Object, Object> F) {
        LinkedQueue queue = new LinkedQueue();
        for (Node cur = tail; cur != null; cur = cur.next) {
            checkPredicateAndApply(queue, cur.value, P, F);
        }
        return queue;
    }

    @Override
    protected void doClear() {
        head = tail = null;
    }
}
