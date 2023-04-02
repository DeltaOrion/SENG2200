package me.jacob.assign;

import me.jacob.assign.util.MoreObjects;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Scanner;

/**
 * File: MyPolygons.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 *
 * This class represents a mutable list of n polygons. The amount of polygons may be changed at any time. The only ordering of polygons
 * this class will respect is the order in which they were inserted into the list.
 *
 * However, this does provide a method {@link #appendInOrder(Polygon)} which when inserted will place the polygon in its natural ordering according
 * to {@link ComparePoly}. This will only ensure that the inserted polygon is ordered according to this specification and will not do
 * anything about the rest of the list.
 *
 * Polygons are stored using the circular doubly linked list data structure. This class is coupled to that specific implementation and iteration
 * is also coupled to this class. Each node of the linked list links to the next element and the previous element in the list. There is a sentinel
 * node which marks the start and the end of the list. The previous element of the sentinel node is the tail and the next is the head. Both
 * the tail and head link respectively to the sentinel node.
 */
public class MyPolygons implements Parseable {

    //cannot be null
    private final Node sentinel;
    private Node current;
    private int size;
    int currentIndex;

    /**
     * Instantiates an empty polygons list. The sentinel node should be the only node that takes in null
     * values and thus any additional appended or prepended data must not contain nulls. The current node
     * should be set to the sentinel which would mean polygons cannot step.
     *
     *
     */
    public MyPolygons() {
        sentinel = new Node(null);
        sentinel.setNext(sentinel);
        sentinel.setPrev(sentinel);

        current = sentinel;
        currentIndex = 0;
        size = 0;
    }

    /**
     * Appends a new item to the end of the list. The appended item must be at the end of the list and the ordering of all
     * the other elements must maintain the order they were inserted in. The new inserted node may not contain
     * any null data.
     *
     * Every time this is called the current node should be moved to the start of the list.
     *
     * @param item The non null item to insert
     * @return whether this operation affected the list
     */
    public boolean append(Polygon item) {
        MoreObjects.requiresNonNull(item,"Cannot insert null items into this structure");
        Node tail = sentinel.getPrev();
        Node insert = new Node(item);

        tail.setNext(insert);
        insert.setPrev(tail);

        sentinel.setPrev(insert);
        insert.setNext(sentinel);

        resetCurrent();
        this.size++;
        return true;
    }

    /**
     * Appends a new item to the start of the list. The appended item must be at the start of the list and the ordering of all
     * the other elements must maintain the order they were inserted in. The new inserted node may not contain
     * any null data.
     *
     * Every time this is called the current node should be moved to the start of the list.
     *
     * @param item The non null item to insert
     * @return whether this operation affected the list
     */
    public boolean prepend(Polygon item) {
        MoreObjects.requiresNonNull(item,"Cannot insert null items into this structure");
        Node head = sentinel.getNext();
        Node insert = new Node(item);

        //make new node, move connections appropriately
        head.setPrev(insert);
        insert.setNext(head);

        insert.setPrev(sentinel);
        sentinel.setNext(insert);

        //move current back to the start, modify the size accordingly.
        resetCurrent();
        this.size++;
        return true;
    }

    /**
     * Retrieves the first item in this list. This operation should not affect the list in any way.The returned
     * value may not be null.
     *
     * @return The the first element in the list
     * @throws ArrayIndexOutOfBoundsException if the first element does not exist
     */
    public Polygon getFirst() {
        Polygon data = this.sentinel.getNext().getData();
        if(data == null)
            failOutOfBounds(0);

        return data;
    }

    /**
     * Called IF an operation fails due to the element not existing in the list. This does not check whether it exists.
     *
     * @param i The index of the failed retrieval
     * @throws ArrayIndexOutOfBoundsException always
     */
    private void failOutOfBounds(int i) {
        throw new ArrayIndexOutOfBoundsException("Cannot access element '"+i+"' as it is greater than the list size '"+size()+"'");
    }

    /**
     * Retrieves the last item in this list. This operation should not affect the list in any way. The returned
     * value may not be null.
     *
     * @return The the last element in the list
     * @throws ArrayIndexOutOfBoundsException if the last element does not exist
     */
    public Polygon getLast() {
        Polygon data = this.sentinel.getPrev().getData();
        if(data == null)
            failOutOfBounds(0);

        return data;
    }

