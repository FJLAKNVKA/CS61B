

public class Planet 
{
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;

    private static final double G=6.67e-11;

    //构造函数
    public Planet(double xP, double yP, double xV,double yV, double m, String img)
    {
        xxPos=xP;
        yyPos=yP;
        xxVel=xV;
        yyVel=yV;
        mass=m;
        imgFileName=img;
    }
    public Planet(Planet p)
    {
        xxPos=p.xxPos;
        yyPos=p.yyPos;
        xxVel=p.xxVel;
        yyVel=p.yyVel;
        mass=p.mass;
        imgFileName=p.imgFileName;
    }

    //计算距离
    public double calcDistance(Planet p)
    {
        double dx=this.xxPos-p.xxPos;
        double dy=this.yyPos-p.yyPos;
        return Math.sqrt(dx*dx+dy*dy);
    }

    //计算合力
    public double calcForceExertedBy(Planet p)
    {
        double dist=calcDistance(p);
        return (G*this.mass*p.mass)/(dist*dist);
    }

    //计算x方向分力
    public double calcForceExertedByX(Planet p)
    {
        double dist=calcDistance(p);
        double F=calcForceExertedBy(p);
        return (p.xxPos-this.xxPos)/dist*F;
    }
    public double calcForceExertedByY(Planet p)
    {
        double dist=calcDistance(p);
        double F=calcForceExertedBy(p);
        return (p.yyPos-this.yyPos)/dist*F;
    }

    //计算所有新星对当前新星的x分力
    public double calcNetForceExertedByX(Planet [] P)
    {
        double TotalF=0;
        for(Planet q:P)
        {
            if(q.equals(this))continue;
            TotalF+=calcForceExertedByX(q);
        }
        return TotalF;
    }

    //计算所有新星对当前新星的y分力
    public double calcNetForceExertedByY(Planet [] P)
    {
        double TotalF = 0;
        for(Planet q:P)
        {
            if(q.equals(this))continue;
            TotalF += calcForceExertedByY(q);
        }
        return TotalF;
    }

    //更新距离
    public void update(double time,double xxF,double yyF)
    {
        double xxA=xxF/this.mass;
        double yyA=yyF/this.mass;
        xxVel+=time*xxA;
        yyVel+=time*yyA;
        this.xxPos+=time*xxVel;
        this.yyPos+=time*yyVel;
    }
    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
    }
}
