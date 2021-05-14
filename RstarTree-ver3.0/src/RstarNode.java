import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Sokratis Athanasiadis
 * @author Konstantinos Perrakis
 * @version 2.7BETA
 */
public class RstarNode implements Serializable {
    private ArrayList<RstarNode> nodes;
    private ArrayList<Point> points;
    private MyRectangle rectangle;
    private int M=5;
    private int m=2;
    private double distanceFromPoint_forKNN = Double.MAX_VALUE;


    /**
     * Represents a Node of the R*Tree
     * @param x1 the lower value on x-axis
     * @param x2 the upper value on x-axis
     * @param y1 the lower value on y-axis
     * @param y2 the upper value on y-axis
     */
    public RstarNode(double x1, double x2, double y1, double y2){
        rectangle = new MyRectangle(x1,x2,y1,y2);
        nodes = new ArrayList<>(M);
        points = new ArrayList<>(M);
    }



    /**
     * Checks if the Node is a Leaf Node
     * @return true if is leaf, false if it is not leaf
     */
    public boolean isLeaf(){
        return nodes.isEmpty() ;
    }



    /**
     * Getter for the Rectangle of the Node
     * @return the rectangle of the Node
     */
    public MyRectangle getRectangle(){
        return rectangle;
    }



    /**
     * Adds a child
     * @param aNode an RstarNode to be added as child Node
     */
    public void addChildNode(RstarNode aNode){
        nodes.add(aNode);
        rectangle.setNewDimensions(aNode.getRectangle().getXXYY().get(0),aNode.getRectangle().getXXYY().get(1),aNode.getRectangle().getXXYY().get(2),aNode.getRectangle().getXXYY().get(3));
    }



    /**
     * Adds a point if the Node is a leaf Node
     * @param aPoint a Point to be added
     */
    public void addPoint(Point aPoint){
        points.add(aPoint);
        rectangle.setNewDimensions(aPoint.getLat(),aPoint.getLon());
    }



    /**
     * Getter for all the child Nodes
     * @return an ArrayList of RstarNodes that contains the children of the Node
     */
    public ArrayList<RstarNode> getChildren(){
        return nodes;
    }



    /**
     * Getter for the points of the Node, if the Node is a Leaf node
     * @return an ArrayList of Points that are inside the Leaf Node
     */
    public ArrayList<Point> getPoints(){
        return points;
    }



    /**
     * Setter for the children of the node
     * @param someNodes an ArrayList of Nodes that we want to add as children
     */
    public void setChildren(ArrayList<RstarNode> someNodes){
        nodes.clear();
        nodes=someNodes;
    }



    /**
     * Setter for the points of the nodes
     * @param somePoints an ArrayList of Points that we want to add as Points if the Node is a Leaf Node
     */
    public void setPoints(ArrayList<Point> somePoints){
        points.clear();
        points=somePoints;
    }



    /**
     * A custom comparator of 2 RstarNodes based on their Rectangles we use it on ChooseSplitAxis method<br>
     * First sorted by the lower x value, and then by the upper x value
     */
    static class RectangleComparatorX implements Comparator<RstarNode> {
        @Override
        public int compare(RstarNode o1, RstarNode o2) {
            if(o1.getRectangle().getXXYY().get(0) > o2.getRectangle().getXXYY().get(0)){
                return -1;
            }
            else if(o1.getRectangle().getXXYY().get(0) < o2.getRectangle().getXXYY().get(0)){
                return 1;
            }
            else if(o1.getRectangle().getXXYY().get(0).equals(o2.getRectangle().getXXYY().get(0))){
                if(o1.getRectangle().getXXYY().get(1) > o2.getRectangle().getXXYY().get(1)){
                    return -1;
                }
                else if(o1.getRectangle().getXXYY().get(1) < o2.getRectangle().getXXYY().get(1)){
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
     * A custom comparator of 2 RstarNodes based on their Rectangles we use it on ChooseSplitAxis method<br>
     * First sorted by the lower y value, and then by the upper y value
     */
    static class RectangleComparatorY implements Comparator<RstarNode>{
        @Override
        public int compare(RstarNode o1, RstarNode o2) {
            if(o1.getRectangle().getXXYY().get(2) > o2.getRectangle().getXXYY().get(2)){
                return -1;
            }
            else if(o1.getRectangle().getXXYY().get(2) < o2.getRectangle().getXXYY().get(2)){
                return 1;
            }
            else if(o1.getRectangle().getXXYY().get(2).equals(o2.getRectangle().getXXYY().get(2))){
                if(o1.getRectangle().getXXYY().get(3) > o2.getRectangle().getXXYY().get(3)){
                    return -1;
                }
                else if(o1.getRectangle().getXXYY().get(3) < o2.getRectangle().getXXYY().get(3)){
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
     * Getter for the distance between RstarNode's Rectangle and a Point<br>
     * Used only for KNN algorithm
     * @return the distance between a Point and an RstarNode
     */
    public double getDistanceFromPoint_forKNN(){
        return distanceFromPoint_forKNN;
    }



    /**
     * Setter for the distance between RstarNode's Rectangle and a Point<br>
     * Used only for KNN algorithm
     * @param aDistance the distance between a Point and an RstarNode
     */
    public void setDistanceFromPoint_forKNN(double aDistance){
        distanceFromPoint_forKNN=aDistance;
    }



}

