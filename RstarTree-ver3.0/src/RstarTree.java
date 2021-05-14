import java.io.*;
import java.util.ArrayList;
import java.util.Stack;


/**
 * @author Sokratis Athanasiadis
 * @author Konstantinos Perrakis
 * @version 2.1
 */
public class RstarTree {
    private RstarNode root;
    private int M = 5;
    private int m = 2;
    private ArrayList<RstarNode> path;
    private int Records_Size;

    /**
     * Constructor of RstarTree Class
     *
     * @param root is an RstarNode that is the root of the R*Tree
     */
    public RstarTree(RstarNode root) {
        this.root = root;
    }


    /**
     * Getter for getRecords_Size
     * @return Records_Size
     */
    public int getRecords_Size() {
        return Records_Size;
    }


    /**
     * Setter for Records_Size
     * @param records_Size new records_size
     */
    public void setRecords_Size(int records_Size) {
        Records_Size = records_Size;
    }


    /**
     * Getter for the root Node
     *
     * @return the root RstarNode
     */
    public RstarNode getRoot() {
        return root;
    }

    /**
     * Algorithm ChooseSubtree<br>
     * CSl Set N to be the root<br>
     * CS2 If N 1s a leaf,<br>
     * return N<br>
     * else Choose the entry m N whose rectangle needs least<br>
     * area enlargement to include the new data Resolve<br>
     * ties by choosing the entry with the rectangle of<br>
     * smallest area<br>
     * end<br>
     * CS3 Set N to be the childnode pointed to by the<br>
     * childpointer of the chosen entry an repeat from CS2<br>
     *
     * @param node an RstarNode (starts with the root Node)
     * @param aPoint the point that we want to insert into the database
     * @return an RstarNode
     */
    public RstarNode ChooseSubtree(RstarNode node, Point aPoint) {
        RstarNode N = node;
        path.add(node);
        if (N.isLeaf()) {
            return N;
        } else {
            RstarNode N1 = null;
            double minimumExtend = Double.MAX_VALUE;
            for (int i = 0; i < N.getChildren().size(); i++) {
                double tempMin = N.getChildren().get(i).getRectangle().getNewArea(aPoint.getLat(), aPoint.getLon()) - N.getChildren().get(i).getRectangle().getArea();
                //System.out.println(tempMin);
                if (tempMin < minimumExtend) {
                    minimumExtend = tempMin;
                    N1 = N.getChildren().get(i);

                }

            }
            return ChooseSubtree(N1, aPoint);
        }


    }

    /**
     * Getter for M value
     *
     * @return maximum number of Nodes that can be as Children on a Node
     */
    public int getM() {
        return M;
    }

    /**
     * Setter for M value
     *
     * @param m maximum number of Nodes that can be as Children on a Node
     */
    public void setM(int m) {
        M = m;
    }

