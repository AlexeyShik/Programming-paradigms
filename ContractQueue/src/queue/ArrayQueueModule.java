package queue;

public class ArrayQueueModule {
    /*
    Инвариант очереди:
        size >= 0,
        элементы очереди следуют в порядке добавления
        с индексами от indexToPop до indexToPush
        в кольце вычетов по модулю values.length
        Все элементы очереди не равны null
    */
    private static Object[] values = new Object[2];
    private static int size = 0;
    private static int indexToPop = 0;

    private static void resizeArray() {
        //  Pre:
        if (size < values.length) {
            return;
        }
        Object[] tmp = new Object[2 * values.length];
        System.arraycopy(values, indexToPop, tmp, 0, values.length - indexToPop);
        System.arraycopy(values, 0, tmp, values.length - indexToPop, getIndexToPush());
        values = tmp;
        indexToPop = 0;
        //  Post: массив values увеличени в 2 раза, относительный порядок элементов очереди не изменился
    }

    private static void insertKthElement(Object object, int i) {
        //  Pre: object не null && values[i] == null
        values[i] = object;
        size++;
        //  Post: object добавлен на i-й элемент массива, размер массива увеличен
    }

    public static void push(Object object) {
        //  Pre: object не null
        assert object != null;
        resizeArray();
        indexToPop = decrement(indexToPop);
        insertKthElement(object, indexToPop);
        //  Post: values[indexToPop - 1] = добавленный объект
        //  Post: объект добавлен в начало очереди,
        //  Post: порядок остальных элементов очереди не изменился
    }

    public static void enqueue(Object object) {
        //  Pre: object не null
        assert object != null;
        resizeArray();
        insertKthElement(object, getIndexToPush());
        //  Post: values[indexToPush - 1] = добавленный объект
        //  Post: объект добавлен в конец очереди,
        //  Post: порядок остальных элементов очереди не изменился
    }

    public static Object element() {
        //  Pre: size > 0
        assert size > 0;
        return values[indexToPop];
        //  Post: вернет первый элемент очереди.
        //  Post: очередь не изменилась
    }

    public static Object peek() {
        //  Pre: size > 0
        assert size > 0;
        return values[decrement(getIndexToPush())];
        //  Post: вернули первый эелемент очереди
    }

    private static void eraseKthElement(int i) {
        //  Pre:
        values[i] = null;
        size--;
        //  Post: Элемент массива удален без утечки памяти, размер уменьшен на 1
    }

    public static Object dequeue() {
        //  Pre: size > 0
        assert size > 0;
        Object value = element();
        eraseKthElement(indexToPop);
        indexToPop = increment(indexToPop);
        return value;
        //  Post: первый элемент очереди был удален
        //  Post: порядок остальныех элементов очереди не изменился
    }

    public static Object remove() {
        //  Pre: size > 0
        assert size > 0;
        Object value = peek();
        eraseKthElement(getIndexToPush());
        return value;
        //  Post: последний элемент очереди был удален
        //  Post: порядок остальныех элементов очереди не изменился
    }

    public static int size() {
        //  Pre:
        return size;
        //  Post: вернули количество элементов в очереди
    }

    public static boolean isEmpty() {
        //  Pre:
        return size == 0;
        //  Post: true, если очередь пуста, false иначе
    }

    public static void clear() {
        //  Pre:
        values = new Object[2];
        size = 0;
        indexToPop = 0;
        //  Post: Очередь не содержит ни одного элемента
        //  Post: Предыдущие элементы очереди будут собраны сборщиком мусора
        //  Post: (То есть нет утечки памяти)
    }

    private static int getIndexToPush() {
        //  Pre:
        return (indexToPop + size) % values.length;
        //  Post: вернет позицию, в которую должен быть добавлен новый элемент в конец
    }

    private static int increment(int t) {
        //  Pre:
        return (t + 1) % values.length;
        //  Post: вернет число (t + 1) (mod values.length)
    }

    private static int decrement(int t) {
        //  Pre:
        return (t - 1 + values.length) % values.length;
        //  Post: вернет число (t - 1) (mod values.length)
    }
}