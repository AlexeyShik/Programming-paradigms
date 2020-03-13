package queue;

import java.util.Iterator;

public class ArrayQueue extends AbstractQueue {
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

    @Override
    protected Queue getInstance() {
        return new ArrayQueue();
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

    @Override
    protected void doClear() {
        values = new Object[2];
        indexToPop = 0;
    }

    @Override
    public Iterator<Object> iterator() {
        return new Iterator<>() {
            int cur = indexToPop, passed = 0;

            @Override
            public boolean hasNext() {
                return passed < size();
            }

            @Override
            public Object next() {
                assert hasNext();
                Object value = values[cur];
                cur = increment(cur);
                passed++;
                return value;
            }
        };
    }
}