    /**
     * Inserts a New Point on the R*Tree<br>
     * Implementation explained on flow chart
     *
     * @param aPoint the Point that we want to Insert to the R*Tree
     */
    public void Insert(Point aPoint) {
        path = new ArrayList<>(); //mia arrayList poy periexei olo to path tou dentrou opoy mpike ena point (to ftiaxnei h ChooseSubtree)
        RstarNode N = ChooseSubtree(root, aPoint); //vres to node poy prepei na mpei, anadromika
        if (N.getPoints().size() < M) {
            N.addPoint(aPoint);
            for (int i = 0; i < path.size(); i++) {
                path.get(i).getRectangle().setNewDimensions(aPoint.getLat(), aPoint.getLon());
            }

        } else { //shmainei oti tha exoume OVERFLOW exei hdh M elements mesa
            N.addPoint(aPoint);
            if (path.size() == 1) { //means we split ONLY the root
                ArrayList<RstarNode> temp = ChooseSplitAxis(N);
                RstarNode newRoot = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                newRoot.addChildNode(temp.get(0));
                newRoot.addChildNode(temp.get(1));
                root = newRoot;
                for (int i = 0; i < path.size(); i++) {
                    path.get(i).getRectangle().setNewDimensions(aPoint.getLat(), aPoint.getLon());
                }

            } else {
                for (int j = path.size() - 1; j >= 0; j--) {
                    if (path.get(j).isLeaf()) {//tha mpei mono an einai leafNode!
                        for (int l = 0; l < path.get(j - 1).getChildren().size(); l++) {
                            if (path.get(j - 1).getChildren().get(l) == path.get(j)) {//vres poio paidi einai gia na to kaneis delete!!!!
                                path.get(j - 1).getChildren().remove(l);
                                break;
                            }
                        }
                        ArrayList<RstarNode> temp = ChooseSplitAxis(path.get(j));
                        path.get(j - 1).addChildNode(temp.get(0));
                        path.get(j - 1).addChildNode(temp.get(1));

                    } else { //alliws an den einai leafNode dhladh
                        if (path.get(j).getChildren().size() > M) {
                            //means non leaf node needs overflow treatment
                            if (j - 1 >= 0) {
                                for (int l = 0; l < path.get(j - 1).getChildren().size(); l++) {
                                    if (path.get(j - 1).getChildren().get(l) == path.get(j)) {//vres poio paidi einai gia na to kaneis delete!!!!
                                        path.get(j - 1).getChildren().remove(l);
                                        break;
                                    }
                                }
                                ArrayList<RstarNode> temp = ChooseSplitAxis(path.get(j));
                                path.get(j - 1).addChildNode(temp.get(0));
                                path.get(j - 1).addChildNode(temp.get(1));
                            } else {//means we need new root
                                ArrayList<RstarNode> temp = ChooseSplitAxis(path.get(j));
                                RstarNode newRoot = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                                newRoot.addChildNode(temp.get(0));
                                newRoot.addChildNode(temp.get(1));
                                root = newRoot;
                            }

                        }
                    }
                    for (int i = 0; i < path.size(); i++) {
                        path.get(i).getRectangle().setNewDimensions(aPoint.getLat(), aPoint.getLon());
                    }


                }
            }


        }
    }


