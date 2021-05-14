import static java.lang.Math.abs;

/**
 * Represents a Location with Latitude and Longitude
 * @author Sokratis Athanasiadis
 * @author Konstantinos Perrakis
 * @version 1.0
 */
public class Location implements Comparable<Location>{
    private long id;
    private double lat, lon;
    private double distance;
    private int blockId,RegisterId;

    /**
     * Constructor of Location class
     * @param id every Location on OSM has a unique id
     * @param lat the latitude value of a location
     * @param lon the longitude value of a location
     */
    public Location(long id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        distance = Double.MAX_VALUE;
    }

    /**
     * Getter for Latitude
     * @return lat
     */
    public double getLat() {

        return lat;
    }

    /**
     * Getter for longitude
     * @return lon
     */
    public double getLon() {

        return lon;
    }

    /**
     * Getter for location's id
     * @return id
     */
    public long getId() {


        return id;
    }

    /**
     * Calculates Manhattan Distance between 2 Locations<br>
     * also a setter for distance field
     * @param x latitude of location2
     * @param y longitude of location2
     * @return the manhattan distance between the location and location2
     */
    public double Manhattan_Distance(double x, double y) {
        distance = abs((x - lat)) + abs(y - lon);
        return distance;

    }

    public void setDistance(double aDistance){
        distance=aDistance;
    }

    /**
     * Getter for distance
     * @return distance
     */
    public double getDistance() {
        if (distance == Double.MAX_VALUE) {
            System.out.println("ERROR WITH MAX VALUE");
            System.exit(-1);
        }
        return distance;
    }

    /**
     * Converts location to a string so it can be printed
     * @return a string to be printed in the form id= anId lat=aLat lon=aLon dist=aDistance
     */
    @Override
    public String toString() {
        return "id:" + id + "   lat= " + lat + "   lon= " + lon+ "  dist= "+distance;
    }

    /**
     * Custom comparator to compare 2 locations according to their distances values
     * @param l2 the location that we compare
     * @return 1 if is bigger -1 if it is not, 0 if are equal
     */
    @Override
    public int compareTo(Location l2) {
        if (distance > l2.getDistance()) {
            return 1;
        } else if (distance < l2.getDistance()) {
            return -1;
        }
        return 0;
    }
}
