package vitterImpl;

import exceptions.InvalidSequenceException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * This is an implementation of the tree produced by Vitter's algorithm.
 * The pseudocode was given by Vitter, 1987 (and adapted to fit an OO approach better)
 * and help with implementation from Sayood, 2012. See report for more.
 *
 * In this program I refer to the tree produced by Vitter's as a VTree.
 */
public class VTree{

    /* Global Variables  and constants */
    //The character we can consider our "first" synbol eg: position 0 in the alphabet
    private final char START_SYMBOL;
    //The size of the alphabet eg: all possible permutations of all the characters
    private final int ALPHA_SIZE;
    //The amount of unique characters we can choose from. Must be contiguous from START_SYMBOL. We can consider our
    //alphabet to be START_SYMBOL to START_SYMBOL + ALPHA_CHARACTERS
    private final int ALPHA_CHARACTERS;
    //EXPONENT and REMAINDER are explained in depth below. They are the maximum number of bits read at once.
    private final int EXPONENT;
    private final int REMAINDER;
    /*This is the number of characters be should consider at once.
     * eg: For a string "ab" if NUM_SYMBOLS = 1 we consider them "a" and "b", but
     * for NUM_SYMBOLS = 2 we have only "ab"
     */
    private final int NUM_SYMBOLS;
    //Used to write the output to file as a String representation of the binary.
    private FileWriter output;
    //This is the node is that current the NYT node in the tree
    private Node currentNYT = NodeFactory.getNYTNode();
    //This is the first node in the tree,
    public Node head = currentNYT;
    /*
     * Used in order to maintain the position a node is in the tree,
     * Can be updated dynamically.
     * This is initalised with the head element already added.
     * Note that new nodes get added to the start of the Linked List.
     */
    private LinkedList<Node> list = new LinkedList<Node>(){{ add(head); }};



    /* Constructors */
    //No-args constructor
    public VTree() throws IOException{
        this(1);
    }

    //Work for more than 1 symbol at once
    public VTree(int NUM_SYMBOLS, char START_SYMBOL, int ALPHA_CHARACTERS, String output) throws IOException{
        this.NUM_SYMBOLS = NUM_SYMBOLS;
        this.START_SYMBOL = START_SYMBOL;
        this.ALPHA_CHARACTERS = ALPHA_CHARACTERS;
        ALPHA_SIZE = (int)Math.pow(ALPHA_CHARACTERS, NUM_SYMBOLS);
        EXPONENT = calculateExponent(ALPHA_SIZE);
        REMAINDER = calculateRemainder(ALPHA_SIZE, EXPONENT);
        currentNYT.parent = head;
        head.parent = null; //This **MUST** be after we assign the parent to current NYT.
        this.output = new FileWriter(output);
    }

    public VTree(int NUM_SYMBOLS, char START_SYMBOL, int ALPHA_CHARACTERS) throws IOException{
        this(NUM_SYMBOLS, START_SYMBOL, ALPHA_CHARACTERS, "output.txt");
    }

    public VTree(int NUM_SYMBOLS, String output) throws IOException{
        this(NUM_SYMBOLS, '\t', 117, output); //Includes all "useful" ASCII according to http://www.asciitable.com/
    }

    public VTree(int NUM_SYMBOLS) throws IOException{
        this(NUM_SYMBOLS, "output.txt");
    }

    /* Methods */
    /* Calculations */
    /*
     * The following two methods calculate their values such  that:
     * 2^EXPONENT + REMAINDER = ALPHA_SIZE and EXPONENT >= 0 and 0 <= REMAINDER <= 2^EXPONENT.
     * This formula was copied from Sayood, 2012. See the report for more information.
     * Implementation was not copied from Sayood, 2012.
     */
    /**
     * Calculates the exponent required for an integer obey the rules set out in Sayood, 2012.
     * @param x the integer an exponent should be calculated for
     * @return the exponent or 0 if x is less than 0.
     */
    private int calculateExponent(int x){
        if (x<0) return 0;
        double val = Math.log(x)/Math.log(2);
        return (int)Math.floor(val);
    }
    /**
     * Calculates the difference between a difference x and 2^exponent.
     * These rules are were out in Sayood, 2012.
     * @param x the integer (larger)
     * @param exponent the exponent
     * @return the difference between the two
     */
    private int calculateRemainder(int x, int exponent){
        if (x<0) return 0;
        return (int)(x - Math.pow(2,exponent));
    }


