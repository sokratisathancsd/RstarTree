import java.util.ArrayList;
import java.util.Collections;


public class nonIndexQueries {

    /**
     * Implementation of sequential knn (without the R*Tree index)<br>
     * For every Location we calculate the distance between the 2 locations (the classify Location and this one)<br>
     * we sort the ArrayList of Locations according to the distance from classify location in descending order <br>
     * we keep the first k Locations from the sorted now ArrayList<br>
     * @param locations an ArrayList<Location> with all possible neighbors
     * @param classify the Location that we want to find each neighbors
     * @param k number of neighbors
     * @return an ArrayList<Location> with all the neighbors of Location classify
     */
    public ArrayList<Location> knnRun(ArrayList<Location> locations, Location classify, int k) {
        for (Location location : locations) {
            location.Manhattan_Distance(classify.getLat(), classify.getLon());

        }
        Collections.sort(locations);
        ArrayList<Location> kBest = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            kBest.add(locations.get(i));
            Point kl = new Point(locations.get(i).getLat(), locations.get(i).getLon());
            kl.setDistanceFromPoint_forKNN(kl.distance(new Point(classify.getLat(), classify.getLon())));
        }
        return kBest;
    }

    /**
     * For every Location on database we compute if it is inside the range query<br>
     * if it is, we add it on CloseAreas ArrayList.
     * @param aPoint the given Location that is the center of the range query
     * @param distance the radius in which we search for points
     * @param locations an ArrayList that contains all the Locations
     * @return the Locations that are inside the range query
     */
    public static ArrayList<Location> Range(Location aPoint, double distance, ArrayList<Location> locations) {
        ArrayList<Location> CloseAreas = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).Manhattan_Distance(aPoint.getLat(), aPoint.getLon()) <= distance) {
                CloseAreas.add(locations.get(i));
            }
        }
        return CloseAreas;
    }
}
