import com.sun.jdi.PrimitiveValue;

import javax.net.ssl.SSLContext;
import java.nio.file.StandardWatchEventKinds;
import java.text.Format;
import java.util.function.DoubleToLongFunction;

public class ArrayDeque<T> {

    private T[] items;
    private int size;
    private int front;

    private int presize = 1;
    private int prefront = 1;

    private int num;

    public ArrayDeque()
    {
        items =(T[]) new Object [8];
        size = 7;
        front = 0;
        num = 0;
    }

    private int chage(int x)
    {
        x = (x% items.length + items.length )% items.length;
        return x;
    }

    private void doublearray()
    {
        int len = items.length;
        T[] a = (T[]) new Object[len*2];
        System.arraycopy(items,0,a,0,len);
        items =a;
    }

    public void addLast(T x)
    {
        num++;
        int q =size;
        if(presize==-1)
        {
            size --;
            size = chage(size);
            items[size] = x;
        }
        else
        {
            items[size] = x;
            size --;
            size = chage(size);
        }
        if(size==items.length-1)size--;
        if(size>q)presize = 1;
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
        int q = front;
        if(prefront==-1)
        {
            front++;
            front = chage(front);
            items[front] = x;
        }
        else
        {
            items[front] = x;
            front++;
            front = chage(front);
        }
        if(front==0)front++;
        if(q>front)prefront = 1;
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
        int idx = front;
        while(idx!=size)
        {
            idx--;
            idx = chage(idx);
            System.out.println(items[idx]);
        }
    }

    public T get(int index)
    {
       if(index > num)return null;
       T t =null;
       int idx = front;
       while(index!=0)
       {
           index--;
           idx--;
           idx = chage(idx);
       }
       if(items[idx]==null)return items[chage(--idx)];
       t = items[idx];
       return t;
    }


    private void checkcut()
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
            if(presize<=0||size==items.length-1)
            {
                size++;
                int len = items.length;
                int q =size;
                size = chage(size);
                if(q>size)presize=-1;
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
            if(prefront<=0||front==0)
            {
                int len = items.length;
                front--;
                int q = front;
                front = chage(front);
                if(q<front)prefront = -1;
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

//   public static void main(String[] args)
//   {
//       ArrayDeque e = new ArrayDeque();
//       e.addLast(0);
//       System.out.println(e.get(0));
//    }
}