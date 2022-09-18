import javax.swing.plaf.metal.MetalBorders.PaletteBorder;

public class NBody {
    //获取半径
    public static double readRadius(String path)
    {
        In in =new In(path);
        in.readInt();
        return in.readDouble();
    }

    //获取星球
    public static Planet[] readPlanets(String path)
    {
        In in = new In(path);
        int num = in.readInt();
        Planet[] planets = new Planet[num];
        in.readDouble();
        for(int i=0;i<num;i++)
        {
            double xxPos=in.readDouble();
            double yyPos=in.readDouble();
            double xxVel=in.readDouble();
            double yyVel=in.readDouble();
            double mass=in.readDouble();
            String imgFileName=in.readString();
            Planet planet = new Planet(xxPos,yyPos,xxVel,yyVel,mass,imgFileName);
            planets[i]=planet;
        }
        return planets;
    }
    public static void main(String[] args)
    {
           double T=Double.parseDouble(args[0]);
           double dt=Double.parseDouble(args[1]);
           String filename = args[2]; 
           double radius = readRadius(filename);
           Planet[] planets = readPlanets(filename);
           int n = planets.length;

           StdDraw.enableDoubleBuffering();

           StdDraw.setScale(-radius,radius);    
           StdDraw.picture(0, 0, "images/starfield.jpg");
           for(Planet planet:planets)
           {
                planet.draw();
           }

           for(double t=0;t<T;t+=dt)
           {
                double[] XF=new double[n];
                double[] YF=new double[n];
                for(int i=0;i<n;i++)
                {
                    XF[i] = planets[i].calcNetForceExertedByX(planets);
                    YF[i] = planets[i].calcNetForceExertedByY(planets);
                }
                for(int i=0;i<n;i++)
                {
                    planets[i].update(dt,XF[i], YF[i]);
                }
                StdDraw.picture(0, 0, "images/starfield.jpg");
                
                for (Planet planet : planets) 
                {
                    planet.draw();
                }
                
                StdDraw.show();
                StdDraw.pause(10);
            }
            StdOut.printf("%d\n",planets.length);
            StdOut.printf("%.2e\n",radius);
            for(int i=0;i<planets.length;i++)
            {
                StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", planets[i].xxPos, planets[i].yyPos,
                    planets[i].xxVel, planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
            }
    }
}