    /**
     * Removes the first item from the list. After this operation is complete all other elements should maintain their order. The second
     * element of the list should become the first element in the list. The sentinel node must NOT be deleted for any reason. If there is no second
     * node then the list should become an empty list again with just the sentinel.
     *
     * Every time this is called the current node must be moved to the start of the list. If there is no more start of the list then the current
     * node should become the sentinel node.
     *
     * This may not return a null value.
     *
     * @return The polygon that was just removed from the list.
     * @throws ArrayIndexOutOfBoundsException if there is no elements to remove from the list
     */
    public Polygon removeFirst() {
        Node head = this.sentinel.getNext();
        if(head.getData()==null)
            failOutOfBounds(0);

        //get the node to remove, rewire the connections
        sentinel.setNext(head.getNext());
        head.getNext().setPrev(sentinel);

        head.setNext(null);
        head.setPrev(null);

        //garbage collector should remove now
        //reset current appropiately
        this.size--;
        resetCurrent();
        return head.getData();
    }

    /**
     * Retrieves the amount of elements inside of the list. This number should never be less than 0.
     *
     * @return The amount of elements in the list
     */
    public int size() {
        return this.size;
    }

    /**
     * Retrieves the current element. Current simply points to a node in the list. It can be stepped using {@link #step()} to move it
     * forward and reset back to the list with {@link #resetCurrent()}. This allows you to iterate across the list.
     *
     * This may not return a null value
     *
     * @return The data stored in the current node.
     * @throws ArrayIndexOutOfBoundsException If the list is empty
     */
    public Polygon getCurrent() {
        if(this.current.getData()==null)
            failOutOfBounds(currentIndex);

        return current.getData();
    }

    /**
     * Checks whether the current node can be stepped any furthur without causing an error.
     *
     * @return Whether the current node can be stepped any furthur.
     */
    public boolean canStep() {
        return current.getData() != null;
    }

    /**
     * Moves the current node to the next element in the list. If the current node cannot be pushed furthur then this will
     * cause an error. This should not alter the list in any way.
     *
     * This may not return a null value
     *
     * @return The value of current, before this was stepped.
     * @throws ArrayIndexOutOfBoundsException if there is nowhere to step to.
     */
    public Polygon step() {
        if(!canStep())
            failOutOfBounds(currentIndex+1);

        Polygon currentData = current.getData();
        current = current.getNext();
        currentIndex++;
        return currentData;
    }

    /**
     * Removes the current node from the list. The new current mode will attempt to maintain its position in the
     * list. If the current node is the last element then the new current will become the new last element. If current is
     * the first element then the new current will be the new first element.
     *
     * The sentinel node must NOT be deleted for any reason. This may not return null.
     *
     * This may not return a null value;
     *
     * @return The value of the current node.
     * @throws ArrayIndexOutOfBoundsException if there is nothing to remove from the list.
     */
    public Polygon removeCurrent() {
        if(current.getData()==null)
            failOutOfBounds(0);

        //get the node for deletion
        Node del = current;
        current = current.getNext();

        //rewire connections so it becomes garbage
        current.setPrev(del.getPrev());
        del.getPrev().setNext(current);

        del.setNext(null);
        del.setPrev(null);

        size--;
        currentIndex--;

        //set current to new value
        if(current.getData()==null)
            current = current.getPrev();

        return del.getData();
    }


    public void resetCurrent() {
        this.currentIndex = 0;
        current = sentinel.getNext();
    }

    /**
     * Inserts an item before the current variable. This should not change the rest of the list and simply insert
     * it in the position before current. The value of current should not change
     *
     * @param item The item to insert, this cannot be null
     * @return Whether this altered the list or not
     */
    public boolean insertCurrent(Polygon item) {
        MoreObjects.requiresNonNull(item);
        if(current.getData()==null)
            return append(item);

        //make node to insert, rewire connections appropiately
        Node insert = new Node(item);
        current.getPrev().setNext(insert);
        insert.setPrev(current.getPrev());

        current.setPrev(insert);
        insert.setNext(current);

        currentIndex++;
        this.size++;
        return true;
    }