    /**
     * Splits a Node to 2 Nodes<br>
     * CSAl For each axis<br>
     * Sort the entries by the lower then by the upper<br>
     * value of their rectangles and determine all<br>
     * distributions as described above Compute S. the<br>
     * sum of all margin-values of the different distributions<br>
     * end<br>
     * CSA2 Choose the axis with the minimum S as split axis<br>
     * CSIl Along the chosen split axIs, choose the<br>
     * distribution with the minimum overlap-value<br>
     * Resolve ties by choosing the distribution with<br>
     * minimum area-value<br>
     *
     * @param Node the node that we want to Split to 2 other Nodes
     * @return an ArrayList that contains the 2 new Nodes
     */
    public ArrayList<RstarNode> ChooseSplitAxis(RstarNode Node) {
        RstarNode N1 = new RstarNode(0, 0, 0, 0);
        RstarNode N2 = new RstarNode(0, 0, 0, 0);
        //UNDO TO HERE
        double marginX = 0;
        double marginY = 0;
        double overlap = Double.MAX_VALUE;

        if (Node.getChildren().size() == 0) { //einai leaf node
            Node.getPoints().sort(new Point.PointsComparatorX());
            for (int k = 1; k < M - 2 * m + 2; k++) {
                RstarNode node1 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                RstarNode node2 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                int _k = 0;
                for (; _k < m - 1 + k; _k++) {
                    node1.addPoint(Node.getPoints().get(_k));
                }
                for (; _k < M + 1; _k++) {
                    node2.addPoint(Node.getPoints().get(_k));
                }


                double temp = node1.getRectangle().getMargin() + node2.getRectangle().getMargin();
                marginX += temp;
            }


            Node.getPoints().sort(new Point.PointsComparatorY());
            for (int k = 1; k < M - 2 * m + 2; k++) {
                RstarNode node1 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                RstarNode node2 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                int _k = 0;
                for (; _k < m - 1 + k; _k++) {
                    node1.addPoint(Node.getPoints().get(_k));
                }
                for (; _k < M + 1; _k++) {
                    node2.addPoint(Node.getPoints().get(_k));
                }
                double temp = node1.getRectangle().getMargin() + node2.getRectangle().getMargin();
                marginY += temp;


            }
            if (marginX < marginY) {
                Node.getPoints().sort(new Point.PointsComparatorX());
                for (int k = 1; k < M - 2 * m + 2; k++) {
                    RstarNode node1 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    RstarNode node2 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    int _k = 0;
                    for (; _k < m - 1 + k; _k++) {
                        node1.addPoint(Node.getPoints().get(_k));
                    }
                    for (; _k < M + 1; _k++) {
                        node2.addPoint(Node.getPoints().get(_k));
                    }


                    double temp = node1.getRectangle().getOverlap(node2.getRectangle());
                    if (temp < overlap) {
                        overlap = temp;
                        N1 = node1;
                        N2 = node2;
                    }
                    if(temp==overlap){
                        double area1= N1.getRectangle().getArea() + N2.getRectangle().getArea();
                        double area2= node1.getRectangle().getArea() + node2.getRectangle().getArea();
                        if(area2<area1){
                            N1=node1;
                            N2=node2;
                        }
                    }
                }
            } else {
                Node.getPoints().sort(new Point.PointsComparatorY());
                for (int k = 1; k < M - 2 * m + 2; k++) {
                    RstarNode node1 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    RstarNode node2 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    int _k = 0;
                    for (; _k < m - 1 + k; _k++) {
                        node1.addPoint(Node.getPoints().get(_k));
                    }
                    for (; _k < M + 1; _k++) {
                        node2.addPoint(Node.getPoints().get(_k));
                    }


                    double temp = node1.getRectangle().getOverlap(node2.getRectangle());
                    if (temp < overlap) {
                        overlap = temp;
                        N1 = node1;
                        N2 = node2;
                    }
                    if(temp==overlap){
                        double area1= N1.getRectangle().getArea() + N2.getRectangle().getArea();
                        double area2= node1.getRectangle().getArea() + node2.getRectangle().getArea();
                        if(area2<area1){
                            N1=node1;
                            N2=node2;
                        }
                    }
                }
            }


        } else {
            Node.getChildren().sort(new RstarNode.RectangleComparatorX());
            for (int k = 1; k < M - 2 * m + 2; k++) {
                RstarNode node1 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                RstarNode node2 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                int _k = 0;
                for (; _k < m - 1 + k; _k++) {
                    node1.addChildNode(Node.getChildren().get(_k));
                }
                for (; _k < M + 1; _k++) {
                    node2.addChildNode(Node.getChildren().get(_k));
                }
                double temp = node1.getRectangle().getMargin() + node2.getRectangle().getMargin();
                marginX += temp;
            }
            Node.getChildren().sort(new RstarNode.RectangleComparatorY());
            for (int k = 1; k < M - 2 * m + 2; k++) {
                RstarNode node1 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                RstarNode node2 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                int _k = 0;
                for (; _k < m - 1 + k; _k++) {
                    node1.addChildNode(Node.getChildren().get(_k));
                }
                for (; _k < M + 1; _k++) {
                    node2.addChildNode(Node.getChildren().get(_k));
                }
                double temp = node1.getRectangle().getMargin() + node2.getRectangle().getMargin();
                marginY += temp;
            }
            if (marginX < marginY) {
                Node.getChildren().sort(new RstarNode.RectangleComparatorX());
                for (int k = 1; k < M - 2 * m + 2; k++) {
                    RstarNode node1 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    RstarNode node2 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    int _k = 0;
                    for (; _k < m - 1 + k; _k++) {
                        node1.addChildNode(Node.getChildren().get(_k));
                    }
                    for (; _k < M + 1; _k++) {
                        node2.addChildNode(Node.getChildren().get(_k));
                    }


                    double temp = node1.getRectangle().getOverlap(node2.getRectangle());
                    if (temp < overlap) {
                        overlap = temp;
                        N1 = node1;
                        N2 = node2;
                    }
                    if(temp==overlap){
                        double area1= N1.getRectangle().getArea() + N2.getRectangle().getArea();
                        double area2= node1.getRectangle().getArea() + node2.getRectangle().getArea();
                        if(area2<area1){
                            N1=node1;
                            N2=node2;
                        }
                    }
                }
            } else {
                Node.getChildren().sort(new RstarNode.RectangleComparatorY());
                for (int k = 1; k < M - 2 * m + 2; k++) {
                    RstarNode node1 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    RstarNode node2 = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    int _k = 0;
                    for (; _k < m - 1 + k; _k++) {
                        node1.addChildNode(Node.getChildren().get(_k));
                    }
                    for (; _k < M + 1; _k++) {
                        node2.addChildNode(Node.getChildren().get(_k));
                    }


                    double temp = node1.getRectangle().getOverlap(node2.getRectangle());
                    if (temp < overlap) {
                        overlap = temp;
                        N1 = node1;
                        N2 = node2;
                    }
                    if(temp==overlap){
                       double area1= N1.getRectangle().getArea() + N2.getRectangle().getArea();
                       double area2= node1.getRectangle().getArea() + node2.getRectangle().getArea();
                       if(area2<area1){
                           N1=node1;
                           N2=node2;
                       }
                    }
                }
            }
        }
        ArrayList<RstarNode> ret = new ArrayList<>(2);
        ret.add(N1);
        ret.add(N2);
        return ret;
    }



