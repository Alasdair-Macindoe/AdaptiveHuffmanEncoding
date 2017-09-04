package test.node;

import org.junit.Before;
import org.junit.Test;
import vitterImpl.Node;

import static org.junit.Assert.*;

public class NodeTest {
    Node h;
    static final String C1 = "a";
    static final int W1 = 2;
    static final String C2 = "b";
    static final int W2 = 1;

    @Before
    public void setUp() throws Exception {
        h = new Node();
        h.setWeight(W1+W2);
        h.right = new Node();
        h.right.setValue(C1);
        h.right.setWeight(W1);
        h.left = new Node();
        h.left.setValue(C2);
        h.left.setWeight(W2);
    }

    @Test
    public void establishCorrectly(){
        assertEquals(W1+W2, h.getWeight());
        assertEquals(C1, h.right.getValue());
        assertEquals(W1, h.right.getWeight());
        assertEquals(C2, h.left.getValue());
        assertEquals(W2, h.left.getWeight());
    }

    @Test
    public void correctLeafDetection(){
        assertFalse(h.isLeaf());
        assertTrue(h.right.isLeaf());
        assertTrue(h.left.isLeaf());
    }

}
