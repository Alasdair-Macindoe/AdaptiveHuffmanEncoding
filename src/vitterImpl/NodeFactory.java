package vitterImpl;

import exceptions.IntegerBelowZeroException;

/**
 * A factory to create certain pre-defined nodes.
 */
public abstract class NodeFactory {

    /**
     * This will create a new node for a VTree with the right node set to a specific value, and the left
     * node set to the NYT node. This maintains the invariant.
     * @param c the symbol for the new internal, right node
     */
    static void unseenCharacterNode(Node n, String c){
        n.left = getNYTNode();
        n.right = new Node(c);
        n.left.parent = n;
        n.right.parent = n;
    }

    static Node getNYTNode(){
        return new Node();
    }
}
