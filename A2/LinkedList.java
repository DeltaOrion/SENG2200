import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * File: LinkedList1.java
 *
 * Author: Jacob Boyce
 * Course: SENG2200
 * Program Description:
 *
 * This class represents a mutable list of n items. The amount of items may be changed at any time. The only ordering of items
 * this class will respect is the order in which they were inserted into the list.
 *
 *
 * Items are stored using the circular doubly linked list data structure. This class is coupled to that specific implementation and iteration
 * is also coupled to this class. Each node of the linked list links to the next element and the previous element in the list. There is a sentinel
 * node which marks the start and the end of the list. The previous element of the sentinel node is the tail and the next is the head. Both
 * the tail and head link respectively to the sentinel node.
 *
 * @param <T> The type of data to store in the list.
 */
public class LinkedList<T> implements Iterable<T> {

    //cannot be null
    private final LinkedListNode<T> sentinel;
    private int modCount;

    /**
     * Instantiates an empty items list. The sentinel node should be the only node that takes in null
     * values and thus any additional appended or prepended data must not contain nulls. The current node
     * should be set to the sentinel which would mean items cannot step.
     *
     *
     */
    public LinkedList() {
        sentinel = new LinkedListNode<>(null);
        sentinel.setNext(sentinel);
        sentinel.setPrev(sentinel);
        this.modCount = 0;
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
     * @throws UnsupportedOperationException if the list definition does not allow this operation
     */
    public boolean append(T item) {
        MoreObjects.requiresNonNull(item,"Cannot insert null items into this structure");
        LinkedListNode<T> tail = getTail();
        LinkedListNode<T> insert = new LinkedListNode<>(item);
        //require connections
        tail.setNext(insert);
        insert.setPrev(tail);

        sentinel.setPrev(insert);
        insert.setNext(sentinel);

        modCount++;

        return true;
    }

    private LinkedListNode<T> getTail() {
        return sentinel.getPrev();
    }

    private LinkedListNode<T> getHead() {
        return sentinel.getNext();
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
     * @throws UnsupportedOperationException if the list definition does not allow this operation
     */
    public boolean prepend(T item) {
        MoreObjects.requiresNonNull(item,"Cannot insert null items into this structure");
        LinkedListNode<T> head = getHead();
        LinkedListNode<T> insert = new LinkedListNode<>(item);

        //make new node, move connections appropriately
        head.setPrev(insert);
        insert.setNext(head);

        insert.setPrev(sentinel);
        sentinel.setNext(insert);

        modCount++;

        return true;
    }

    /**
     * Retrieves the first item in this list. This operation should not affect the list in any way.The returned
     * value may not be null.
     *
     * @return The the first element in the list
     * @throws ArrayIndexOutOfBoundsException if the first element does not exist
     */
    public T getFirst() {
        T data = this.sentinel.getNext().getData();
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
    public T getLast() {
        T data = this.sentinel.getPrev().getData();
        if(data == null)
            failOutOfBounds(0);

        return data;
    }

    /**
     * follows same functionality as {@link #append(Object)}
     */
    public void insert(T data) {
        append(data);
    }

    /**
     * Iteration was moved over to {@link #iterator()}
     */
    @Deprecated
    public T next() {
        throw new UnsupportedOperationException("these are invalid methods on LinkedList.");
    }

    /**
     * Iteration was moved over to {@link #iterator()}
     */
    @Deprecated
    public void reset() {
        throw new UnsupportedOperationException("these are invalid methods on LinkedList.");
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
    public T removeFirst() {
        LinkedListNode<T> head = this.sentinel.getNext();
        if(head.getData()==null)
            failOutOfBounds(0);

        //get the node to remove, rewire the connections
        sentinel.setNext(head.getNext());
        head.getNext().setPrev(sentinel);

        head.setNext(null);
        head.setPrev(null);


        modCount++;
        //garbage collector should remove now
        //reset current appropiately
        return head.getData();
    }

    /**
     * Retrieves the amount of elements inside of the list. This number should never be less than 0.
     *
     * @return The amount of elements in the list
     */
    public int size() {
        LinkedListNode<T> root = this.sentinel;
        int count = 0;
        while(root.getNext().getData()!=null) {
            root = root.getNext();
            count++;
        }
        return count;
    }

    /**
     * Checks whether two Myitems are equal. They are only equal if they have the same elements in the same
     * order.
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof LinkedList))
            return false;

        //get the second list
        LinkedList<?> list = (LinkedList<?>) o;
        //if they don't have the same size then they are not equal as a fact
        if(list.size() != this.size())
            return false;

        LinkedListNode<T> head = sentinel.getNext();
        LinkedListNode<?> headB = list.sentinel.getNext();
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
     * Returns a string format of the  list in the format
     * [element1, element2, element3]
     */
    @Override
    public String toString() {
        String builder = "[";
        //grab temporary node as to not disturb the position of current.
        LinkedListNode<T> head = sentinel.getNext();
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

    /**
     * Follows definition provided by {@link Iterable#iterator()}
     */
    @Override
    public Iterator<T> iterator() {
        return listIterator();
    }

    /**
     * @return The raw list iterator for more precise manipulation of the list.
     */
    protected ListIterator<T> listIterator() {
        return new LinkedListIterator();
    }

    /**
     * File: LinkedList.java
     *
     * Author: Jacob Boyce
     * Course: SENG2200
     * Program Description:
     * The linked list iterator provides iterator functions for a {@link LinkedList}. This allows a user to loop through
     * the list. The iterator complies fully with the {@link ListIterator} and {@link java.util.Iterator} specification.
     */
    private class LinkedListIterator implements ListIterator<T> {

        private LinkedListNode<T> current;
        private int index;
        private final int expectedModCount;

        public LinkedListIterator() {
            this.current = sentinel;
            this.index = 0;
            this.expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            //check if next node is the sentinel
            checkForCModification();

            return current.getNext().getData() != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                failNoElement(index + 1);
            }

            checkForCModification();
            //step to next element
            current = current.getNext();
            index++;
            return current.getData();
        }

        @Override
        public boolean hasPrevious() {
            //check if the previous node is the sentinel
            checkForCModification();
            return current.getPrev().getData() != null;
        }

        @Override
        public T previous() {
            if (!hasPrevious()) {
                failNoElement(index - 1);
            }

            checkForCModification();
            //step back
            current = current.getPrev();
            index--;
            return current.getData();
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            if (current.getData() == null)
                throw new IllegalStateException();

            checkForCModification();

            //get the node for deletion
            LinkedListNode<T> del = current;

            //rewire connections so it becomes garbage
            current = current.getPrev();
            current.setNext(del.getNext());
            del.getNext().setPrev(current);
            //clean up for easy gc
            del.setNext(null);
            del.setPrev(null);

            index--;

        }

        @Override
        public void set(T t) {
            if(t==null)
                throw new IllegalArgumentException("Cannot insert a null element into this list");

            if(current.getData()==null)
                throw new IllegalStateException("Cannot set this element");

            checkForCModification();

            current.setData(t);
        }

        @Override
        public void add(T t) {
            if(t==null)
                throw new IllegalArgumentException("Cannot insert a null element into this list");

            checkForCModification();

            LinkedListNode<T> prev = new LinkedListNode<>(t);
            //move connections around to insert the element before
            current.getPrev().setNext(prev);
            prev.setPrev(current.getPrev());
            prev.setNext(current);
            current.setPrev(prev);
            //add size
        }

        /**
         * Called IF an operation fails due to the element not existing in the list. This does not check whether it exists.
         *
         * @param i The index of the failed retrieval
         * @throws java.util.NoSuchElementException always
         */
        private void failNoElement(int i) {
            throw new NoSuchElementException("Cannot access element '"+i+"' as it is greater than the list size '"+size()+"'");
        }

        private void checkForCModification() {
            if(modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }


}
