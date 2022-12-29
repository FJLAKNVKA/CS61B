package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.algs4.ResizingArrayBag;
import org.junit.Test;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.nio.file.Watchable;
import java.util.Arrays;
import java.util.Random;

public class Test1 {
    private static final int WIDTH = 81;
    private static final int HEIGHT = 31;

    static int size = 0;

    public int dx[] = new int[]{0,1,0,-1};
    public int dy[] = new int[]{1,0,-1,0};

    private int count = 0;

    private int up,left,down,right;

    public static TETile[][] world;
    public Point[] grid;

    public Point wall[][] = new Point[4][WIDTH+HEIGHT];  //按照上左下右的排列

    public static boolean st[][];

    public static int num = 0;

    public boolean rst[][];

    public boolean maze_load[][] = new boolean[WIDTH+1][HEIGHT+1];

    public Test1()
    {
        rst = new boolean[WIDTH+3][HEIGHT+3];

        grid = new Point[WIDTH*HEIGHT+10];
        st = new boolean[WIDTH+3][HEIGHT+3];

        world = new TETile[WIDTH][HEIGHT];

        for(int i=0;i<WIDTH;i++)
            for(int j=0;j<HEIGHT;j++)
            {
                if(j%2==0)world[i][j] = Tileset.WALL;
                else
                {
                    if(i%2==0)world[i][j] = Tileset.WALL;
                    else world[i][j] = Tileset.FLOOR;
                }
            }

    }

    public boolean check(int starx,int stary,int len,int wid) //检查房子是否重叠和是否越界
    {
        if (starx + len >= WIDTH || stary + wid >= HEIGHT) return false;
        for(int i=starx;i<=len+starx;i++)
            for(int j=stary;j<=wid+stary;j++)
                if(world[i][j]==Tileset.GRASS||rst[i][j])return false;

        //下面检测房子和房子之间是否挨得太近，分别从上下左右四个方向

        //上
        if (stary + 2 +wid< HEIGHT)
        {
            for(int i=starx;i<=starx+len;i++)
                if(world[i][stary + 2 + wid]==Tileset.GRASS||rst[i][stary + 2 + wid])return false;
        }
        //左
        if(starx-2>=0)
        {
            for(int i=stary;i<=stary+wid;i++)
                if(world[starx-2][i]==Tileset.GRASS||rst[starx-2][i])return false;
        }
        //下
        if(stary-2>=0)
        {
            for(int i=starx;i<=starx+len;i++)
                if(world[i][stary-2]==Tileset.GRASS||rst[i][stary-2])return false;
        }
        //右
        if(starx+2+len<WIDTH)
        {
            for(int i=stary;i<=stary+wid;i++)
                if(world[starx+2+len][i]==Tileset.GRASS||rst[starx+2+len][i])return false;
        }
        return true;
    }
    public boolean check(int a,int b)
    {
        if(a<=0||a>=WIDTH-1||b<=0||b>=HEIGHT-1)return false;
        if(world[a][b]==Tileset.WALL)return false;
        for(int i=0;i<4;i++)
        {
            int x = a + dx[i],y = b + dy[i];
            if(x<0||x>=WIDTH||y<0||y>=HEIGHT)continue;
            if(world[x][y]!=Tileset.WALL)return false;  //这个说明对面那个floor已经身处在一个连通块中，不能打通这面墙了
        }
        return true;
    }

    public void bulid(int starx,int stary,int len,int wid)
    {
        //rst用来标记墙的
        if(check(starx,stary,len,wid))
        {
            count++;
            for(int i=starx;i<=len+starx;i++)
            {
                rst[i][stary] = true;
                rst[i][stary+wid] = true;
            }
            for(int i=stary;i<=wid+stary;i++)
            {
                rst[starx][i] = true;
                rst[starx+len][i] = true;
            }
            for(int i=starx+1;i<starx+len;i++)
                for(int j=stary+1;j<stary+wid;j++)
                    world[i][j] = Tileset.GRASS;
        }

    }

    public void add_list(int x,int y,int idx)    //传入的是一个floor周围的墙
    {
        if(num>0) remove(idx);
        else return;
        if(check(x+1,y)||check(x-1,y))
        {
            world[x+1][y] = Tileset.TREE;
            world[x-1][y] = Tileset.TREE;
            world[x][y] = Tileset.TREE;
            maze_load[x][y] = true;
            for(int i=0;i<4;i++)
            {
                int x1 = x+1+dx[i],y1 = y+dy[i];
                int x2 = x-1+dx[i],y2 = y+dy[i];
                if(bodercheck(x1,y1)&&!st[x1][y1])add(x1,y1);
                if(bodercheck(x2,y2)&&!st[x2][y2])add(x2,y2);
            }
        }

        else if(check(x,y+1)||check(x,y-1))
        {
            world[x][y] = Tileset.TREE;
            world[x][y+1] = Tileset.TREE;
            world[x][y-1] = Tileset.TREE;
            maze_load[x][y] = true;
            for(int i=0;i<4;i++)
            {
                int x1 = x+dx[i],y1 = y+dy[i]+1;
                int x2 = x+dx[i],y2 = y+dy[i]-1;
                if(x1>=0&&x1<WIDTH&&y1>=0&&y1<HEIGHT&&!st[x1][y1])add(x1,y1);
                if(x2>=0&&x2<WIDTH&&y2>=0&&y2<HEIGHT&&!st[x2][y2])add(x2,y2);
            }
        }
    }