    /**
     * Detects the symbol from a given symbol number
     * @param value the symbol number
     * @param noSymbols the number of characters in each symbol
     * @return the symbol detected
     */
    private String detectSymbol(int value, int noSymbols){
        StringBuilder sb = new StringBuilder();
        for (int i=noSymbols-1; i >= 0; i--){
            int loops = 0;
            while (value >= Math.pow(ALPHA_CHARACTERS, i)){
                loops++;
                value -= Math.pow(ALPHA_CHARACTERS, i);
            }
            //Special case. Treat new line as /n append that, do not actually take a new line.
            //If this wasn't here is will take a new line. Odd.
            if (loops == 0){
                sb.append(START_SYMBOL);
            }else {
                sb.append((char) (START_SYMBOL + loops));
            }
        }
        return sb.toString();
    }

    /**
     * Detects the number in the alphabet that this symbol is
     * @param s the string representation of the symbol
     * @return the position in the alphabet
     */
    private int detectSymbolNumber(String s){
        int number = 0;
        for (int i =0; i<s.length(); i++){
            //In effect this is arthm mod ALPHA_SIZE
            //Recall we are counting from 0
            number += Math.pow(ALPHA_CHARACTERS, s.length() - i - 1)*((int)s.charAt(i)-(int)START_SYMBOL);
        }
        return number;
    }
    /**
     * Given a string representing a binary input, calculate its numerical representation
     * @param s the binary string
     * @return the number represented by this string
     */
    public int reverseBinary(String s){
        int val = 0;
        for (int i = 0; i<s.length(); i++){
            if (s.charAt(i) == '1'){
                //Observe that the last character is the least significant
                val += (int)Math.pow(2,s.length() - 1 - i);
            }
        }
        return val;
    }

    /**
     * Gets the binary representation for a number
     * @param v the number
     * @param bits the amount of bits required to store this number
     * @return a String representing the binary of this number
     */
    private String getBinary(int v, int bits){
        StringBuilder sb = new StringBuilder();
        for (int i =0; i<bits; i++){
            if ((v&1) == 1){
                sb.append('1');
            }else{
                sb.append('0');
            }
            v = v>>>1;
        }
        return sb.reverse().toString();
    }

    /**
     * Gets the route to a node from root. To traverse from root "1" = go right, "0" = go left.
     * Should **NOT** be null, or not exist in the tree.
     * @param n the node to traverse a route to
     * @return the route to this node from root
     */
    private String getEncoding(Node n){
        if (n == head) return "0"; //special case
        StringBuilder sb = new StringBuilder();
        while (n != head){
            if (isOnRight(n)){
                sb.append("1");
            }else{
                sb.append("0");
            }
            n = n.parent;
        }
        return sb.reverse().toString();
    }


    /* Getters, Setters & Checkers */

    /**
     * Retrieves the linked list currently being used to maintain the invariant (please do not update this), otherwise
     * unknown behaviour will occur.
     * @return the linked list holding the index of each node
     */
    public LinkedList<Node> getList(){
        return list;
    }

    /**
     * The block deeper into the LinkedList with a different weight or different "type" (eg leaf or internal).
     * @return the next block or the head node if no such block exists
     */
    public Node getNextBlock(Node n){
        Node currentNextBlock = null;
        for (Node b: list){
            if (b.getWeight() == n.getWeight() && b != head && b!=n.parent) currentNextBlock = b;
        }
        return currentNextBlock;
    }

    /**
     * Returns whether a node is on the right side of its parent
     * @param n the node to be checked
     * @return true the node is on the right side of its parent, false otherwise (and false if null)
     */
    private boolean isOnRight(Node n){
        if (n == null) return false;
        return n.parent.right == n;
    }

    /**
     * Determines if a node is NYT
     * @param n the node to be checked
     * @return true if NYT and false otherwise
     */
    public boolean isNYTNode(Node n){
        return n.getWeight()==0;
    }