    /**
     * Implementation of a circular Range Query<br>
     * Uses a Stack where we push a node if it is a possible choice to hold neighbors<br>
     * and an ArrayList<Point> Points where we insert the neighbors<br>
     *
     * Algorithm<br>
     * push root's children to the Stack<br>
     * pop the first Node from Stack,<br>
     * if it is not a leaf Node, insert it's children to the stack, only if their Rectangles are close enough to the given Point <br>
     * (condition : temp.getChildren().get(i).getRectangle().distance(aPoint) <= distance)<br>
     * if it is a leaf Node, check if it's points are close enough according to the circle of the range query<br>
     * (condition : emp.getChildren().get(i).getPoints().get(k).distance(aPoint) <= distance)<br>
     *
     * WE LOAD EVERY LOCATION FROM THE DATABASE, with this command dt.ReadBlock(Points.get(i).getBlockId(),Points.get(i).getRegisterId())<br>
     * WE LOAD IT AS BLOCK, locations are NOT saved on Main Memory!!!
     *
     * @param aPoint   the Center of the circle
     * @param distance the radius of the circle
     * @return An ArrayList of points that has the result of the range query
     */
    public ArrayList<Location> Range(Point aPoint, double distance) {
        ArrayList<Point> Points = new ArrayList<>();
        LoadData dt=new LoadData();  //In this class there is a method to read a specific register from DataFile
        if (root.getRectangle().distance(aPoint) <= distance) {
            Stack<RstarNode> DFS = new Stack<>();
            DFS.push(root);
            while (!DFS.empty()) {
                RstarNode temp = DFS.pop();
                for (int i = 0; i < temp.getChildren().size(); i++) {
                    if (temp.getChildren().get(i).isLeaf()) {
                        for (int k = 0; k < temp.getChildren().get(i).getPoints().size(); k++) {
                            if (temp.getChildren().get(i).getPoints().get(k).distance(aPoint) <= distance) {
                                Points.add(temp.getChildren().get(i).getPoints().get(k));
                            }
                        }
                    } else {
                        if (temp.getChildren().get(i).getRectangle().distance(aPoint) <= distance) {
                            DFS.push(temp.getChildren().get(i));
                        }
                    }
                }
            }
        }
        ArrayList<Location> locations=new ArrayList<>();
        for(int i=0;i<Points.size();i++){
            Location loc = (dt.ReadBlock(Points.get(i).getBlockId(),Points.get(i).getRegisterId()));
            loc.Manhattan_Distance(aPoint.getLat(),aPoint.getLon());
            locations.add(loc);
        }
        return locations;
    }

