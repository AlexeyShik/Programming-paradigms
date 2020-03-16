package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue, Iterable<Object> {
    private int size;

    @Override
    public void enqueue(Object object) {
        assert object != null;
        doEnqueue(object);
        size++;
    }

    //  Pre: object != null
    protected abstract void doEnqueue(Object object);
    //  Post: object добавлен в очередь

    @Override
    public Object element() {
        assert size > 0;
        return doElement();
    }

    //  Pre: size > 0
    protected abstract Object doElement();
    //  Post: вернет первый элемент очереди

    @Override
    public Object dequeue() {
        assert size > 0;
        Object result = doDequeue();
        size--;
        return result;
    }

    //  Pre: size > 0
    protected abstract Object doDequeue();
    //  Post: удалили первый элемент очереди и вернули его

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        doClear();
        size = 0;
    }

    @Override
    public Queue filter(Predicate<Object> P) {
        assert P != null;
        return makeFilterAndPredicate(P, Function.identity());
    }

    @Override
    public Queue map(Function<Object, Object> F) {
        assert F != null;
        return makeFilterAndPredicate(object -> true, F);
    }

    protected abstract Queue getInstance();

    //  Pre: P != null, F != null, результат применения F не null
    private Queue makeFilterAndPredicate(Predicate<Object> P, Function<Object, Object> F) {
        Queue queue = getInstance();
        for (Object o : this) {
            if (P.test(o)) {
                Object arg = F.apply(o);
                assert arg != null;
                queue.enqueue(arg);
            }
        }
        return queue;
    }
    //  Post: Вернет очередь, все элементы которой удовлетворяют Predicate P,
    //  после применения к ним Function F

    //  Pre:
    protected abstract void doClear();
    //  Post: очередь пуста, утечек памяти нет
}
