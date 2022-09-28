import javax.net.ssl.SSLContext;
import java.nio.file.StandardWatchEventKinds;
import java.text.Format;
import java.util.function.DoubleToLongFunction;

public class ArrayDeque<T> {

    private T[] items;
    private int size;
    private int front;

    private int num;

    public ArrayDeque()
    {
        items =(T[]) new Object [8];
        size = 7;
        front = 0;
        num = 0;
    }

    public int chage(int x)
    {
        x = (x% items.length + items.length )% items.length;
        return x;
    }

    public void doublearray()
    {
        int len = items.length;
        T[] a = (T[]) new Object[len*2];
        System.arraycopy(items,0,a,0,len);
        items =a;
    }

    public void addLast(T x)
    {
        num++;
        items[size] = x;
        size --;
        size = chage(size);
        if(num==items.length)
        {
            front = items.length;
            doublearray();
            size = items.length-1;
        }
    }

    public void addFirst(T x)
    {
        num++;
        items[front] = x;
        front++;
        front = chage(front);
        if(num== items.length)
        {
            front = items.length;
            doublearray();
            size = items.length-1;
        }
    }

    public boolean isEmpty()
    {
        if(num==0)return true;
        return false;
    }

    public int size()
    {
        return num;
    }

    public void printDeque()
    {
        while(front!=size)
        {
            front--;
            front = chage(front);
            System.out.println(items[front]);
        }
    }

    public T get(int index)
    {
       index++;
       if(index>num)return null;
       T t =null;
       while(index!=0)
       {
           index--;
           front--;
           front = chage(front);
           t = items[front];
       }
       return t;
    }


    public void checkcut()
    {
        if(items.length>8)
        {
            int r = items.length - size - 1;
            int len = front + r;
            if (items.length >= len * 4)
            {
                int l = (items.length) / 2;
                T[] a =(T[]) new Object [l];
                if (front != 0) System.arraycopy(items, 0, a, 0, front);
                if (r != 0) System.arraycopy(items, size + 1, a, l - r, r);
                size = l - r - 1;
                items = a;
            }
        }
    }

    public T removeLast()
    {
        if(!isEmpty())
        {
            num--;
            if(size==items.length-1||size<front)
            {
                size++;
                int len = items.length;
                size = chage(size);
                if(size == 0)size++;
                T t = items[size-1];
                if ((front - size) * 4 <= len && len > 8)
                {
                    int l = len / 2;
                    T[] a = (T[]) new Object[l];
                    if(front!=size)System.arraycopy(items, size, a, 0, front - size);
                    size = l - 1;
                    front = front-size;
                    items = a;
                }
                return t;
            }
            else
            {
                T t = items[size+1];
                size++;
                checkcut();
                return t;
            }
        }
        return null;
    }

    public T removeFirst()
    {
        if(!isEmpty())
        {
            num--;
            if(front==0||front>size)
            {
                int len = items.length;
                front--;
                front = chage(front);
                if(front ==len-1)front--;
                T t = items[front+1];
                if ((front - size) * 4 <= len && len > 8)
                {
                    int l = len / 2;
                    T[] a = (T[]) new Object[l];
                    if(front!=size)System.arraycopy(items, size + 1, a, l-(size-front), size-front);
                    size = l-(size-front)-1;
                    front = 0;
                    items = a;
                }
                return t;
            }
            else
            {
                T t = items[front-1];
                front--;
                checkcut();
                return t;
            }
        }
        return null;
    }

//    public static void main(String[] args)
//    {
//        ArrayDeque e = new ArrayDeque<Integer>();
//        e.isEmpty();
//        e.addFirst(1);
//        e.isEmpty();
//        e.addFirst(3);
//        e.addFirst(4);
//        e.addFirst(5);
//        e.addFirst(6);
//        e.addFirst(7);
//
//        e.removeLast();
//
//        e.addFirst(-4);
//        e.addFirst(-5);
//        e.addFirst(-6);
//        e.addFirst(-7);
//
//        e.printDeque();
//
//    }
}