    /**
     * Finds the correct node in the VTree for a specific symbol
     * @param c the symbol to be found
     * @return the node for this character, or the NYT node if unfound
     */
    private Node findNode(String c){
        for (Node n: list){
            if (n.getValue().equals(c)) return n;
        }
        return currentNYT;
    }

    /**
     * Adds a character c to a node which is attached to the right of a now internal node, whilst maintaining invariant,
     * which includes updating the current NYT node
     * @param n the NYT node
     * @param s the symbol to be added
     */
    private void addToZeroNode(Node n, String s){
        NodeFactory.unseenCharacterNode(n, s);
        addParentAndChildrenToList(n);
        currentNYT = n.left;
    }

    /**
     * Adds a node and its children to the list of nodes.
     * Maintains the invariant, as such: 0 - left node, 1 - right node, 2 - parent node
     * @param n the parent node
     */
    private void addParentAndChildrenToList(Node n){
        list.addFirst(n.right);
        list.addFirst(n.left);
    }


    /* Maintenance */

    /**
     * Given two nodes it swaps their positions. Does not change their memory locations or logical addresses.
     * @param a a node to be swapped
     * @param b the other node to be swapped
     */
    private void swap(Node a, Node b){
        if (a==b) return;
        boolean aOnRight = a.parent.right == a;
        boolean bOnRight = b.parent.right == b;
        Node bParent = b.parent;
        //Update their parent nodes
        if (aOnRight){
            a.parent.right = b;
        }else{
            a.parent.left = b;
        }
        if (bOnRight){
            b.parent.right = a;
        }else{
            b.parent.left = a;
        }
        //Update so they have the correct parents
        b.parent = a.parent;
        a.parent = bParent;
    }

    /**
     * Resorts the list full the root node to maintain the invariant
     */
    private void resortList(){
        list = new LinkedList<>();
        list.add(head);
        sort(head);
    }

    /**
     * Sorts the list to maintain the ordering of the tree and the invariant
     * @param n the node to be sorted from
     */
    private void sort(Node n){
        if (n.right != null){
            list.addFirst(n.right);
        }
        if (n.left != null){
            list.addFirst(n.left);
        }
        if (n.right != null){
            sort(n.right);
        }
        if (n.left != null){
            sort(n.left);
        }
    }


    /* Encoding, Decoding and Tree creation */

    /**
     * Adds a string to the tree. Does not encode.
     * @param s the String to be added
     */
    public void add(String s){
        if (s == null) return;
        add(findNode(s),s);
    }

    /**
     * Adds a String to a specific node.
     * @param n the node representing this String. **MUST** be the correct node.
     * @param s the String itself.
     */
    private void add(Node n, String s) {
        //Manage unseen characters
        if (isNYTNode(n)){
            addToZeroNode(n,s); //note that n is the parent of the newly created symbol
            n.right.incrementWeight();
            //Special case
            if (n == head) {
                n.incrementWeight();
            }else { //normal case
                adapt(n);
            }
        }else{ //We have seen this value before
            adapt(n);
        }
    }

    /**
     * Internal adaption method. Updates the tree to maintain the invariant.
     * @param n node which is the frame of reference for this adaption.
     */
    private void adapt(Node n){
        if (n == null) return;
        Node block = getNextBlock(n);
        if (block != null && block != head && n.parent != null){
            swap(block,n);
            resortList(); //we only need to resort list upon a swap
        }
        n.incrementWeight();
        adapt(n.parent);
    }

    /**
     * Generate the encoding for a sequence of characters (which will be converted to a String) read in from a File.
     * Output is written to a file determined by the constructor.
     * @param r the FileReader with the file open.
     * @throws IOException when there is difficulty reading from the file or writing to file
     * (eg file has already been closed)
     */
    public void encode(FileReader r) throws IOException{
        BufferedReader br = new BufferedReader(r);
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();
        while (line !=null){
            sb.append(line);
            sb.append("\n");
            line = br.readLine();
        }
        sb.deleteCharAt(sb.length() - 1); //delete the last \n
        output.append(encode(sb.toString()));
        output.close();
        br.close();
    }

