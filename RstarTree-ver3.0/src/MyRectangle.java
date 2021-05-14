import java.util.ArrayList;
import java.util.Comparator;
import java.io.Serializable;

/** Represents a Rectangle.
 * @author Sokratis Athanasiadis
 * @author Konstantinos Perrakis
 * @version 1.4
 */
public class MyRectangle implements Serializable {
    private double x1,x2=0;
    private double y1,y2=0;

    /**
     * A rectangle is given by 4 values [x1,x2,y1,y2]
     * @param x1 the lowest value on the x-axis
     * @param x2 the upper value on the x-axis
     * @param y1 the lower value on the y-axis
     * @param y2 the upper value on the y-axis
     */
    public MyRectangle(double x1,double x2, double y1, double y2){
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
    }


    /**
     * Returns all 4 values of a Rectangle
     * @return an ArrayList of Doubles [x1,x2,y1,y2]
     */
    public ArrayList<Double> getXXYY(){
        ArrayList<Double> temp = new ArrayList<>(4);
        temp.add(x1);
        temp.add(x2);
        temp.add(y1);
        temp.add(y2);
        return temp;
    }


    /**
     * Getter for the lower value on the x-axis
     * @return x1
     */
    public double getX1(){
        return x1;
    }


    /**
     * Getter for the upper value on the y-axis
     * @return x2
     */
    public double getX2(){
        return x2;
    }


    /**
     * Getter of the lower value on the y-axis
     * @return y1
     */
    public double getY1(){
        return y1;
    }


    /**
     * Getter fo the upper value on the y-axis
     * @return return upper value on y-axis
     */
    public double getY2(){
        return y2;
    }


    /**
     * Checks if a Point is inside a Rectangle
     * @param x the value on the x-axis of the Point
     * @param y the value on the y-axis of the Point
     * @return true if Point is inside rectangle , else if not
     */
    public boolean contains(double x, double y){
        if(x1<=x && x<=x2 && y1<=y && y<=y2){
            return true;
        }
        else{
            return false;
        }
    }


    /**
     * Get area of the Rectangle
     * @return the area of the rectangle
     */
    public double getArea(){
        return (x2-x1)*(y2-y1);
    }


    /**
     * Calculates how much the area will change if we extend the Rectangle according to a Point
     * @param x the value on the x-axis of the point
     * @param y the value on the y-axis of the point
     * @return how much will the area be after the insertion of the (x,y) point into the rectangle
     */
    public double getNewArea(double x, double y){
        if(x1<=x && x<=x2 && y>y2){
            return (x2-x1)*(y-y1);
        }
        else if(y1<=y && y<=y2 && x>x2){
            return (x-x1)*(y2-y1);
        }
        else if(x1<=x && x<=x2 && y<y1){
            return (x2-x1)*(y2-y);

        }
        else if(y1<=y && y<=y2 && x<x1){
            return (x2-x)*(y2-y1);
        }
        else if(x<x1 && y>y2){
            return (x2-x)*(y-y1);
        }
        else if(x>x2 && y>y2){
            return (x-x1)*(y-y1);
        }
        else if(x>x2 && y<y1){
            return (x-x1)*(y2-y);
        }
        else if(x<x1 && y<y1){
            return (x2-x)*(y2-y);
        }
        else if(x>=x1 && x<=x2 && y>=y1 && y<=y2){
            return (x2-x1)*(y2-y1);//means that the point is inside the Rectangle
        }
        else {
            System.out.println("ERROR ON CALCULATING NEW AREA");
            return -1;
        }
    }


    /**
     * Changes dimensions of Rectangle according to new [x1,x2,y1,y2] values
     * @param x1 the lower value on the x-axis
     * @param x2 the upper value on the x-axis
     * @param y1 the lower value on the y-axis
     * @param y2 the upper value on the y-axis
     */
    public void setDimensions(double x1, double x2, double y1, double y2){
        this.x1=x1;
        this.x2=x2;
        this.y1=y1;
        this.y2=y2;
    }


    /**
     *  Adds a new point on the Rectangle so each dimension change
     * @param x the value on the x-axis of the point
     * @param y the value on the y-axis of the point
     */
    public void setNewDimensions(double x, double y){
        if(x1==Double.MAX_VALUE && x2==Double.MIN_VALUE && y1==Double.MAX_VALUE && y2==Double.MIN_VALUE){
            x1=x;
            x2=x;
            y1=y;
            y2=y;
            return;

        }
        if(x1<=x && x<=x2 && y>y2){
            y2=y;
        }
        else if(y1<=y && y<=y2 && x>x2){
            x2=x;
        }
        else if(x1<=x && x<=x2 && y<y1){
            y1=y;
        }
        else if(y1<=y && y<=y2 && x<x1){
            x1=x;
        }
        else if(x<x1 && y>y2){
            x1=x;
            y2=y;
        }
        else if(x>x2 && y>y2){
            x2=x;
            y2=y;
        }
        else if(x>x2 && y<y1){
            x2=x;
            y1=y;
        }
        else if(x<x1 && y<y1){
            x1=x;
            y1=y;
        }
        else if(x<=x2 && x>=x1 && y>=y1 && y<=y2){
        }
        else System.out.println("ERROR ON CHANGING DIMENSIONS");
    }