    /**
     * Implementation of KNN Algorithm<br>
     * Min heap contains the RstarNodes that we might need to access to search for possible neighbors<br>
     * on the top of minHeap we hold the RstarNode that is closer to the given point<br>
     * MaxHeap contains the k neighbors<br>
     * on the top of maxHeap we hold the most distance neighbor<br>
     *
     * Algorithm<br>
     * First we push on minHeap the children of the root of R*tree<br>
     * Then as soon as minHeap is not Empty repeat:<br>
     * take the RstarNode that it's rectangle is closer to the given point (greedy algorithm, closer neighbor is at closer rectangle) ,this node is at the top of minHeap<br>
     * if this node is not a leaf node, and we have less than k neighbors on maxHeap insert this node on minheap<br>
     * if this node is not a leaf, and we have k objects inside maxHeap, if the distance between the point and the node, is less than the distance between the point and the top of the maxHeap, insert this node on minHeap, else we cut this node from searching<br>
     * if this node is a leaf node, insert it points on maxHeap!<br>
     *
     *  WE LOAD EVERY LOCATION FROM THE DATABASE, with this command dt.ReadBlock(Points.get(i).getBlockId(),Points.get(i).getRegisterId())<br>
     *  WE LOAD IT AS BLOCK, locations are NOT saved on Main Memory!!!
     *
     * @param aPoint we want the nearest neighbors of this point
     * @param k      the number of nearest neighbors that is returned
     * @return an ArrayList that contains the nearest neighbors
     */
    public ArrayList<Location> knn(Point aPoint, int k) {
        ArrayList<Point> neighbors = new ArrayList<>();
        LoadData dt=new LoadData(); //In this class there is a method to read a specific register from DataFile
        MinHeap minHeap = new MinHeap(100);
        MaxHeap maxHeap = new MaxHeap(k);
        for (RstarNode node : root.getChildren()) {
            node.setDistanceFromPoint_forKNN(node.getRectangle().distance(aPoint));
            minHeap.insert(node);
        }
        while (!minHeap.isEmpty()) {
            RstarNode tempNode = minHeap.remove();
            for (RstarNode node : tempNode.getChildren()) {
                //System.out.println(maxHeap.getSize());
                node.setDistanceFromPoint_forKNN(node.getRectangle().distance(aPoint));
                if (!node.isLeaf() && (maxHeap.getSize() < k)) {
                    minHeap.insert(node);
                } else if (!node.isLeaf()) {                   // If maxHeap  is full we already have k neigborngs but we must the nearest
                    if (maxHeap.getMax().getDistanceFromPoint_forKNN() >= node.getDistanceFromPoint_forKNN()) {
                        minHeap.insert(node);
                    }
                } else {
                    ArrayList<Point> temp = node.getPoints();
                    for (Point point : temp) {
                        point.setDistanceFromPoint_forKNN(point.distance(aPoint));
                        maxHeap.insert(point);// We ensured that if maxHeap is empty we replace the max with the new one only if the new one is smaller. The condition is in Insert method.
                    }
                }
            }
        }
        for (int i = 0; i < k; i++) {
            neighbors.add(maxHeap.extractMax());
        }
        ArrayList<Location> locations=new ArrayList<>();
        for(int i=0;i<neighbors.size();i++){
            Location loc=dt.ReadBlock(neighbors.get(i).getBlockId(),neighbors.get(i).getRegisterId());
            loc.setDistance(neighbors.get(i).getDistanceFromPoint_forKNN());
            locations.add(loc);
        }
        return locations;
    }


    /**
     * The depth of the R*Tree
     *
     * @return the maximum level of the R*Tree
     */
    public int getDepth() {
        int level = 1;
        RstarNode node = root;
        while (node.getChildren().size() > 0) {
            node = node.getChildren().get(0);
            level++;
        }
        return level;
    }


