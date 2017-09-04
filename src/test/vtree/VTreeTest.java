package test.vtree;

import org.junit.Before;
import org.junit.Test;
import org.testng.annotations.BeforeTest;
import test.node.NodeTest;
import vitterImpl.Node;
import vitterImpl.VTree;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * The test suite for VTree implementation, using JUnit 4.
 */
public class VTreeTest{
    VTree t;

    @Before
    public void setUp() throws IOException{
        t = new VTree();
    }

    @Test
    public void addNormalToEmptyTree(){
        String charToAdd = "a";
        t.add(charToAdd);
        //Test to ensure both the left and right node are correct now
        //still counts as one "thing".
        //Should look like:
        //  head
        //NYT   a
        assertEquals(0, t.head.left.getWeight());
        assertEquals(charToAdd, t.head.right.getValue());
        assertEquals(1, t.head.right.getWeight());
        assertEquals(1, t.head.getWeight());
    }

    @Test
    public void addUniqueToEmptyTreeTwiceAndThrice(){
        String charToAdd = "a";
        t.add(charToAdd);
        assertEquals(1, t.head.right.getWeight());
        assertEquals(1, t.head.getWeight());
        t.add(charToAdd);
        assertEquals(2, t.head.right.getWeight());
        assertEquals(2, t.head.getWeight());
        t.add(charToAdd);
        assertEquals(3, t.head.getWeight());
        assertEquals(3, t.head.right.getWeight());
    }

    @Test
    public void addTwoUniqueCharacters(){
        t.add("a");
        t.add("b");
        Node n = t.head;
        assertEquals(2, n.getWeight());
        assertEquals(1, n.left.getWeight());
        assertEquals(1, n.right.getWeight());
        n = t.head.right;
        assertEquals(1, n.right.getWeight());
        assertEquals("b", n.right.getValue());
        assertEquals(0, n.left.getWeight());
    }

    /*This example was taken from Viiter 1987*/
    @Test
    public void addingThreeUnique(){
        t.add("a");
        t.add("b");
        t.add("c");

        Node n = t.head;
        assertEquals(3, n.getWeight());
        n = t.head.right;
        assertEquals(2, n.getWeight());
        assertEquals(1, n.right.getWeight());
        assertEquals(1, n.left.getWeight());
        n = t.head.left;
        assertEquals(1, n.getWeight());
        assertEquals(1, n.right.getWeight());
        assertEquals(0, n.left.getWeight());
        assertEquals("c", n.right.getValue());

        //Ensure list is ordered correctly also
        Node[] list = new Node[7];
        list = t.getList().toArray(list);
        assertEquals(0, list[0].getWeight());
        assertEquals(1, list[1].getWeight());
        assertEquals(1, list[2].getWeight());
        assertEquals(1, list[3].getWeight());
        assertEquals(1, list[4].getWeight());
        assertEquals(2, list[5].getWeight());
        assertEquals(3, list[6].getWeight());

    }

    //This example came from Dan Hirschberg at UC Irvine.
    /**
     * Attempt to add the String "aa bbb c"
     */
    @Test
    public void aabbbcWithSpacesTest(){
        t.add("a");
        t.add("a");
        t.add(" ");
        t.add("b");
        t.add("b");
        t.add("b");
        t.add(" ");
        t.add("c");

        Node n = t.head;
        assertEquals(8, n.getWeight());
        n = t.head.right;
        assertEquals(5, n.getWeight());
        assertEquals(3, n.right.getWeight());
        assertEquals("b", n.right.getValue());
        assertEquals(2, n.left.getWeight());
        assertEquals("a", n.left.getValue());
        n = t.head.left;
        assertEquals(3, n.getWeight());
        assertEquals(2, n.right.getWeight());
        assertEquals(' ', n.right.getValue());
        n = t.head.left.left;
        assertEquals(1, n.getWeight());
        assertEquals(1, n.right.getWeight());
        assertEquals("c", n.right.getValue());
        assertEquals(0, n.left.getWeight());
    }

    @Test
    public void emulateExampleInSpec(){
        for (int i =0; i<10; i++){
            t.add("e");
        }
        t.add("d");
        t.add("d");
        t.add("c");
        t.add("c");
        t.add("b");
        t.add("b");

        Node n = t.head;
        assertEquals(16, t.head.getWeight());
        assertEquals(10, n.right.getWeight());
        assertEquals("e", n.right.getValue());
        n = t.head.left;
        assertEquals(6, n.getWeight());
        n = t.head.left.right;
        assertEquals(4, n.getWeight());
        assertEquals(2, n.left.getWeight());
        assertEquals(2, n.right.getWeight());
        n = t.head.left.left;
        assertEquals(2, n.getWeight());
        assertEquals("d", n.getValue());
    }

