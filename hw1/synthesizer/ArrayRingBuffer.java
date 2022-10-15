// TODO: Make sure to make this class a part of the synthesizer package
package synthesizer;
import javax.sql.rowset.FilteredRowSet;
import java.util.Iterator;
//TODO: Make sure to make this class and all of its methods public
//TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T>  extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    private int chage(int x)
    {
        return x%capacity;
    }

    public ArrayRingBuffer(int capacity) {
        rb =(T[]) new Object[capacity];
        this.capacity = capacity;
        first = last = fillCount = 0;
    }

    public void enqueue(T x) {
        if(isFull()) throw new RuntimeException("Ring buffer overflow");
        rb[last++] = x;
        last = chage(last);
        fillCount++;
    }




    public T dequeue() {
        if(isEmpty())throw new RuntimeException("Ring buffer overflow");
        T res = rb[first];
        first = chage(++first);
        fillCount--;
        return res;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {

        if(isEmpty())throw new RuntimeException("Ring buffer overflow");
        return rb[first];
    }

    public Iterator<T> iterator() {
        return new BufferIterator();
    }

    private class BufferIterator implements Iterator<T> {
        private int pos;
        private int num;

        BufferIterator() {
            pos = first;
            num = 0;
        }

        @Override
        public boolean hasNext() {
            return num < fillCount;
        }

        @Override
        public T next() {
            T returnItem = rb[pos];
            pos++;
            if (pos == capacity) {
                pos = 0;
            }
            num++;
            return returnItem;
        }
    }
}
