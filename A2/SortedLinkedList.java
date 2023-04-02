import java.util.ListIterator;

/**
 * File: LinkedListIterator.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 *
 * A sorted linked list is an extension of the linked list that rather than preserving the inserted order
 * of the items in the list, anything that is inserted obeys the natural ordering as defined by the classes
 * {@link Comparable<T>}. Any methods that may cause the natural ordering to not be obeyed will throw an {@link UnsupportedOperationException}
 *
 * This class will attempt to order in descending order.
 *
 * @param <T> The type of data that will be stored in the list. This must be a comparable type.
 */
public class SortedLinkedList<T extends Comparable<T>> extends LinkedList<T> {

    @Override
    public boolean append(T item) {
        throw new UnsupportedOperationException("This is an invalid method for a sorted linked list");
    }

    @Override
    public boolean prepend(T item) {
        throw new UnsupportedOperationException("This is an invalid method for a sorted linked list");
    }

    /**
     * follows same functionality as insertInOrder
     */
    @Override
    public void insert(T data) {
        insertInOrder(data);
    }
    /**
     * Inserts an item with natural ordering into the linked list
     *
     * @param item The item to insert
     */
    public void insertInOrder(T item) {
        //use insertion sort
        ListIterator<T> iterator = listIterator();
        if(item == null)
            failNotComparable();

        while(iterator.hasNext()) {
            //iterate through the list, check if this comes before the next
            T next = iterator.next();

            int comparison = item.compareTo(next);
            //if greater than the next element insert it herer
            if(comparison>=0) {
                iterator.add(item); //if it comes before current then end insert it right here.
                return;
            }
        }

        super.append(item);
    }

    private void failNotComparable() {
        throw new IllegalStateException("Cannot append a non-comparable item");
    }

}