    /**
     * Extends a dimension according to another Rectangle
     * @param xx1 lower value on x-axis
     * @param xx2 upper value on x-axis
     * @param yy1 lower value on y-axis
     * @param yy2 upper value on y-axis
     */
    public void setNewDimensions(double xx1, double xx2, double yy1, double yy2){
        if(x1==Double.MAX_VALUE && x2==Double.MIN_VALUE && y1==Double.MAX_VALUE && y2==Double.MIN_VALUE){
            x1=xx1;
            x2=xx2;
            y1=yy1;
            y2=yy2;
            return;
        }
        if(xx1<x1){
            x1=xx1;
        }
        if(xx2>x2){
            x2=xx2;
        }
        if(yy1<y1){
            y1=yy1;
        }
        if(yy2>y2){
            y2=yy2;
        }
    }


    /**
     * Calculates Margin of the Rectangle
     * @return the Margin of the Rectangle
     */
    public double getMargin(){
        return 2*(x2-x1)+2*(y2-y1);
    }


    /**
     * First sorted by the lower x value, and then by the upper x value<br>
     * It is a custom made comparator that is needed for the ChooseSplitAxis of R*Tree
     */
    static class RectangleComparatorX implements Comparator<MyRectangle>{
        @Override
        public int compare(MyRectangle o1, MyRectangle o2) {
            if(o1.getXXYY().get(0) > o2.getXXYY().get(0)){
                return -1;
            }
            else if(o1.getXXYY().get(0) < o2.getXXYY().get(0)){
                return 1;
            }
            else if(o1.getXXYY().get(0).equals(o2.getXXYY().get(0))){
                if(o1.getXXYY().get(1) > o2.getXXYY().get(1)){
                    return -1;
                }
                else if(o1.getXXYY().get(1) < o2.getXXYY().get(1)){
                    return 1;
                }
                else {
                    return 0;
                }
            }
            else{
                return 0;
            }
        }
    }


    /**
     * First sorted by the lower y value, and then by the upper y value<br>
     * It is a custom made comparator that is needed for the ChooseSplitAxis of the R*Tree
     */
    static class RectangleComparatorY implements Comparator<MyRectangle>{
        @Override
        public int compare(MyRectangle o1, MyRectangle o2) {
            if(o1.getXXYY().get(2) > o2.getXXYY().get(2)){
                return -1;
            }
            else if(o1.getXXYY().get(2) < o2.getXXYY().get(2)){
                return 1;
            }
            else if(o1.getXXYY().get(2).equals(o2.getXXYY().get(2))){
                if(o1.getXXYY().get(3) > o2.getXXYY().get(3)){
                    return -1;
                }
                else if(o1.getXXYY().get(3) < o2.getXXYY().get(3)){
                    return 1;
                }
                else {
                    return 0;
                }
            }
            else{
                return 0;
            }
        }
    }


    /**
     * Calculates the distance between an Point and a Rectangle
     * @param aPoint a given Point that we want to check how far from the Rectangle it is
     * @return the distance between the given Point and the Rectangle
     */
    public double distance(Point aPoint){
        double x=aPoint.getLat();
        double y=aPoint.getLon();
        if(x1<=x && x<=x2 && y>y2){
            return y-y2;
        }
        else if(y1<=y && y<=y2 && x>x2){
            return x-x2;
        }
        else if(x1<=x && x<=x2 && y<y1){
            return y1-y;
        }
        else if(y1<=y && y<=y2 && x<x1){
            return x1-x;
        }
        else if(x<x1 && y>y2){
            return Math.abs(x1-x)+Math.abs(y2-y);
        }
        else if(x>x2 && y>y2){
            return Math.abs(x2-x)+Math.abs(y2-y);
        }
        else if(x>x2 && y<y1){
            return Math.abs(x2-x)+Math.abs(y1-y);
        }
        else if(x<x1 && y<y1){
            return Math.abs(x1-x)+Math.abs(y1-y);
        }
        return 0;                                   // If x is inside Rectangle
    }


    public double  getOverlap(MyRectangle rect2){
        double xx1=rect2.getX1();
        double xx2=rect2.getX2();
        double yy1=rect2.getY1();
        double yy2=rect2.getY2();
        double overlap_x=Math.max(0,Math.min(x2,xx2)-Math.max(x1,xx1));
        double overlap_y=Math.max(0,Math.min(y2,yy2)-Math.max(y1,yy1));
        if(overlap_x==0){
            return overlap_y;
        }
        if(overlap_y==0){
            return overlap_x;
        }
        return overlap_x*overlap_y;
    }


}
