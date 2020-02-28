package queue;

public class ArrayQueue {
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

    public ArrayQueue() {
        clear();
    }

    private void resizeArray() {
        //  Pre:
        if (size < values.length)
            return;
        Object[] tmp = new Object[2 * values.length];
        System.arraycopy(values, indexToPop, tmp, 0, values.length - indexToPop);
        System.arraycopy(values, 0, tmp, values.length - indexToPop, getIndexToPush());
        values = tmp;
        indexToPop = 0;
        //  Post: values.length > size, относительный порядок элементов не поменялся
    }

    private void insertKthElement(Object object, int i) {
        //  Pre: object не null && values[i] == null
        values[i] = object;
        size++;
        //  Post: object добавлен на i-й элемент массива, размер массива увеличен
    }

    public void push(Object object) {
        //  Pre: object не null
        assert object != null;
        resizeArray();
        indexToPop = decrement(indexToPop);
        insertKthElement(object, indexToPop);
        //  Post: values[indexToPop - 1] = добавленный объект
        //  Post: объект добавлен в начало очереди,
        //  Post: порядок остальных элементов очереди не изменился
    }

    public void enqueue(Object object) {
        //  Pre: object не null
        assert object != null;
        resizeArray();
        insertKthElement(object, getIndexToPush());
        //  Post: values[indexToPush - 1] = добавленный объект
        //  Post: объект добавлен в конец очереди,
        //  Post: порядок остальных элементов очереди не изменился
    }

    public Object element() {
        //  Pre: size > 0
        assert size > 0;
        return values[indexToPop];
        //  Post: вернет первый элемент очереди.
        //  Post: очередь не изменилась
    }

    public Object peek() {
        //  Pre: size > 0
        assert size > 0;
        return values[decrement(getIndexToPush())];
        //  Post: вернули первый эелемент очереди
    }

    private void eraseKthElement(int i) {
        //  Pre:
        values[i] = null;
        size--;
        //  Post: Элемент массива удален без утечки памяти, размер уменьшен на 1
    }

    public Object dequeue() {
        //  Pre: size > 0
        assert size > 0;
        Object value = element();
        eraseKthElement(indexToPop);
        indexToPop = increment(indexToPop);
        return value;
        //  Post: первый элемент очереди был удален
        //  Post: порядок остальныех элементов очереди не изменился
    }

    public Object remove() {
        //  Pre: size > 0
        assert size > 0;
        Object value = peek();
        eraseKthElement(decrement(getIndexToPush()));
        return value;
        //  Post: последний элемент очереди был удален
        //  Post: порядок остальныех элементов очереди не изменился
    }

    public int size() {
        //  Pre:
        return size;
        //  Post: вернули количество элементов в очереди
    }

    public boolean isEmpty() {
        //  Pre:
        return size == 0;
        //  Post: true, если очередь пуста, false иначе
    }

    public void clear() {
        //  Pre:
        values = new Object[2];
        size = 0;
        indexToPop = 0;
        //  Post: Очередь не содержит ни одного элемента
        //  Post: Предыдущие элементы очереди будут собраны сборщиком мусора
        //  Post: (То есть нет утечки памяти)
    }

    private int getIndexToPush() {
        //  Pre:
        return (indexToPop + size) % values.length;
        //  Post: вернет позицию, в которую должен быть добавлен новый элемент в конец
    }

    private int increment(int t) {
        //  Pre:
        return (t + 1) % values.length;
        //  Post: вернет число (t + 1) (mod values.length)
    }

    private int decrement(int t) {
        //  Pre:
        return (t - 1 + values.length) % values.length;
        //  Post: вернет число (t - 1) (mod values.length)
    }
}