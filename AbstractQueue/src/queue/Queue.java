package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
    //  Pre: object не null
    void enqueue(Object object);
    //  Post: объект добавлен в конец очереди,
    //  Post: порядок остальных элементов очереди не изменился

    //  Pre: size > 0
    Object element();
    //  Post: вернет первый элемент очереди.
    //  Post: очередь не изменилась

    //  Pre: size > 0
    Object dequeue();
    //  Post: первый элемент очереди был удален
    //  Post: порядок остальныех элементов очереди не изменился

    //  Pre:
    Queue filter(Predicate<Object> P);
    //  Post: Вернет очередь, содержащую все элементы данной, которые удовлетворяют Predicate P

    //  Pre:
    Queue map(Function<Object, Object> F);
    //  Post: Вернет очередь, содержащую все элементы данной, к каждому элементу которой была применена Function F

    //  Pre:
    int size();
    //  Post: вернули количество элементов в очереди

    //  Pre:
    boolean isEmpty();
    //  Post: true, если очередь пуста, false иначе

    //  Pre:
    void clear();
    //  Post: Очередь не содержит ни одного элемента
    //  Post: Предыдущие элементы очереди будут собраны сборщиком мусора
    //  Post: (То есть нет утечки памяти)
}