    /**
     * This function saves the R*Tree to an IndexFile as Object
     */
    public void WriteFile() {

        try {
            FileOutputStream myWriter = new FileOutputStream("IndexFile.txt");
            ObjectOutputStream objectOut = new ObjectOutputStream(myWriter);
            objectOut.writeObject(root);
            objectOut.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Loads the R*Tree from the IndexFile as Object
     */
    public void ReadFile() {
        try {
            FileInputStream myWriter = new FileInputStream("IndexFile.txt");
            ObjectInputStream objectOut = new ObjectInputStream(myWriter);
            root = (RstarNode) objectOut.readObject();
            objectOut.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    /**
     * Creates the IndexFile that is divided in blocks<br>
     * each block is a RstarNode<br>
     * non leaf Nodes have 36+M*4 Bytes size block<br>
     * leaf Nodes have 36+M*16 Byte size block<br>
     * the result of this function can be used to do knn or range queries if the Index is too large and doesn't fit on Main Memory
     */
    public void createIndexFile() {
        ArrayList<RstarNode> st = new ArrayList<>();
        int k = 1;
        int bid = 1;
        st.add(root);

        String strFilePath = "indexFileInBlocks.txt";
        try {
            FileOutputStream fos = new FileOutputStream(strFilePath);
            DataOutputStream dos = new DataOutputStream(fos);
            while (!st.isEmpty()) {
                ArrayList<RstarNode> childrenNodes = new ArrayList<>(M);
                for (int i = 0; i < st.size(); i++) {
                    for (RstarNode child : st.get(i).getChildren()) {
                        childrenNodes.add(child);

                    }
                }
                for (int i = 0; i < st.size(); i++) {
                    dos.writeInt(bid);
                    bid++;

                    dos.writeDouble(st.get(i).getRectangle().getX1());
                    dos.writeDouble(st.get(i).getRectangle().getX2());
                    dos.writeDouble(st.get(i).getRectangle().getY1());
                    dos.writeDouble(st.get(i).getRectangle().getY2());
                    int Ma = 0;
                    if (st.get(i).isLeaf()) {
                        for (Point point : st.get(i).getPoints()) {
                            dos.writeDouble(point.getLat());
                            dos.writeDouble(point.getLon());
                            Ma++;
                        }
                        for (; Ma < M; Ma++) {
                            dos.writeDouble(-1.0);
                            dos.writeDouble(-1.0);
                        }
                    } else {
                        for (int c = 0; c < st.get(i).getChildren().size(); c++) {
                            dos.writeInt(++k);
                            Ma++;
                        }
                        for (; Ma < M; Ma++) {
                            dos.writeInt(-1);
                        }
                    }
                }

                st = childrenNodes;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    /**
     * PROTOTYPING FOR INDEXFILE<br>
     * Creates an IndexFile as text, so we can test if the code is write or not
     */
    public void createIndexFileASSTRING() {
        ArrayList<RstarNode> st = new ArrayList<>();
        int k = 1;
        int bid = 1;
        st.add(root);
        try {
            FileWriter myWriter = new FileWriter("TESTIndexFileInBlocksTEST.txt");
            while (!st.isEmpty()) {

                ArrayList<RstarNode> childrenNodes = new ArrayList<>(M);
                for (int i = 0; i < st.size(); i++) {
                    for (RstarNode child : st.get(i).getChildren()) {
                        childrenNodes.add(child);

                    }
                }
                for (int i = 0; i < st.size(); i++) {
                    myWriter.write(String.valueOf(bid));
                    bid++;
                    myWriter.write(" ");
                    myWriter.write(String.valueOf(st.get(i).getRectangle().getX1()));
                    myWriter.write(" ");
                    myWriter.write(String.valueOf(st.get(i).getRectangle().getX2()));
                    myWriter.write(" ");
                    myWriter.write(String.valueOf(st.get(i).getRectangle().getY1()));
                    myWriter.write(" ");
                    myWriter.write(String.valueOf(st.get(i).getRectangle().getY2()));
                    myWriter.write(" ");
                    int Ma = 0;
                    myWriter.write("\n");
                    if (st.get(i).isLeaf()) {
                        for (Point point : st.get(i).getPoints()) {
                            myWriter.write(String.valueOf(point.getLat()) + ",");
                            myWriter.write(String.valueOf(point.getLon()));
                            myWriter.write(" ");
                            Ma++;
                        }
                        for (; Ma < M; Ma++) {
                            myWriter.write("n");
                            myWriter.write(" ");
                        }
                        myWriter.write("\n");
                        myWriter.write("\n");
                    } else {
                        for (int c = 0; c < st.get(i).getChildren().size(); c++) {
                            k++;
                            myWriter.write(String.valueOf(k));
                            myWriter.write(" ");
                            Ma++;
                        }
                        for (; Ma < M; Ma++) {
                            myWriter.write("n");
                            myWriter.write(" ");
                        }
                        myWriter.write("\n");
                        myWriter.write("\n");
                    }

                }

                st = childrenNodes;
            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    /**
     * Creates a txt file with all the Rectangles of the R*Tree<br>
     * Creates a txt file with all points that are inside the R*Tree<br>
     * The results of this function are used for plots with a python script
     * @throws IOException if we can not access the files
     */
    public void createPythonFiles() throws IOException {
        File myObj = new File("python plot/outData.txt");
        myObj.delete();
        File myOb = new File("python plot/points.txt");
        myOb.delete();
        try (FileWriter fw = new FileWriter("python plot/outData.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            Stack<RstarNode> stack = new Stack<>();
            stack.push(root);
            while (!stack.empty()) {
                if (stack.peek().isLeaf()) {
                    out.println(stack.pop().getRectangle().getXXYY());
                } else {
                    RstarNode temp = stack.pop();
                    out.println(temp.getRectangle().getXXYY());
                    for (int i = 0; i < temp.getChildren().size(); i++) {
                        stack.push(temp.getChildren().get(i));
                    }
                }
            }
        }
        LoadData ld = new LoadData();
        ArrayList<Location> locations = new ArrayList<>();
        locations = ld.ReadClass();
        try (FileWriter fw = new FileWriter("python plot/points.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            for (Location location : locations) {
                out.println(location.getLat() + "," + location.getLon());
            }
        }
    }


}