    @Test
    public void specExampleExtended(){
        for (int i =0; i<10; i++){
            t.add("e");
        }
        t.add("d");
        t.add("d");
        t.add("c");
        t.add("c");
        t.add("b");
        t.add("b");

        //add the 'a's
        t.add("a");
        //only test the difference
        Node n = t.head.left.left.left;
        assertEquals(1, n.right.getWeight());
        assertEquals(0, n.left.getWeight());

        //next a
        t.add("a");
        assertEquals(2, n.right.getWeight());
        assertEquals(0, n.left.getWeight());

        //next a
        t.add("a");
        assertEquals(2, n.right.getWeight());
        assertEquals(0, n.left.getWeight());
        assertEquals(3, t.head.left.right.right.getWeight());
        assertEquals("a", t.head.left.right.right.getValue());

        //next a - dramatic swaps
        t.add("a");
        t.add("a");
        assertEquals(10, t.head.left.getWeight());
        assertEquals(11, t.head.right.getWeight());
        assertEquals(5, t.head.right.left.getWeight());
        assertEquals(6, t.head.right.right.getWeight());
    }

    /* Note: This example comes from Sayood, 2012.
    */
    @Test
    public void aardvTest(){
        Node n = t.head;

        t.add("a");
        assertEquals(1, n.getWeight());
        assertEquals(1, n.right.getWeight());
        assertEquals(0, n.left.getWeight());
        assertEquals("a", n.right.getValue());

        t.add("a");
        assertEquals(2, n.getWeight());
        assertEquals(2, n.right.getWeight());
        assertEquals(0, n.left.getWeight());

        t.add("r");
        assertEquals(3, n.getWeight());
        assertEquals(2, n.right.getWeight());
        assertEquals(1, n.left.getWeight());
        assertEquals(1, n.left.getWeight());
        assertEquals(1, n.left.right.getWeight());
        assertEquals(0, n.left.left.getWeight());
        assertEquals("r", n.left.right.getValue());

        t.add("d");
        assertEquals(4, n.getWeight());
        assertEquals(2, n.right.getWeight());
        assertEquals(2, n.left.getWeight());
        assertEquals(2, n.left.getWeight());
        assertEquals(1, n.left.right.getWeight());
        assertEquals(1, n.left.left.getWeight());
        assertEquals(1, n.left.left.right.getWeight());
        assertEquals("d", n.left.left.right.getValue());

        //Ensure list is updating correctly
        Node[] list = new Node[7];
        list = t.getList().toArray(list);
        assertEquals(0, list[0].getWeight());
        assertEquals(1, list[1].getWeight());
        assertEquals(1, list[2].getWeight());
        assertEquals(1, list[3].getWeight());
        assertEquals(2, list[4].getWeight());
        assertEquals(2, list[5].getWeight());
        assertEquals(4, list[6].getWeight());


        t.add("v");
        assertEquals(5,n.getWeight());
        assertEquals(2, n.left.getWeight());
        assertEquals("a", n.left.getValue());
        n = n.right;
        assertEquals(3, n.getWeight());
        assertEquals(1, n.left.getWeight());
        assertEquals("r",n.left.getValue());
        n = n.right;
        assertEquals(2, n.getWeight());
        assertEquals(1, n.right.getWeight());
        assertEquals("d", n.right.getValue());
        n = n.left;
        assertEquals(1, n.getWeight());
        assertEquals(1, n.right.getWeight());
        assertEquals("v", n.right.getValue());
        assertEquals(0, n.left.getWeight());

        //Ensure list has updated correctly
        list = new Node[9];
        list = t.getList().toArray(list);
        assertEquals(0, list[0].getWeight());
        assertEquals(1, list[1].getWeight());
        assertEquals(1, list[2].getWeight());
        assertEquals(1, list[3].getWeight());
        assertEquals(1, list[4].getWeight());
        assertEquals(2, list[5].getWeight());
        assertEquals(2, list[6].getWeight());
        assertEquals(3, list[7].getWeight());
        assertEquals(5, list[8].getWeight());
    }

    @Test
    public void testEncodeAndDecode() throws Exception{
        String m1 = "Hello World!";
        String m2 = "A far more complex String\nWell I should hope so!";

        String e1 = t.encode(m1);
        t = new VTree();
        String d1 = t.decode(e1);
        assertEquals(d1,m1);

        t = new VTree();
        String e2 = t.encode(m2);
        t = new VTree();
        String d2 = t.decode(e2);
        assertEquals(d2, m2);
    }

    @Test
    public void testEncodeAndDecodeForDoubleSymbols() throws Exception{
        String m1 = "Hello World!";
        t = new VTree(2);
        String e1 = t.encode(m1);
        System.out.println("He: "+e1);
        t = new VTree(2);
        System.out.println("Length: "+e1.length());
        String d1 = t.decode(e1);
        assertEquals(d1,m1);
    }


}
