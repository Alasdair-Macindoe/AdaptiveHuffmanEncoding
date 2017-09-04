package vitterImpl;

import exceptions.IntegerBelowZeroException;

/**
 * The node object required for a VTree implementation
 */
public class Node {
    /*
     * These both are private to ensure the invariant is maintained.
     */
    private int weight = 0;
    private String value = "";

    /*
     * Nodes to give the tree structure
     * These are public for ease of updating
     */
    public Node left;
    public Node right;
    public Node parent;

    /**
     * Creates a Node with predetermined left and right nodes
     * @param left left node
     * @param right right node
     */
    public Node(Node left, Node right){
        this.left = left;
        this.right = right;

        //sets the correct weight
        if (left != null){
            weight += left.weight;
        }
        if (right != null){
            weight += right.weight;
        }
    }

    /**
     * Adds two nodes an updates their parent element
     * @param parent the parent of these nodes
     * @param left the left node
     * @param right the right node
     */
    public Node(Node parent, Node left, Node right){
        this(left, right);
        this.parent = parent;
    }

    /**
     * Creates a new node with the character c, with the weight set to 0 as a default
     * Allows it to be used for arbitrary weightings
     * @param c
     */
    public Node(String c){
        value = c;
        weight = 0;
        parent = null;
    }

    /**
     * Default no-args constructor
     */
    public Node(){}

    /**
     * @return the weight assigned to this node
     */
    public int getWeight(){
        return weight;
    }

    /**
     * Increments the weight of this Node by 1.
     */
    public void incrementWeight(){
        weight++;
    }

    /**
     * Updates the weighting of this node, and maintains the invariant in doing so
     * @param w the new weight to be assigned to this node
     * @throws IntegerBelowZeroException when the invariant would not be maintained
     */
    public void setWeight(int w) throws IntegerBelowZeroException{
        if (w < 0) throw new IntegerBelowZeroException();
        weight = w;
    }


    /**
     * @return the value of this node
     */
    public String getValue(){
        return value;
    }

    /**
     * Updates the value of this node
     * @param c the new value this node should contain
     */
    public void setValue(String c){
        if (c == null) c = "";
        value = c;
    }


    public boolean isLeaf(){
        return ( left == null && right == null);
    }

}
