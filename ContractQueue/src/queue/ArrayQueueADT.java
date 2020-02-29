package queue;

public class ArrayQueueADT {
    /*
    Инвариант очереди:
        size >= 0,
        элементы очереди следуют в порядке добавления
        с индексами от indexToPop до indexToPush
        в кольце вычетов по модулю values.length
        Все элементы очереди не равны null
    */
    private Object[] values;
    private int size;
    private int indexToPop;

    public ArrayQueueADT() {
        clear(this);
    }

    private static void resizeArray(ArrayQueueADT queue) {
        //  Pre:
        if (queue.size < queue.values.length) {
            return;
        }
        Object[] tmp = new Object[2 * queue.values.length];
        System.arraycopy(queue.values, queue.indexToPop, tmp, 0, queue.values.length - queue.indexToPop);
        System.arraycopy(queue.values, 0, tmp, queue.values.length - queue.indexToPop, getIndexToPush(queue));
        queue.values = tmp;
        queue.indexToPop = 0;
        //  Post: values.length > size, относительный порядок элементов не поменялся
    }

    private static void insertKthElement(ArrayQueueADT queue, Object object, int i) {
        //  Pre: object не null && values[i] == null
        resizeArray(queue);
        queue.values[i] = object;
        queue.size++;
        //  Post: object добавлен на i-й элемент массива, размер массива увеличен
    }

    public static void push(ArrayQueueADT queue, Object object) {
        //  Pre: object не null
        assert object != null;
        queue.indexToPop = decrement(queue, queue.indexToPop);
        insertKthElement(queue, object, queue.indexToPop);
        //  Post: values[indexToPop - 1] = добавленный объект
        //  Post: объект добавлен в начало очереди,
        //  Post: порядок остальных элементов очереди не изменился
    }

    public static void enqueue(ArrayQueueADT queue, Object object) {
        //  Pre: object не null
        assert object != null;
        insertKthElement(queue, object, getIndexToPush(queue));
        //  Post: values[indexToPush - 1] = добавленный объект
        //  Post: объект добавлен в конец очереди,
        //  Post: порядок остальных элементов очереди не изменился
    }

    public static Object element(ArrayQueueADT queue) {
        //  Pre: size > 0
        assert queue.size > 0;
        return queue.values[queue.indexToPop];
        //  Post: вернет первый элемент очереди.
        //  Post: очередь не изменилась
    }

    public static Object peek(ArrayQueueADT queue) {
        //  Pre: size > 0
        assert queue.size > 0;
        return queue.values[decrement(queue, getIndexToPush(queue))];
        //  Post: вернули первый эелемент очереди
    }

    private static void eraseKthElement(ArrayQueueADT queue, int i) {
        //  Pre:
        queue.values[i] = null;
        queue.size--;
        //  Post: Элемент массива удален без утечки памяти, размер уменьшен на 1
    }

    public static Object dequeue(ArrayQueueADT queue) {
        //  Pre: size > 0
        assert queue.size > 0;
        Object value = element(queue);
        queue.indexToPop = increment(queue, queue.indexToPop);
        eraseKthElement(queue, queue.indexToPop);
        return value;
        //  Post: первый элемент очереди был удален
        //  Post: порядок остальныех элементов очереди не изменился
    }

    public static Object remove(ArrayQueueADT queue) {
        //  Pre: size > 0
        assert queue.size > 0;
        Object value = peek(queue);
        eraseKthElement(queue, getIndexToPush(queue));
        return value;
        //  Post: последний элемент очереди был удален
        //  Post: порядок остальныех элементов очереди не изменился
    }

    public static int size(ArrayQueueADT queue) {
        //  Pre:
        return queue.size;
        //  Post: вернули количество элементов в очереди
    }

    public static boolean isEmpty(ArrayQueueADT queue) {
        //  Pre:
        return queue.size == 0;
        //  Post: true, если очередь пуста, false иначе
    }

    public static void clear(ArrayQueueADT queue) {
        //  Pre:
        queue.values = new Object[2];
        queue.size = 0;
        queue.indexToPop = 0;
        //  Post: Очередь не содержит ни одного элемента
        //  Post: Предыдущие элементы очереди будут собраны сборщиком мусора
        //  Post: (То есть нет утечки памяти)
    }

    private static int getIndexToPush(ArrayQueueADT queue) {
        //  Pre:
        return (queue.indexToPop + queue.size) % queue.values.length;
        //  Post: вернет позицию, в которую должен быть добавлен новый элемент в конец
    }

    private static int increment(ArrayQueueADT queue, int t) {
        //  Pre:
        return (t + 1) % queue.values.length;
        //  Post: вернет число (t + 1) (mod values.length)
    }

    private static int decrement(ArrayQueueADT queue, int t) {
        //  Pre:
        return (t - 1 + queue.values.length) % queue.values.length;
        //  Post: вернет число (t - 1) (mod values.length)
    }
}