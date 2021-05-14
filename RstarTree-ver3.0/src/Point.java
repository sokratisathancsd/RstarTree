import java.util.Comparator;
import java.io.Serializable;

/**
 * Represents a Point in a 2-dimensional space<br>
 * It has a blockId and a registerId to know where the point on datafile is
 * @author Sokratis Athanasiadis
 * @author Konstantinos Perrakis
 * @version 1.9
 */
public class Point implements Serializable {
    private double lat;//x
    private double lon;//y
    private int blockId;
    private int registerId;
    private double distance; //use for knn

    /**
     * Constructor of Class Point
     * @param lat the latitude value of the point
     * @param lon the longitude value of the point
     * @param blockId the blockId on datafile that we can find this point
     * @param registerId the registerId of a point in a blockId in the datafile
     */
    public Point(double lat, double lon , int blockId, int registerId){
        this.lat=lat;
        this.lon=lon;
        this.blockId=blockId;
        this.registerId=registerId;

    }

    /**
     * Constructor of the Class Point
     * @param lat the latitude value of the point
     * @param lon the longitude value of the point
     */
    public Point(double lat, double lon){
        this.lat=lat;
        this.lon=lon;
    }


    /**
     * Returns the distance of the point from a Rectangle cause it is needed to implement the KNN algorithm
     * @return distance of the Point from a Rectangle
     */
    public double getDistanceFromPoint_forKNN() {
        return distance;
    }

    /**
     * Sets the distance of the Point from a Rectangle, it is needed for KNN algorithm implementation
     * @param distance is the of the point from a Rectangle
     */
    public void setDistanceFromPoint_forKNN(double distance) {
        this.distance = distance;
    }


    /**
     * Getter for blockId
     * @return the blockId of the datafile that the point is
     */
    public int getBlockId(){
        return blockId;
    }

    /**
     * Getter for registerId
     * @return the registerId of the blockId of the datafile where the point is
     */
    public int getRegisterId(){
        return registerId;
    }

    /**
     * Getter for latitude value of the point
     * @return the latitude value of the point
     */
    public double getLat(){
        return lat;
    }

    /**
     * Getter for longitude
     * @return the longidute value of the point
     */
    public double getLon(){
        return lon;
    }

    /**
     * A custom made comparator that is needed in the ChooseSplitAxis algorithm of R*Tree
     * Compares latitude values of 2 points
     */
    static class PointsComparatorX implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            return Double.compare(o2.getLat(), o1.getLat());
        }
    }

    /**
     * A custom comparator that is needed in the ChooseSplitAxis algorithm of R*Tree
     * Compares the longitude values of 2 points
     */
    static class PointsComparatorY implements Comparator<Point> {
        @Override
        public int compare(Point o1, Point o2) {
            return Double.compare(o2.getLon(), o1.getLon());
        }
    }

    /**
     * Manhattan distance of 2 points
     * @param aPoint a given point2 that we want to check how far from the point1 is
     * @return the distance between the 2 points
     */
     public double distance(Point aPoint){
        return Math.abs(lat-aPoint.getLat())+Math.abs(lon-aPoint.getLon());
    }

}