    /**
     * Generates the encoding for a specific String of characters.
     * @param s the String to be encoded.
     * @return the encoding generated by this data structure
     */
    public String encode(String s){
        //Add new lines to make it a length we can manage. This does affect the encoding.
        while ((s.length() % NUM_SYMBOLS) != 0){
            s = s +"\n";
        }
        StringBuilder sb = new StringBuilder();
        for (int i =0; i<s.length(); i += NUM_SYMBOLS){
            String input = s.substring(i, i+ NUM_SYMBOLS);
            sb.append(encode(input, NUM_SYMBOLS));
        }
        return sb.toString();
    }

    /**
     * Encodes a string with a specific number of characters.
     * Internal method. When in doubt use String encode(String);
     * @param c The string to be encoded
     * @param symbols the number of symbols that form each symbol.
     * @return the encoding as a String
     */
    private String encode(String c, int symbols){
        Node n = findNode(c);
        int pos = detectSymbolNumber(c);
        String encoding;
        if (isNYTNode(n)) {
            if (pos > 2 * REMAINDER - 1) { //We count from 0 in this program
                pos -= REMAINDER;
                encoding = (getEncoding(n) + getBinary(pos, EXPONENT));
            } else {
                encoding = (getEncoding(n) + getBinary(pos, EXPONENT + 1));
            }
            add(n,c);
        }else{
            encoding = getEncoding(n);
            add(n,c);
        }
        return encoding;

    }

    /**
     * Decodes a specific code generated by this tree. Should be as a String representing the binary. If not a String
     * can be created using the createBinary method. Output is written to a file decided in the constructor.
     * @param r the FileReader with the open file
     * @throws IOException when there is difficulty reading the file or writing the output
     * @throws InvalidSequenceException when the sequence given does not form a valid tree
     */
    public void decode(FileReader r) throws IOException, InvalidSequenceException{
        BufferedReader br = new BufferedReader(r);
        output.append(decode(br.readLine(), NUM_SYMBOLS));
        output.close();
        br.close();
    }

    /**
     * Decodes a specific code generated by this tree. Should be as a String representing the binary. If not a String
     * can be created using the createBinary method.
     * @param s the String to be decoded
     * @throws InvalidSequenceException when the sequence given does not form a valid tree
     */
    public String decode(String s) throws InvalidSequenceException{
        return decode(s, NUM_SYMBOLS);
    }

    /**
     * Internal method for decoding Strings.
     * @param s The String to be decoded
     * @param numberOfSymbols the number of characters that form each synbol
     * @return the decoded version of this String
     * @throws InvalidSequenceException when the given sequence does not form a valid tree
     */
    private String decode(String s, int numberOfSymbols) throws InvalidSequenceException {
        StringBuilder decoding = new StringBuilder();
        int currentChar = 0;
        //sanity check
        if (s.length() < EXPONENT+1) throw new InvalidSequenceException();
        while (currentChar < s.length()) {
            Node n = head;
            //whilst we do not have an external node
            //keep searching to see what we find
            //we keep characters here because we are reading whether to go left or right
            while (!n.isLeaf() && currentChar < s.length()) {
                if (s.charAt(currentChar) == '1') {
                    n = n.right;
                } else if (s.charAt(currentChar) == '0') {
                    n = n.left;
                } else {
                    throw new InvalidSequenceException();
                }
                currentChar++;
            }
            //special case
            if (currentChar == 0) currentChar++;
            //Once we have gotten our external node
            //If we have seen this character before, eg is not 0-weighted
            if (!isNYTNode(n)) {
                String c = n.getValue();
                decoding.append(c);
                add(n, c);
                //If we have never seen this value before we need to treat it differently
            } else { //if this is the first time we have ever saw this character
                String input = s.substring(currentChar, currentChar + EXPONENT);
                currentChar += EXPONENT;
                int value = reverseBinary(input);
                /* Adapt value if it is too small */
                if (value < REMAINDER) {
                    input = s.substring(currentChar - EXPONENT, currentChar + 1);
                    value = reverseBinary(input);
                    currentChar++;
                } else { //if value > REMAINDER
                    value += REMAINDER;
                }
                String c = detectSymbol(value, numberOfSymbols);
                decoding.append(c);
                add(c);
            }
        }
        return decoding.toString();
    }
}
