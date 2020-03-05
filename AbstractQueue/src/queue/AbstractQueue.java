package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
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
        return makeFilterAndPredicate(P, Function.identity());
    }

    @Override
    public Queue map(Function<Object, Object> F) {
        return makeFilterAndPredicate(object -> true, F);
    }

    void checkPredicateAndApply(Queue queue, Object object, Predicate<Object> P, Function<Object, Object> F) {
        //  Pre:
        if (P.test(object)) {
            queue.enqueue(F.apply(object));
        }
        //  Post: Если Predicate P выполнен, то в очередь добавлен результат применения Function F к объекту object
    }

    protected abstract Queue makeFilterAndPredicate(Predicate<Object> P, Function<Object, Object> F);

    //  Pre:
    protected abstract void doClear();
    //  Post: очередь пуста, утечек памяти нет
}
