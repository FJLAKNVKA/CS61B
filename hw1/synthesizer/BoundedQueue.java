package synthesizer;
public interface BoundedQueue<T>
{
    int capacity();
    int fillCount();
    void enqueue(T x);
    T dequeue();
    T peek();
    default boolean isEmpty() {
        if(fillCount()==0)return true;
        return false;
    }
    default boolean isFull() {
        if(fillCount()==capacity())return true;
        return false;
    }
}
