/**
 * File: LinkedListNode.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 *
 * Program Description: This class acts as a template for a node instance. If a linked list is a set of elements
 * then a node is an element in the set. It's main job is to store a value of type t. Each node or element
 * in the list also stores a reference to the previous element(node) in the set and the next element(node) in the set.
 * This acts as the main basis of a linked list.
 *
 *
 * @param <T> The type of data to store in the list.
 */
class LinkedListNode<T> {

    private T data;
    private LinkedListNode<T> next;
    private LinkedListNode<T> prev;

    /**
     * Instantiates a new node with the given data
     *
     * @param data The initial data for the node. This may be null
     */
    LinkedListNode(T data) {
        this.data = data;
    }

    /**
     * Returns the data stored in this node. This may be null.
     *
     * @return The data stored in this node.
     */
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * returns the next node after this node in the list. This cannot be null unless the
     * node is about to be deleted.
     *
     * @return The next node in the list
     */
    public LinkedListNode<T> getNext() {
        return next;
    }

    /**
     * sets the next node after this node in the list. This cannot be null unless the
     * node is about to be deleted.
     *
     * @param next The next node in the list
     */
    public void setNext(LinkedListNode<T> next) {
        this.next = next;
    }


    /**
     * returns the previous node before this node in the list. This cannot be null unless the
     * node is about to be deleted.
     *
     * @return The previous node in the list
     */
    public LinkedListNode<T> getPrev() {
        return prev;
    }

    /**
     * set the previous node before this node in the list. This cannot be null unless the
     * node is about to be deleted.
     *
     * @param prev The previous node in the list
     */
    public void setPrev(LinkedListNode<T> prev) {
        this.prev = prev;
    }
}
