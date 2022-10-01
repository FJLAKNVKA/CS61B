import com.sun.jdi.PrimitiveValue;

import javax.net.ssl.SSLContext;
import javax.security.auth.callback.CallbackHandler;
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

    private void resize(int len)
    {
        int t = size;
        int Num = num;
        T a[] = (T[]) new Object[len];
        int cnt=0;
        while(Num!=0)
        {
            Num--;
            a[cnt++] = items[chage(++t)];
        }
        front = num;
        size = len - 1;
        items = a;
    }

    public void addLast(T x)
    {
        num++;
        items[size] = x;
        size = chage(++size);
        if(num+1==items.length) resize(items.length*2);
    }

    public void addFirst(T x)
    {
        num++;
        items[front] = x;
        front = chage(++front);
        if(num+1== items.length) resize(items.length*2);
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
        int t = front;
        int Num = num;
        while(Num!=0)
        {
            Num--;
            System.out.println(items[chage(--t)]);
        }
    }

    public T get(int index)
    {
       int t = front;
       t = chage(--t);
       while(index!=0)
       {
           t--;
       }
       return items[chage(t)];
    }

    public T removeLast()
    {
        if(!isEmpty())
        {
            num--;
            size = chage(++size);
            T t = items[size];
            if(num*4<=items.length)resize(items.length/2);
            return t;
        }
        return null;
    }

    public T removeFirst()
    {
        if(!isEmpty())
        {
            num--;
            front = chage(++front);
            T t = items[front];
            if(num*4<=items.length)resize(items.length/2);
            return t;
        }
        return null;
    }

//   public static void main(String[] args)
//   {
//       ArrayDeque e = new ArrayDeque();
//       for(int i=1;i<=9;i++)
//       {
//           e.addFirst(i);
//       }
//       System.out.println(e.get(0));
//       for(int i=1;i<=3;i++)e.removeLast();
//       for(int i=1;i<=8;i++)
//       {
//           e.addFirst(-i);
//       }
//       for(int i=1;i<=7;i++)e.removeLast();
//    }
}