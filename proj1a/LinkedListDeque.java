import com.sun.jdi.AbsentInformationException;

import javax.swing.*;
import java.util.AbstractMap;
import java.util.EventListener;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.IntConsumer;

public class LinkedListDeque<T> {
    public class IntNode
    {
        private T item;
        private IntNode next;
        private IntNode pre;

        private IntNode(IntNode pre1,T x,IntNode next1)
        {
            item = x;
            pre=pre1;
            next=next1;
        }
    }

    public IntNode first;
    private int size;
    private IntNode Sentinel;
    public LinkedListDeque()
    {
        Sentinel = new IntNode(null,(T) new Object(),null);
        Sentinel.pre = new IntNode(null,(T) new Object(),null);
        Sentinel.next = new IntNode(Sentinel.pre,(T) new Object(),null);
        Sentinel.pre.next=Sentinel.next;
        size=0;
    }

    //添加头结点
    public void addFirst(T x)
    {

        IntNode p = new IntNode(Sentinel.pre,x,Sentinel.pre.next);
        Sentinel.pre.next.pre = p;
        Sentinel.pre.next = p;
        size++;
    }

    public void addLast(T x)
    {
        IntNode p =new IntNode(Sentinel.next.pre,x,Sentinel.next);
        Sentinel.next.pre.next = p;
        Sentinel.next.pre = p;
        size++;
    }

    public int size()
    {
        return size;
    }

    public boolean isEmpty()
    {
        return size==0;
    }

    public void printDeque()
    {
        for(IntNode i = Sentinel.pre.next;i.next!=null;i = i.next)
        {
            System.out.println(i.item);
        }
    }

    public T get(int idx)
    {
        idx++;
        if(size<idx)return null;
        IntNode p = Sentinel.pre;
        while(idx!=0)
        {
            p=p.next;
            idx--;
        }
        return p.item;
    }

    public T removeLast()
    {
        if(size<=0)return null;
        T x= Sentinel.next.pre.item;
        Sentinel.next.pre = Sentinel.next.pre.pre;
        Sentinel.next.pre.next = Sentinel.next;
        size--;
        return x;
    }

    public T removeFirst()
    {
        if(size<=0)return null;
        T x = Sentinel.pre.next.item;
        Sentinel.pre.next = Sentinel.pre.next.next;
        Sentinel.pre.next.pre = Sentinel.pre;
        size--;
        return x;
    }

    public T getRecursive(int index)
    {
        index++;
        if(size< index)return null;
        else
        {
            return getRecursive(index-1,Sentinel.pre.next);
        }
    }

    private T getRecursive(int index,IntNode node)
    {
        if(index==0)return node.item;
        else
        {
            return getRecursive(index-1,node.next);
        }
    }

//    public static void main(String[] args)
//    {
//
//    }
}