    //将点当前的点加入到队伍中去，用于扩张
    public void add(int x,int y)
    {
        num++;
        if(num>size)
        {
            size = num;
            grid[size] = new Point();
        }
        st[x][y]=true;
        grid[num].x=x;
        grid[num].y=y;
    }

    public void remove(int idx)  //用掉的点要从队伍中去除掉：和最后一个点做交换，然后让队伍长度减一即可
    {
        if(idx==-1)return;
        int x = grid[num].x;
        int y = grid[num].y;
        num--;
        grid[idx].x = x;
        grid[idx].y = y;
    }

    //遍历当前的房间，并把房间里面连通块置为water，并将房间的围墙通过find_wall找到，加入到随机序列队伍中去。
    public void dfs_grass(int x,int y)
    {
        for(int i=0;i<4;i++)
        {
            int a = dx[i]+x,b = dy[i]+y;
            if(bodercheck(a,b)&&world[a][b]==Tileset.GRASS&&!st[a][b])
            {
                world[a][b]=Tileset.WATER;
                find_wall(a,b);
                st[a][b]=true;
                dfs_grass(a,b);
            }
        }
    }

    //通过遍历房间的草来找到周围的墙，并加入相应的数组中储存
    public void find_wall(int x,int y)  //传入贴着墙的房间块
    {
        for(int i=0;i<4;i++)
        {
            int a = x+dx[i],b = y+dy[i];
            if(bodercheck(a,b)&&world[a][b]==Tileset.WALL)
            {
                //判断这个房间块和墙的关系
                if(dy[i]==1)
                {
                    wall[0][up] = new Point();
                    wall[0][up].x = a;
                    wall[0][up++].y = b;
                }
                else if(dx[i]==-1)
                {
                    wall[1][left] = new Point();
                    wall[1][left].x = a;
                    wall[1][left++].y = b;
                }
                else if(dy[i]==-1)
                {
                    wall[2][down] = new Point();
                    wall[2][down].x = a;
                    wall[2][down++].y = b;
                }

                {
                    wall[3][right] = new Point();
                    wall[3][right].x = a;
                    wall[3][right++].y = b;
                }
            }
        }
    }

    public void change()
    {
        for(int i=0;i<WIDTH;i++)
            for(int j=0;j<HEIGHT;j++)
                if(world[i][j]==Tileset.WATER)
                    world[i][j]=Tileset.FLOOR;
    }

    public boolean bodercheck(int x,int y)
    {
        return (x>=0&&x<WIDTH&&y>=0&&y<HEIGHT);
    }

    public boolean checkground(int a,int b)
    {
        for(int i=0;i<4;i++)
        {
            int x = a+dx[i],y = b+dy[i];
            if(bodercheck(a,b)&&st[x][y]==true&&world[x][y]!=Tileset.WATER)
            {
                return true;
            }
        }
        return false;
    }

    public boolean search(int x,int y,Random random)
    {
        int dir = random.nextInt(4);
        int a = x,b = y;
        if(dir==0)y++;
        else if(dir==1)x--;
        else if(dir==2)y--;
        else x++;
        if(!bodercheck(x,y))return false;
        if(st[x][y]&&world[x][y]!=Tileset.WATER)
        {
            world[a][b] = Tileset.FLOOR;
            return true;
        }

        else if(world[x][y]==Tileset.WALL&&!rst[x][y])  //如果为墙的话，凿墙走
        {
            boolean t = search(x,y,random);
            if(t)
            {
                world[a][b] = Tileset.FLOOR;
                return true;
            }
        }

        return false;
    }

    public void connect(Random random,Test1 t)
    {
        for(int i=0;i<WIDTH;i++)
            for(int j=0;j<HEIGHT;j++)
            {
                if(world[i][j]==Tileset.GRASS)
                {
                    up = 0;left =0;down = 0;right = 0;
                    world[i][j]=Tileset.WATER;
                    st[i][j] = true;

                    find_wall(i,j); //先将i,j中的周围的墙加入

                    dfs_grass(i,j);  //遍历房间里面的草,并将房间至成true

                    int cnt=0;
                    boolean load = false;
                    while(cnt<4||!load)
                    {
                        int dir = random.nextInt(4);
                        int x;
                        if(dir==0)x = random.nextInt(up);
                        else if(dir==1) x = random.nextInt(left);
                        else if(dir==2) x = random.nextInt(up);
                        else x = random.nextInt(left);
                        load |= search(wall[dir][x].x,wall[dir][x].y,random);
                        cnt++;
                    }
                    change();
                }
            }
    }

