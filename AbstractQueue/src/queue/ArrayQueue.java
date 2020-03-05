package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue implements Queue {
    private Object[] values;
    private int indexToPop;

    public ArrayQueue() {
        //  Pre:
        clear();
        //  Post: создана пустая очередь
    }

    private void resizeArray() {
        //  Pre:
        Object[] tmp = new Object[2 * values.length];
        System.arraycopy(values, indexToPop, tmp, 0, values.length - indexToPop);
        System.arraycopy(values, 0, tmp, values.length - indexToPop, getIndexToPush());
        values = tmp;
        indexToPop = 0;
        //  Post: values.length > size, относительный порядок элементов не поменялся
    }

    @Override
    protected void doEnqueue(Object object) {
        if (size() == values.length) {
            resizeArray();
        }
        values[getIndexToPush()] = object;
    }

    @Override
    protected Object doElement() {
        return values[indexToPop];
    }

    @Override
    protected Object doDequeue() {
        Object value = values[indexToPop];
        values[indexToPop] = null;
        indexToPop = increment(indexToPop);
        return value;
    }

    private int increment(int t) {
        //  Pre:
        return (t + 1) % values.length;
        //  Post: вернет число (t + 1) (mod values.length)
    }

    private int getIndexToPush() {
        //  Pre:
        return (indexToPop + size()) % values.length;
        //  Post: возвращает индекс, указывающий на элемент массива values перед началом очереди.
    }

    protected ArrayQueue makeFilterAndPredicate(Predicate<Object> P, Function<Object, Object> F) {
        ArrayQueue queue = new ArrayQueue();
        for (int i = 0, j = indexToPop; i < size(); ++i, j = increment(j)) {
            checkPredicateAndApply(queue, values[j], P, F);
        }
        return queue;
    }

    @Override
    protected void doClear() {
        values = new Object[2];
        indexToPop = 0;
    }
}