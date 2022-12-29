package byog.Core;
import java.io.Serializable;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    private Point player;

    private Point outdoor;

    private String state = "";

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        menu();
        char op = menuoption();

        if(op == 'n')Gamestart();
        else if(op == 'l')loadgame();
        else if(op == 'q')
        {
            System.out.println(state);
            System.exit(0);
        }
    }

    public void menu()
    {
        initializeCanvas();

        Font font = new Font("Monaco",Font.PLAIN,40);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "CS61B: THE GAME");

        Font sfont = new Font("Monaco",Font.PLAIN,20);
        StdDraw.setFont(sfont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Quit (Q)");

        StdDraw.show();
    }
    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    private void initializeCanvas()
    {
        StdDraw.setCanvasSize((WIDTH+1)*16,(HEIGHT+1)*16);
        StdDraw.setXscale(0, WIDTH + 1);
        StdDraw.setYscale(0, HEIGHT + 1);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
    }

    private char menuoption()
    {
        char str;
        while(true)
        {
            if(!StdDraw.hasNextKeyTyped())continue;
            str = Character.toLowerCase(StdDraw.nextKeyTyped());
            state += str;
            if(str == 'n'||str == 'l'||str == 'q')
                return str;
        }
    }

    private void Gamestart()
    {
        long seed = getseed();
        ter.initialize(WIDTH + 1, HEIGHT + 1);
        System.out.println(seed);
        worldgenerate t = new worldgenerate(seed);
        TETile[][] world = t.Theworld();
        player = new Point();
        outdoor = new Point();
        player = t.Play();
        outdoor = t.door();
        ter.renderFrame(world);
        play(world);
    }

    private long getseed()
    {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 50));
        StdDraw.text(WIDTH/2,3 * HEIGHT / 4,"Please enter a random seed:");
        StdDraw.show();

        String strseed = "";
        while(true)
        {
            StdDraw.clear(Color.BLACK);
            StdDraw.setFont(new Font("Monaco", Font.PLAIN, 50));
            StdDraw.text(WIDTH/2,3 * HEIGHT / 4,"Please enter a random seed:");

            char digit;
            if(!StdDraw.hasNextKeyTyped())continue;
            digit = Character.toLowerCase(StdDraw.nextKeyTyped());
            state += digit;
            if(digit!='s')
            {
                if(!Character.isDigit(digit))continue;
                strseed += digit;
                StdDraw.setFont(new Font("Monaco", Font.PLAIN, 30));
                StdDraw.text(WIDTH / 2, HEIGHT / 2, strseed);
                StdDraw.show();
            }
            else return convertSeed(strseed);
        }
    }

    private void play(TETile[][] world)
    {
        while(true)
        {
            while(!StdDraw.hasNextKeyTyped())continue;
            char op = Character.toLowerCase(StdDraw.nextKeyTyped());
            //System.out.println(StdDraw.mouseX()+" " + StdDraw.mouseY());
            state += op;
            if(op == 'w')
            {
                //System.out.println("YES");
                upwalk(world);
                ter.renderFrame(world);
            }
            else if(op == 'a')
            {
                leftwalk(world);
                ter.renderFrame(world);
            }
            else if(op == 'd')
            {
                rightwalk(world);
                ter.renderFrame(world);
            }
            else if(op == 's')
            {
                downwalk(world);
                ter.renderFrame(world);
            }
            else if(op == ':')
            {
                while(true)
                {
                    if(!StdDraw.hasNextKeyTyped())continue;
                    char str = Character.toLowerCase(StdDraw.nextKeyTyped());
                    if(str == 'q')
                    {
                        savegame(world);
                        System.out.println(state);
                        System.exit(0);
                    }
                    else break;
                }
            }
        }
    }

    private TETile[][] play(TETile[][] world, int star, String str)
    {
        for(int i = star; i < str.length(); i++)
        {
            char op = str.charAt(i);
            if(op == 'w') upwalk(world);
            else if(op == 'a') leftwalk(world);
            else if(op == 'd') rightwalk(world);
            else if(op == 's') downwalk(world);
            else if(op == ':')
            {
                if(i + 1 < str.length() && str.charAt(i+1) == 'q')
                {
                    savegame(world);
                    return world;
                }
                else continue;
            }
        }
        return world;
    }

    private void upwalk(TETile[][] world)
    {
        int x = player.x, y = player.y+1;

        if(world[x][y].character() == '·')
        {
            world[player.x][player.y] = Tileset.FLOOR;
            player.x = x;
            player.y = y;
            world[x][y] = Tileset.PLAYER;
        }
        else if(world[x][y].character() == '█')world[x][y] = Tileset.UNLOCKED_DOOR;
        else if(world[x][y].character() == '▢')wingame();
    }

    private void leftwalk(TETile[][] world)
    {
        int x = player.x - 1, y = player.y;
        if(world[x][y].character()=='·')
        {
            world[player.x][player.y] = Tileset.FLOOR;
            player.x = x;
            player.y = y;
            world[x][y] = Tileset.PLAYER;
        }
        else if(world[x][y].character() == '█')world[x][y] = Tileset.UNLOCKED_DOOR;
        else if(world[x][y].character() == '▢')wingame();
    }

    private void rightwalk(TETile[][] world)
    {
        int x = player.x + 1, y = player.y;
        if(world[x][y].character()=='·')
        {
            world[player.x][player.y] = Tileset.FLOOR;
            player.x = x;
            player.y = y;
            world[x][y] = Tileset.PLAYER;
        }
        else if(world[x][y].character() == '█')world[x][y] = Tileset.UNLOCKED_DOOR;
        else if(world[x][y].character() == '▢')wingame();
    }

    private void downwalk(TETile[][] world)
    {
        int x = player.x, y = player.y - 1;
        if(world[x][y].character()=='·')
        {
            world[player.x][player.y] = Tileset.FLOOR;
            player.x = x;
            player.y = y;
            world[x][y] = Tileset.PLAYER;
        }
        else if(world[x][y].character() == '█')world[x][y] = Tileset.UNLOCKED_DOOR;
        else if(world[x][y].character() == '▢')wingame();
    }

    private void wingame()
    {
        System.out.println("Win");
    }

    private void savegame(TETile[][] finalWorld)
    {
        //System.out.println("YES!!!!!!!!");
        try
        {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("output.txt"));
            out.writeObject(finalWorld);
            out.writeObject(player);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadgame()
    {
        ter.initialize(WIDTH + 1,HEIGHT + 1);
        TETile[][] world = getgame();
        ter.renderFrame(world);
        play(world);
    }

    private TETile[][] loadgame(String op)
    {
        TETile[][] world = getgame();
        play(world, 1, op);
        return world;
    }

    private TETile[][] getgame()
    {
        TETile[][] finalWorld = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("output.txt"));
            finalWorld = (TETile[][]) in.readObject();
            player = (Point) in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return finalWorld;
    }

    private long convertSeed(String str)
    {
        return Long.valueOf(str.toString());
    }


    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        System.out.println(input);
        TETile[][] finalWorldFrame = null;
        String str = tolower(input);
        char op = str.charAt(0);
        if(op == 'n') finalWorldFrame = Gamestart(str);
        else if(op == 'l') finalWorldFrame = loadgame(str);
        else if(op == 'q') System.exit(0);

        return finalWorldFrame;
    }

    private String tolower(String op)
    {
        StringBuilder t = new StringBuilder();
        for(int i=0;i<op.length();i++)
        {
            char c = Character.toLowerCase(op.charAt(i));
            t.append(c);
        }
        return t.toString();
    }

    private TETile[][] Gamestart(String op)
    {
        TETile[][] world = null;
        int idx = op.indexOf('s');
        long seed = convertSeed(op.substring(1,idx));
        //System.out.println(seed);
        worldgenerate t = new worldgenerate(seed);
        world = t.Theworld();
        player = new Point();
        outdoor = new Point();
        player = t.Play();
        outdoor = t.door();
        System.out.println(op.length());
        play(world, idx + 1, op);
        return world;
    }
}