    public void room_generate(Random random,Test1 t)
    {
        for(int i=1;i<=200;i++)
        {
            int starx = random.nextInt(WIDTH)/2 * 2;
            int stary = random.nextInt(HEIGHT)/2 * 2;
            int len = random.nextInt(WIDTH / 4)/2 * 2;
            int wid = random.nextInt(HEIGHT / 4)/2 * 2;

            //System.out.println(starx+" "+stary+" "+len+" "+wid);

            if (len < 3 || wid < 3) continue;
            bulid(starx, stary, len, wid);
        }
    }

    public void checkend(int x,int y)
    {
        int cnt=0;
        int x1 = -1,y1 = -1;
        for(int i=0;i<4;i++)
        {
            int a = x+dx[i],b=y+dy[i];
            if(bodercheck(a,b)&&world[a][b]==Tileset.WALL)cnt++;
            else if(bodercheck(a,b)&&world[a][b]==Tileset.FLOOR)
            {
                x1 = a;
                y1 = b;
            }
        }
        if(cnt>=3&&x!=-1&&y!=-1)
        {
            world[x][y] = Tileset.WALL;
            checkend(x1,y1);
        }
    }

    public void Deadend()
    {
        int dx1[] = new int[]{1,0,-1,0,1,-1,-1,1};
        int dy1[] = new int[]{0,1,0,-1,1,-1,1,-1};

        for(int i=0;i<WIDTH;i++)
            for(int j=0;j<HEIGHT;j++)
                checkend(i,j);

        for(int i=0;i<WIDTH;i++)
            for(int j=0;j<HEIGHT;j++)
                rst[i][j] = false;

        for(int i=0;i<WIDTH;i++)
            for(int j=0;j<HEIGHT;j++)
                if(world[i][j]==Tileset.FLOOR)
                {
                    for(int k=0;k<8;k++)
                    {
                        int a = i+dx1[k],b=j+dy1[k];
                        if(bodercheck(a,b))rst[a][b] = true;
                    }
                }

        for(int i=0;i<WIDTH;i++)
            for(int j=0;j<HEIGHT;j++)
            {
                if(world[i][j]==Tileset.WALL&&!rst[i][j])
                    world[i][j] = Tileset.NOTHING;
                //if(rst[i][j])world[i][j] = Tileset.FLOWER;
            }

    }

    //将连通的道路置位true
    public void dfs(int x,int y)
    {
        for(int i=0;i<4;i++)
        {
            int a = x+dx[i],b = y+dy[i];
            if(a>=0&&a<WIDTH&&b>=0&&b<HEIGHT&&world[a][b]==Tileset.FLOOR&&!st[a][b])
            {
                st[a][b]=true;
                dfs(a,b);
            }
        }
    }

    public static void maze_generate(Test1 t,Random random)
    {
        //找一个地图的起始点
        int x = random.nextInt(WIDTH),y = random.nextInt(HEIGHT);
        while(world[x][y] != Tileset.FLOOR)
        {
            x = random.nextInt(WIDTH);
            y = random.nextInt(HEIGHT);
        }

        for(int i=0;i<4;i++)
        {
            int a = x+t.dx[i],b = y+t.dy[i];
            if(a<=0||a>=WIDTH-1||b<=0||b>=HEIGHT-1)continue;
            t.add(x+t.dx[i],y+t.dy[i]);
        }

        while(num>0)
        {
            int idx = random.nextInt(num+1);   //从加入的队伍中随机选点
            if(idx==0)idx++;
            int a = t.grid[idx].x;
            int b = t.grid[idx].y;
            t.add_list(a,b,idx);
        }
        for(int i=0;i<WIDTH;i++)
            for(int j=0;j<HEIGHT;j++)
                t.st[i][j] = false;

        t.st[x][y] = true;

        for(int i=0;i<WIDTH;i++)
            for(int j=0;j<HEIGHT;j++)
                if(world[i][j]==Tileset.TREE)world[i][j] = Tileset.FLOOR;

        t.dfs(x,y);  //将道路至成true

        t.connect(random,t);
        t.Deadend();
    }

    public static void main(String[] args)
    {
        TERenderer ter = new TERenderer();
        ter.initialize(81,31);
        Test1 t = new Test1();

        Random random = new Random(85155614);

        t.room_generate(random,t);
        t.maze_generate(t,random);

//        for(int i=0;i<WIDTH;i++)
//            for(int j=0;j<HEIGHT;j++)
//                if(t.st[i][j])world[i][j] = Tileset.SAND;

        ter.renderFrame(world);
    }
}
