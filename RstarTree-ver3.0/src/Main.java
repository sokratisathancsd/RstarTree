import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static int level = 0;
    private static ArrayList<Point> Sequence = new ArrayList<>();

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        int selection = 0;
        ArrayList<Location> locations = null;
        Scanner myScanner = new Scanner(System.in);
        System.out.println("--------------------STARTING MENU--------------------");
        System.out.println("1: To load new database from OSM file");
        System.out.println("2: To load data from saved database");
        System.out.println("---------------------END OF MENU---------------------");
        selection = myScanner.nextInt();
        nonIndexQueries sKNN = new nonIndexQueries();
        RstarTree tree=new RstarTree(new RstarNode(Double.MAX_VALUE,Double.MIN_VALUE,Double.MAX_VALUE,Double.MIN_VALUE));
        switch (selection) {
            case 1:
                //Load OSM File
                LoadOSM lOSM = new LoadOSM();
                lOSM.Load();
            case 2:
                //Load Data
                LoadData ld = new LoadData();
                locations = ld.ReadClass();

                tree.ReadFile();
                tree.setRecords_Size(ld.getRecordSize());
        }

        while (selection != 0) {
            tree.createPythonFiles();
            tree.createIndexFileASSTRING();
            System.out.println("------------------------ MENU ------------------------");
            System.out.println("1: To run simple KNN without using the index");
            System.out.println("2: To run simple KNN with index");
            System.out.println("3: To run range queries without using the index");
            System.out.println("4: To run range queries with index");
            System.out.println("5: To add a new Location");
            System.out.println("0: To exit");
            System.out.println("---------------------END OF Menu---------------------");
            selection = myScanner.nextInt();
            int k;
            double lat;
            double lon;
            double axis;

            switch (selection) {
                case 1:
                    //Simple KNN
                    System.out.println("Give k: ");
                    k = myScanner.nextInt();
                    System.out.println("Give a Location");
                    System.out.print("Give lat: ");
                    lat = myScanner.nextDouble();
                    System.out.print("Give lon: ");
                    lon = myScanner.nextDouble();
                    Location classify = new Location(-1, lat, lon);
                    long startTime = System.currentTimeMillis();
                    ArrayList<Location> distances = sKNN.knnRun(locations, classify, k);

                    for(Location neib:distances){
                        System.out.println(neib.toString());
                    }
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println(elapsedTime);
                    break;
                case 2:
                    System.out.println("Give k: ");
                    k = myScanner.nextInt();
                    System.out.println("Give a Location");
                    System.out.print("Give lat: ");
                    lat = myScanner.nextDouble();
                    System.out.print("Give lon: ");
                    lon = myScanner.nextDouble();
                    Point aPoint = new Point(lat, lon);
                     startTime = System.currentTimeMillis();
                    ArrayList<Location> Neighbors = tree.knn(aPoint, k);
                    for(Location neib:Neighbors){
                        System.out.println(neib.toString());
                    }
                     elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println(elapsedTime);
                    break;
                case 3:
                    //Run range query
                    System.out.println("Give a Location");
                    System.out.print("Give lat: ");
                    lat = myScanner.nextDouble();
                    System.out.print("Give lon: ");
                    lon = myScanner.nextDouble();
                    System.out.println("Give axis: ");
                    axis = myScanner.nextDouble();
                    startTime = System.currentTimeMillis();
                    sKNN = new nonIndexQueries();
                    ArrayList<Location> CloseAreas = sKNN.Range(new Location(-1, lat, lon), axis, locations);
                    for(Location neib:CloseAreas){
                        System.out.println(neib.toString());
                    }
                    elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println(elapsedTime);
                    break;
                case 4:
                    //Run range query
                    System.out.println("Give a Location");
                    System.out.print("Give lat: ");
                    lat = myScanner.nextDouble();
                    System.out.print("Give lon: ");
                    lon = myScanner.nextDouble();
                    System.out.println("Give axis: ");
                    axis = myScanner.nextDouble();
                    startTime = System.currentTimeMillis();
                    sKNN = new nonIndexQueries();
                    ArrayList<Location> CloseAreasA = tree.Range(new Point(lat,lon),axis);
                    for(Location neib:CloseAreasA){
                        System.out.println(neib.toString());
                    }
                    elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println(elapsedTime);
                    break;

               /* case 5:
                    //Add new Element
                    System.out.println("Give a Location");
                    System.out.print("Give id: ");
                    long tempId = myScanner.nextLong();
                    System.out.print("Give lat: ");
                    double tempLat = myScanner.nextDouble();
                    System.out.print("Give lon: ");
                    double tempLon = myScanner.nextDouble();
                    Location newLocation = new Location(tempId, tempLat, tempLon);
                    LoadOSM lOSM = new LoadOSM();
                    lOSM.addLocation(newLocation, locations);
                    LoadData ld = new LoadData();
                    locations = ld.ReadClass();
                    System.out.println("Location: " + newLocation.toString() + " added.");
                    break;*/
                case 0:
                    System.exit(0);
            }
        }

    }
}

