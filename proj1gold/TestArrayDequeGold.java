import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequeGold {
    @Test
    public void test()
    {
        StudentArrayDeque stu = new StudentArrayDeque();
        ArrayDequeSolution sol = new ArrayDequeSolution();

        int num = 0;
        String str = "\n";
        for(int i=1;i<=500;i++)
        {
//            if(i%5==0)
//            {
//                str += "size()\n";
//                assertEquals(str,stu.size(),sol.size());
//            }
            double op = StdRandom.uniform();
            if(op<=0.25)
            {
                stu.addFirst(i);
                sol.addFirst(i);
                num++;
                str += "addFirst(" + i + ")\n";
                assertEquals(str,stu.get(0),sol.get(0));
            }

            else if(op<=0.5)
            {
                stu.addLast(i);
                sol.addLast(i);
                num++;
                str += "addLast(" + i + ")\n";
                assertEquals(str,stu.get(num-1),sol.get(num-1));
            }

            else if(op<=0.75)
            {
                if(num>0)
                {
                    str += "removeFirst()\n";
                    num--;
                    assertEquals(str,stu.removeFirst(),sol.removeFirst());
                }
                else
                {
                    str +="isEmpty()\n";
                    assertEquals(str,sol.isEmpty(),stu.isEmpty());
                }
            }

            else
            {
                if(num>0)
                {
                    str += "removeLast()\n";
                    num--;
                    assertEquals(str,stu.removeLast(),sol.removeLast());
                }
                else
                {
                    str +="isEmpty()\n";
                    assertEquals(str,sol.isEmpty(),stu.isEmpty());
                }
            }
        }
    }
}
