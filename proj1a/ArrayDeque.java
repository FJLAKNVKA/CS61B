import com.sun.jdi.PrimitiveValue;

import javax.net.ssl.SSLContext;
import java.lang.reflect.GenericArrayType;
import java.nio.file.StandardWatchEventKinds;
import java.text.Format;
import java.time.Period;
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

    private void resize(int len,int idx)
    {
        if(presize==-1)idx--;
        T[] a = (T[]) new Object[len];
        int cnt = 0;
        idx = chage(idx);
        while (len!=0)
        {
            len--;
            a[cnt++] = items[idx++];
            idx = chage(idx);
        }
        presize = 1;
        prefront = 1;
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
        if(size==items.length-1&& presize==-1)size--;
        if(size>q)presize = 1;
        if(num==items.length)
        {
            int t=size;
            front = num;
            resize(items.length*2,t+1);
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
        if(front==0&&prefront==-1)front++;
        if(q>front)prefront = 1;
        if(num== items.length)
        {
            int t = size;
            front = num;
            resize(items.length*2,t+1);
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
        int idx;
        if(prefront==-1)idx=front;
        else idx = front-1;
        int t= num;
        while(t!=0)
        {
            t--;
            idx--;
            idx = chage(idx);
            System.out.println(items[idx]);
        }
    }

    public T get(int index)
    {
       if(index > num)return null;
       T t =null;
       int idx;
       if(prefront!=-1)idx = front-1;
       else idx = front;
       while(index!=0)
       {
           index--;
           idx--;
       }
       idx = chage(idx);
       t = items[idx];
       return t;
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
                if (num * 4 <= len && len > 8)
                {
                    int l = len / 2;
                    T[] a = (T[]) new Object[l];
                    resize(l,size+1);
                    front = num;
                    size = items.length-1;
                }
                return t;
            }
            else
            {
                T t = items[size+1];
                size++;
                if (num * 4 <= items.length && items.length > 8)
                {
                    resize (items.length/2,size+1);
                    front = num;
                    size = items.length-1;
                }
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
                if (num * 4 <= len && len > 8)
                {
                    int l = len / 2;
                    T[] a = (T[]) new Object[l];
                    resize(l,size+1);
                    front = num;
                    size = items.length;
                }
                return t;
            }
            else
            {
                T t = items[front-1];
                front--;
                if (num * 4 <= items.length && items.length > 8)
                {
                    resize(items.length/2,size+1);
                    front = 0;
                    size = items.length-1;
                }
                return t;
            }
        }
        return null;
    }

//   public static void main(String[] args)
//   {
//       ArrayDeque e = new ArrayDeque();
//       e.addFirst(0);
//       e.removeLast();      //==> 0
//       e.addFirst(2);
//       e.addFirst(3);
//       e.addFirst(4);
//       e.addFirst(5);
//       e.removeLast();      //==> 2
//       e.addLast(7);
//       e.removeLast();      //==> 7
//       e.addFirst(9);
//       e.get(1);     // ==> 5
//       e.get(2);      //==> 4
//       e.addFirst(12);
//       e.removeLast();      //==> 3
//       e.removeLast();      //==> 4
//       e.addFirst(15);
//       System.out.println(e.get(3));
//    }
}