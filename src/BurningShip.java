import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator {
    public static final int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double range){
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }
    public int numIterations(double x, double y){
        int iteration = 0;
        double zre =0;
        double zim = 0;
        while (iteration<MAX_ITERATIONS && zre*zre+zim*zim<4){
            double zreUp = zre*zre - zim*zim + x;
            double zimUp = 2 * Math.abs(zre)*Math.abs(zim)+y;
            zre = zreUp;
            zim = zimUp;
            iteration +=1;
        }
        if (iteration==MAX_ITERATIONS) return -1;
        return iteration;
    }
    public String toString(){
        return "Burning Ship";
    }
}