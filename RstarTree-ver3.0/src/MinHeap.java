
// Java implementation of Min Heap
public class MinHeap {
    private RstarNode[] Heap;
    private int size;
    private int maxsize;

    private static final int FRONT = 1;

    public MinHeap(int maxsize)
    {
        this.maxsize = maxsize;
        this.size = 0;
        Heap = new RstarNode[this.maxsize + 1];
        RstarNode aNode = new RstarNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
        aNode.setDistanceFromPoint_forKNN(Double.MIN_VALUE);
        Heap[0] = aNode;
    }

    // Function to return the position of
    // the parent for the node currently
    // at pos
    private int parent(int pos)
    {
        return pos / 2;
    }

    // Function to return the position of the
    // left child for the node currently at pos
    private int leftChild(int pos)
    {
        return (2 * pos);
    }

    // Function to return the position of
    // the right child for the node currently
    // at pos
    private int rightChild(int pos)
    {
        return (2 * pos) + 1;
    }

    // Function that returns true if the passed
    // node is a leaf node
    private boolean isLeaf(int pos) {
        if(leftChild(pos)>=size) {
            return true;
        }

         // Scenario not covered: "A node might not be a leaf but only have a left child."

        return false;
    }

    // Function to swap two nodes of the heap
    private void swap(int fpos, int spos)
    {
        RstarNode tmp;
        tmp = Heap[fpos];
        Heap[fpos] = Heap[spos];
        Heap[spos] = tmp;
    }

    // Function to heapify the node at pos
    private void minHeapify(int pos)
    {

        // If the node is a non-leaf node and greater
        // than any of its child
        if (!isLeaf(pos)) {
            if (Heap[pos].getDistanceFromPoint_forKNN() > Heap[leftChild(pos)].getDistanceFromPoint_forKNN()
                    || Heap[pos].getDistanceFromPoint_forKNN() > Heap[rightChild(pos)].getDistanceFromPoint_forKNN()) {

                // Swap with the left child and heapify
                // the left child
                if (Heap[leftChild(pos)].getDistanceFromPoint_forKNN() < Heap[rightChild(pos)].getDistanceFromPoint_forKNN()) {
                    swap(pos, leftChild(pos));
                    minHeapify(leftChild(pos));
                }

                // Swap with the right child and heapify
                // the right child
                else {
                    swap(pos, rightChild(pos));
                    minHeapify(rightChild(pos));
                }
            }
        }
    }

    // Function to insert a node into the heap
    public void insert(RstarNode element)
    {
        if (size >= maxsize) {
            return;
        }
        Heap[++size] = element;
        int current = size;

        while (Heap[current].getDistanceFromPoint_forKNN() < Heap[parent(current)].getDistanceFromPoint_forKNN()) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    // Function to print the contents of the heap
    public void print()
    {
        for (int i = 1; i <= size / 2; i++) {
            System.out.print(" PARENT : " + Heap[i].getDistanceFromPoint_forKNN()
                    + " LEFT CHILD : " + Heap[2 * i].getDistanceFromPoint_forKNN()
                    + " RIGHT CHILD :" + Heap[2 * i + 1].getDistanceFromPoint_forKNN());
            System.out.println();
        }
    }

    // Function to build the min heap using
    // the minHeapify
    public void minHeap()
    {
        for (int pos = (size / 2); pos >= 1; pos--) {
            minHeapify(pos);
        }
    }

    // Function to remove and return the minimum
    // element from the heap
    public RstarNode remove()
    {
        RstarNode popped = Heap[FRONT];
        Heap[FRONT] = Heap[size--];
        minHeapify(FRONT);
        return popped;
    }

    public boolean isEmpty(){
        if(size==0){
            return true;
        }
        else{
            return false;
        }
    }

}
