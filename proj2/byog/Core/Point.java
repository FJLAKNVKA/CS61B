package byog.Core;

import java.io.Serializable;

public class Point implements Serializable {
    private static final long serialVersionUID = -797000591210082L;

    public int x=0;
    public int y=0;
    public int z=0;


    public void getnum(int a,int b,int c)
    {
        x = a;
        y = b;
        z = c;
    }
}