    /**
     * Checks whether two MyPolygons are equal. They are only equal if they have the same elements in the same
     * order.
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof MyPolygons))
            return false;

        //get the second list
        MyPolygons list = (MyPolygons) o;
        //if they dont have the same size then they are not equal as a fact
        if(list.size() != this.size)
            return false;

        list.resetCurrent();
        Node head = sentinel.getNext();
        Node headB = list.sentinel.getNext();
        //get each head to begin iterating.
        while(head.getData()!=null) {
            //check if data is equal
            if(!MoreObjects.equals(head.getData(),headB.getData()))
                return false;

            //get the next element in each list to check
            head = head.getNext();
            headB = headB.getNext();
        }
        return true;
    }

    /**
     * Returns a string format of the MyPolygons list in the format
     * [element1, element2, element3]
     */
    @Override
    public String toString() {
        String builder = "[";
        //grab temporary node as to not disturb the position of current.
        Node head = sentinel.getNext();
        while(head.getData()!=null) {
            builder = builder + head.getData();
            head = head.getNext();
            if(head.getData()!=null) {
                builder = builder + ",";
            }
        }
        builder = builder + "]";
        return builder;
    }

    public boolean appendInOrder(Polygon polygon) {
        resetCurrent();
        while(canStep()) {
            //iterate through the list, check if this comes before the next
            if(polygon.ComesBefore(getCurrent())) {
                return insertCurrent(polygon); //if it comes before current then end insert it right here.
            }
            step();
        }

        return append(polygon);
    }

    @Override
    public void write() throws IOException {
        resetCurrent();
        while(canStep()) {
            System.out.print(step().toString());
            System.out.print(System.lineSeparator());
        }
        //user should close writer, they may still have use
    }


    /**
     * Parses a MyPolygons from an input stream. The stream must not be null and should be valid
     *
     * @param stream a non-null stream
     * @return The new MyPolygons created from the stream.
     * @throws InvalidParseException If the stream cannot be parsed.
     */
    public static MyPolygons parse(InputStream stream) throws InvalidParseException {
        Scanner scanner = new Scanner(stream);
        MyPolygons polygons = new MyPolygons();

        //initialise all variables needed for parsing
        Point[] current = null;
        boolean foundX = false;
        double x = 0;
        int sides = -1;
        int lineCount = 1;
        int i = 0;

        //iterate line by line to avoid overflowing the buffer.
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //split the line on whitespace
            for(String token : line.split("\\s+")) {
                if(token.equals("P")) {
                    //this means that we have found a new polygon. add it to the list accordingly
                    if(current!=null) {
                        polygons.append(new Polygon(current));
                    }
                    current = null;
                    sides = -1;
                } else if(token.length()>0) {
                    //parse the number, the specification should only have P and numbers
                    double num = 0;
                    try {
                        num = Double.parseDouble(token);
                    } catch (NumberFormatException e) {
                        throw new InvalidParseException("illegal token on line '"+lineCount+"' Token: '"+token+"'",e);
                    }

                    //if we are yet to find a side establish the new current with n sides.
                    if(sides == -1) {
                        sides = (int) num;
                        current = new Point[sides];
                        i = 0;
                        continue;
                    }

                    //if we have found the x coordinate add that and continue
                    if(!foundX) {
                        x = num;
                        foundX = true;
                    } else {
                        //otherwise establish a new point.
                        current[i] = new Point(x,num);
                        i++;
                        foundX = false;
                    }
                }
            }
            lineCount++;
        }

        if(current!=null) {
            polygons.append(new Polygon(current));
        }

        scanner.close();
        //user should close input stream, they may still have a use
        return polygons;
    }

    /**
     * Represents a node of the linked list. Each node has both a head and a tail link
     */
    private static class Node {

        private final Polygon data;
        private Node next;
        private Node prev;

        /**
         * Instantiates a new node with the given data
         *
         * @param data The initial data for the node. This may be null
         */
        private Node(Polygon data) {
            this.data = data;
        }

        /**
         * Returns the data stored in this node. This may be null.
         *
         * @return The data stored in this node.
         */
        public Polygon getData() {
            return data;
        }

        /**
         * returns the next node after this node in the list. This cannot be null unless the
         * node is about to be deleted.
         *
         * @return The next node in the list
         */
        public Node getNext() {
            return next;
        }

        /**
         * sets the next node after this node in the list. This cannot be null unless the
         * node is about to be deleted.
         *
         * @param next The next node in the list
         */
        public void setNext(Node next) {
            this.next = next;
        }


        /**
         * returns the previous node before this node in the list. This cannot be null unless the
         * node is about to be deleted.
         *
         * @return The previous node in the list
         */
        public Node getPrev() {
            return prev;
        }

        /**
         * set the previous node before this node in the list. This cannot be null unless the
         * node is about to be deleted.
         *
         * @param prev The previous node in the list
         */
        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
}
