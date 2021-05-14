// Java program to implement Max Heap 
public class MaxHeap {
    private Point[] Heap;
    private int size;
    private int maxsize;

    // Constructor to initialize an 
    // empty max heap with given maximum 
    // capacity. 
    public MaxHeap(int maxsize) {
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new Point[this.maxsize + 1];
        Point aPoint =  new Point(Double.MIN_VALUE, Double.MIN_VALUE);
        aPoint.setDistanceFromPoint_forKNN(Double.MAX_VALUE);
        Heap[0] = aPoint;

    }

    // Returns position of parent 
    private int parent(int pos) {
        return pos / 2;
    }

    // Below two functions return left and 
    // right children. 
    private int leftChild(int pos) {
        return (2 * pos);
    }

    private int rightChild(int pos) {
        return (2 * pos) + 1;
    }

    // Returns true of given node is leaf 
    private boolean isLeaf(int pos) {
        if(leftChild(pos)>=size) {
            return true;
        }
        return false;
    }

    private void swap(int fpos, int spos) {
        Point tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }

    // A recursive function to max heapify the given 
    // subtree. This function assumes that the left and 
    // right subtrees are already heapified, we only need 
    // to fix the root. 
    private void maxHeapify(int pos) {
        if (isLeaf(pos))
            return;

        if (Heap[pos].getDistanceFromPoint_forKNN() < Heap[leftChild(pos)].getDistanceFromPoint_forKNN() ||
                Heap[pos].getDistanceFromPoint_forKNN() < Heap[rightChild(pos)].getDistanceFromPoint_forKNN()) {

            if (Heap[leftChild(pos)].getDistanceFromPoint_forKNN() > Heap[rightChild(pos)].getDistanceFromPoint_forKNN()) {
                swap(pos, leftChild(pos));
                maxHeapify(leftChild(pos));
            } else {
                swap(pos, rightChild(pos));
                maxHeapify(rightChild(pos));
            }
        }
    }

    // Inserts a new element to max heap 
    public void insert(Point element) {

        if (size==maxsize){      // If the new Children is closer than the farthest one and Heap is full we remove max and and the new one

            if(element.getDistanceFromPoint_forKNN()<=getMax().getDistanceFromPoint_forKNN()){
                extractMax();
            }
            else{
                return ;
            }
        }
        Heap[++size] = element;
        // Traverse up and fix violated property
        int current = size;
        while (Heap[current].getDistanceFromPoint_forKNN() > Heap[parent(current)].getDistanceFromPoint_forKNN() ) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    public void print() {
        for (int i = 1; i <= size / 2; i++) {
            System.out.print(" PARENT : " + Heap[i].getDistanceFromPoint_forKNN() + " LEFT CHILD : " +
                    Heap[2 * i].getDistanceFromPoint_forKNN() + " RIGHT CHILD :" + Heap[2 * i + 1].getDistanceFromPoint_forKNN());
            System.out.println();
        }
    }

    // Remove an element from max heap 
    public Point extractMax() {
        Point popped = Heap[1];
        Heap[1] = Heap[size];
        maxHeapify(1);
        size--;
        return popped;
    }

    public boolean isEmpty(){
        if(size==0)return true;
        return  false;
    }

    public int getSize(){
        return size;
    }
    // Returns the max value without extracting it
    public Point getMax(){
        return Heap[1];
    }
}