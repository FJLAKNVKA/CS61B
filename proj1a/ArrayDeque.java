import java.text.Format;

public class ArrayDeque<T> {

    private T[] items;
    private int size;
    private int front;
    public ArrayDeque()
    {
        items =(T[]) new Object [8];
        size = 7;
        front = 0;
    }

    public void addLast(T x)
    {
        if(front==size)
        {
            int len = items.length;
            T[] a = (T[]) new Object [len*2];
            System.arraycopy(items,0,a,0,front);
            System.arraycopy(items,front+1,a,size+len+1,len-size-1);
            items = a;
            size = len+size;
        }
        items[size] = x;
        size --;
    }

    public boolean isEmpty()
    {
        if(front==0&&(size+1==items.length))return true;
        return false;
    }

    public int size()
    {
        int r = items.length - size - 1;
        int len = front + r;
        return len;
    }

    public void printDeque()
    {
        for(int i=front-1;i>=0;i--)System.out.println(items[i]);
        for(int i=items.length-1;i>size;i--)System.out.println(items[i]);
    }

    public T get(int index)
    {
        if(index>this.size())return null;
        if(front>=index)return items[index-1];
        else
        {
            index-= front;
            return items[size-index];
        }
    }

    public void addFirst(T x)
    {
        if(front==size)
        {
            int len = items.length;
            T[] a = (T[]) new Object [len*2];
            System.arraycopy(items,0,a,0,front);
            System.arraycopy(items,front+1,a,size+len+1,len-size-1);
            items = a;
            size = len+size;
        }
        items[front] = x;
        front ++;
    }

    public T removeLast()
    {
        if(size==items.length-1)return null;
        T t = items[size+1];
        size++;
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
        return t;
    }

    public T removeFirst()
    {
        if(front==0)return null;
        T t = items[front-1];
        front--;
        if(items.length>8) {
            int r = items.length - size - 1;
            int len = front + r;
            if (items.length >= len * 4) {
                int l = (items.length) / 2;
                T[] a =(T[]) new Object [l];
                if(front!=0)System.arraycopy(items, 0, a, 0, front);
                if(r!=0)System.arraycopy(items, size + 1, a, l - r, r);
                size = l - r - 1;
                items = a;
            }
        }
        return t;
    }

//    public static void main(String[] args) {
//        ArrayDeque n = new ArrayDeque();
//        for(int i=1;i<=6;i++) {
//            n.addLast(i * 10);
//        }
//        for(int i=1;i<=2;i++)
//        {
//            n.addFirst(-i*10);
//        }
//        for(int i=1;i<=5;i++)
//        {
//            n.removeLast();
//        }
//    }
}