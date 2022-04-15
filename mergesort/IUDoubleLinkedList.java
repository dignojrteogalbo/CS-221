import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked node implementation of IndexedUnsortedList.
 * 
 * @param <T> type to store
 * 
 * @author Digno JR Teogalbo
 * @version CS 221-002 Spring 2022
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;
    private static int NOT_FOUND = -1;

    /**
     * Empty list constructor where head and tail are null and the size 
     * is zero.
     */
    public IUDoubleLinkedList() {
        head = null;
        tail = null;
        size = 0;
        modCount = 0;
    }

    /**
     * Adds the element to the front of the list, sets the head to the
     * new element node, and increments size by one.
     * 
     * @param element element to add to the front of the list
     */
    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<>(element, head);

        if (isEmpty()) {
            tail = newNode;
        } else {
            head.setPrevious(newNode);
        }

        head = newNode;
        size++;
        modCount++;
    }

    /**
     * Adds the element to the rear of the list, sets the tail to the
     * new element node, and increments size by one.
     * 
     * @param element element to add to the rear of the list
     */
    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<>(element, null, tail);

        if (isEmpty()) {
            head = newNode;
        } else {
            tail.setNext(newNode);
        }

        tail = newNode;
        size++;
        modCount++;
    }

    /**
     * Adds the element to the rear of the list, sets the tail to the
     * new element node, and increments size by one. Performs the same
     * actions as addToRear().
     * 
     * @param element element to add to the rear of the list
     */
    @Override
    public void add(T element) {
        addToRear(element);
    }

    /** 
     * Adds the element after the target, and increments size by one. If
     * the target is the tail also set tail to the new element node.
     * 
     * @param element element to add after target
     * @param target target the element will be added after
     */
    @Override
    public void addAfter(T element, T target) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = indexOf(target);

        if (index == NOT_FOUND) {
            throw new NoSuchElementException();
        }

        Node<T> targetNode = gotoNode(index);
        Node<T> targetNodeNext = targetNode.getNext();

        if (targetNode == tail) {
            addToRear(element);
            return;
        }

        Node<T> newNode = new Node<>(element, targetNodeNext, targetNode);
        targetNode.setNext(newNode);
        targetNodeNext.setPrevious(newNode);
        size++;
        modCount++;
    }

    /** 
     * Inserts the element at the index and existing elements at the 
     * index and after will be shifted up one index.
     * 
     * @param index index where the element will be inserted
     * @param element element to be inserted
     */
    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (isEmpty()) {
            Node<T> newNode = new Node<>(element);
            head = newNode;
            tail = newNode;
            size++;
            modCount++;
            return;
        }

        if (index == 0) {
            addToFront(element);
            return;
        } else if (index == size()) {
            addToRear(element);
            return;
        }

        Node<T> targetNode = gotoNode(index);
        Node<T> targetNodePrev = targetNode.getPrevious();
        Node<T> newNode = new Node<>(element, targetNode, targetNodePrev);
        targetNodePrev.setNext(newNode);
        targetNode.setPrevious(newNode);
        size++;
        modCount++;
    }

    /** 
     * Removes the first element, decrements size by one, and returns the
     * removed element.
     * 
     * @return T first element in list
     * @throws NoSuchElementException if list is empty
     */
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T value = head.getElement();
        head = head.getNext();

        if (head != null) {
            head.setPrevious(null);
        } else {
            tail = null;
        }

        size--;
        modCount++;
        return value;
    }

    /** 
     * Removes the last element, decrements size by one, and returns the 
     * removed element.
     * 
     * @return T last element in list
     * @throws NoSuchElementException if list is empty
     */
    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T value = tail.getElement();
        tail = tail.getPrevious();

        if (tail != null) {
            tail.setNext(null);
        } else {
            head = null;
        }

        size--;
        modCount++;
        return value;
    }


    /**
     * Removes the element, decrements size by one, and returns the
     * removed element. If index == 0 perform removeFirst(), otherwise
     * if index == size - 1 perform removeLast().
     * 
     * @param element element to be removed in list
     * @return T element that was removed from list
     * @throws NoSuchElementException if list is empty or element is not found
     */
    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = indexOf(element);

        if (index == NOT_FOUND) {
            throw new NoSuchElementException();
        } else if (index == 0) {
            return removeFirst();
        } else if (index == size() - 1) {
            return removeLast();
        }

        Node<T> current = gotoNode(index);
        Node<T> currentNext = current.getNext();
        Node<T> currentPrev = current.getPrevious();
        currentNext.setPrevious(currentPrev);
        currentPrev.setNext(currentNext);
        size--;
        modCount++;
        return current.getElement();
    }


    /**
     * Removes the element at index in list, decrements size by one, 
     * and returns the removed element. If index == 0 perform 
     * removeFirst(), otherwise if index == size - 1 perform removeLast().
     * 
     * @param index index of the element to remove
     * @return T element that was removed from list
     */
    @Override
    public T remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        if (isEmpty()) {
            throw new NoSuchElementException();
        } else if (index == 0) {
            return removeFirst();
        } else if (index == size() - 1) {
            return removeLast();
        }

        Node<T> current = gotoNode(index);
        Node<T> currentNext = current.getNext();
        Node<T> currentPrev = current.getPrevious();
        currentNext.setPrevious(currentPrev);
        currentPrev.setNext(currentNext);
        size--;
        modCount++;
        return current.getElement();
    }


    /** 
     * Change the element at index in the list to the new specified 
     * element.
     * 
     * @param index index of the element to be replace
     * @param element new element to replace existing element
     */
    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> targetNode = gotoNode(index);
        targetNode.setElement(element);
        modCount++;
    }


    /** 
     * Return a reference to the element at the index in the list. If 
     * index == 0 return first(), otherwise if index == size - 1 return
     * last().
     * 
     * @param index index of the element to get reference
     * @return T element reference at the index
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            return first();
        } else if (index == size() - 1) {
            return last();
        }

        Node<T> targetNode = gotoNode(index);
        return targetNode.getElement();
    }


    /** 
     * Retrieves the index of the first element in the list that matches 
     * the specified target. Performs a two pointer search starting at
     * the head and tail towards the center.
     * 
     * @param element element target to find first index of
     * @return int index for the element or -1 if list is empty or element is not found
     */
    @Override
    public int indexOf(T element) {
        if (isEmpty()) {
            return NOT_FOUND;
        }

        Node<T> left = head;
        Node<T> right = tail;
        int leftIdx = 0;
        int rightIdx = size() - 1;

        while (leftIdx <= rightIdx) {
            if (element.equals(left.getElement())) {
                return leftIdx;
            } else if (element.equals(right.getElement())) {
                return rightIdx;
            }

            left = left.getNext();
            right = right.getPrevious();
            leftIdx++;
            rightIdx--;
        }

        return NOT_FOUND;
    }


    /** 
     * Retrieves the reference to the first element in the list.
     * 
     * @return T first element reference
     * @throws NoSuchElementException if the list is empty
     */
    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return head.getElement();
    }

    
    /**
     * Retrieves the reference to the last element in the list.
     * 
     * @return T last element reference
     * @throws NoSuchElementException if the list is empty
     */
    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return tail.getElement();
    }


    /** 
     * Returns true if the target element exists in the list.
     * 
     * @param target target element to find
     * @return boolean true if the element exists in the list
     */
    @Override
    public boolean contains(T target) {
        if (isEmpty()) {
            return false;
        }

        return indexOf(target) != NOT_FOUND;
    }


    /** 
     * Returns a boolean value indicating if the list is empty or not: 
     * true when the list is empty, false when the list is not empty.
     * 
     * @return boolean true if size == 0, otherwise false
     */
    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }


    /** 
     * Returns the number of elements in the list.
     * 
     * @return int int of number of elements in list.
     */
    @Override
    public int size() {
        return size;
    }


    /** 
     * Returns a string object containing all the elements in the list
     * using an iterator. Example string output will be "[A, B, C]" if
     * list contains elements A, B, and C with indexes being 0, 1, and 2
     * respectively.
     * 
     * @return String string representation of elements in the list
     */
    @Override
    public String toString() {
        Iterator<T> iterator = this.iterator();
        StringBuilder output = new StringBuilder("[");
        String QUOTE = "\"";
        String COMMA = ",";

        while (iterator.hasNext()) {
            T value = iterator.next();
            output.append(QUOTE);
            output.append(value.toString());
            output.append(QUOTE);

            if (iterator.hasNext()) {
                output.append(COMMA);
            }
        }

        output.append("]");
        return output.toString();
    }


    /**
     * Traverses the linked list from the head or tail, based on
     * whichever the index is closest to.
     * 
     * @param index index of node
     * @return Node<T> node at index or null if (index < 0 || index == size)
     */
    private Node<T> gotoNode(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }

        Node<T> current;

        if (index < size() / 2) { // index is closer to head
            current = head;

            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
        } else { // index is closer to tail
            current = tail;

            for (int i = size - 1; i > index; i--) {
                current = current.getPrevious();
            }
        }

        return current;
    }


    /** 
     * Returns an iterator for the elements in the list.
     * 
     * @return Iterator<T> iterator for the elements in the list
     */
    @Override
    public Iterator<T> iterator() {
        return new DLLIterator();
    }


    /** 
     * Returns a list iterator for the elements in the list.
     * 
     * @return ListIterator<T> list iterator for the elements in the list
     */
    @Override
    public ListIterator<T> listIterator() {
        return new DLListIterator();
    }


    /** 
     * Returns a list iterator with an initial position before the 
     * starting index.
     * 
     * @param startingIndex int of the element to position the list iterator before
     * @return ListIterator<T> list iterator at the position before the index and after index - 1
     */
    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLListIterator(startingIndex);
    }

    /**
     * The iterator for this list. Iterator is positioned before the nextNode,
     * iterModCount indicates how many modifications this iterator has
     * performed and should match with the list's mod count, and lastRet 
     * is the last returned value of this iterator or null after a call to 
     * remove().
     */
    private class DLLIterator implements Iterator<T> {
        private Node<T> nextNode;
        private int iterModCount;
        private T lastRet;

        /**
         * Iterator positioned before the first element.
         */
        public DLLIterator() {
            nextNode = head;
            iterModCount = modCount;
            lastRet = null;
        }

        /**
         * Returns true if there is an element to traverse.
         * 
         * @return boolean true if there are elements to traverse
         */
        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return (!isEmpty() && nextNode != null);
        }

        /**
         * Returns the next element and traverses the iterator forward. 
         * Increases the iterator index by one and sets the nextNode to
         * the next node.
         * 
         * @throws ConcurrentModificationException if the list is modified by another iterator
         * @throws NoSuchElementException if there is no element to traverse
         */
        @Override
        public T next() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            lastRet = nextNode.getElement();
            nextNode = nextNode.getNext();
            return lastRet;
        }

        /**
         * Removes the element last returned by this iterator. This 
         * method can only be called once after each call to next().
         * 
         * @throws ConcurrentModificationException if the list is modified by another iterator
         * @throws IllegalStateException if next() method has not been called before
         */
        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (lastRet == null) {
                throw new IllegalStateException();
            }

            // remove node before nextNode
            Node<T> removeNode = (nextNode != null) ? nextNode.getPrevious() : tail;

            if (size() == 1) { // only 1 element to remove
                head = null;
                tail = null;
                nextNode = null;
            } else if (removeNode == head) { // remove head
                head = head.getNext();
                head.setPrevious(null);
            } else if (removeNode == tail) { // remove tail
                tail = tail.getPrevious();
                tail.setNext(null);
            } else { // remove node at middle
                Node<T> removeNodeNext = removeNode.getNext();
                Node<T> removeNodePrev = removeNode.getPrevious();
                removeNodeNext.setPrevious(removeNodePrev);
                removeNodePrev.setNext(removeNodeNext);
            }

            size--;
            iterModCount++;
            modCount++;
            lastRet = null;
        }
    }

    /**
     * The list iterator for this list. Iterator is positioned after
     * prevNode and before nextNode. nextIndex is the int of the index 
     * after this list iterator. lastRet is the last returned value
     * from this list iterator or null after a call to set(), remove(),
     * or add().
     */
    private class DLListIterator implements ListIterator<T> {
        private Node<T> nextNode;
        private Node<T> prevNode;
        private T lastRet;
        private int iterModCount;
        private int nextIndex;

        /**
         * List iterator positioned before the first element.
         */
        public DLListIterator() {
            nextNode = head;
            prevNode = null;
            lastRet = null;
            iterModCount = modCount;
            nextIndex = 0;
        }

        /**
         * List iterator positioned before the startingIndex and after
         * startingIndex - 1.
         * 
         * @param startingIndex int of the element to position the list iterator before
         * @throws IndexOutOfBoundsException if (startingIndex < 0 || startingIndex > size)
         */
        public DLListIterator(int startingIndex) {
            if (startingIndex < 0 || startingIndex > size()) {
                throw new IndexOutOfBoundsException();
            }

            nextNode = gotoNode(startingIndex);
            prevNode = (nextNode != null) ? nextNode.getPrevious() : tail;
            lastRet = null;
            iterModCount = modCount;
            nextIndex = startingIndex;
        }

        /**
         * Returns true if there is an element to traverse in the 
         * forward direction.
         * 
         * @return boolean true if there are elements to traverse in the forward direction
         * @throws ConcurrentModificationException if another iterator has modified the list or if the iterModCount does not match the list modCount
         */
        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return (!isEmpty() && nextNode != null);
        }

        /**
         * Returns a reference to the element before the iterator after
         * the iterator moves to the next position forward.
         * 
         * @return T element reference to the element before the iterator after moving forward
         * @throws ConcurrentModificationException if another iterator has modified the list or if the iterModCount does not match the list modCount
         * @throws NoSuchElementException if there is no element to traverse in the forward direction
         */
        @Override
        public T next() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            lastRet = nextNode.getElement();
            prevNode = nextNode;
            nextNode = nextNode.getNext();
            nextIndex++;
            return lastRet;
        }

        /**
         * Returns true if there is an element to traverse in the 
         * backwards direction.
         * 
         * @return boolean true if there are elements to traverse in the backwards direction
         */
        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return (!isEmpty() && prevNode != null);
        }

        /**
         * Returns a reference to the element before the iterator after
         * the iterator moves to the next position backwards.
         * 
         * @return T element reference to the element before the iterator after moving backwards
         * @throws ConcurrentModificationException if another iterator has modified the list or if the iterModCount does not match the list modCount
         * @throws NoSuchElementException if there is no element to traverse in the backwards direction
         */
        @Override
        public T previous() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            lastRet = prevNode.getElement();
            nextNode = prevNode;
            prevNode = prevNode.getPrevious();
            nextIndex--;
            return lastRet;
        }

        /**
         * Returns the index of the element after the list iterator.
         * 
         * @return int index of the element after the list iterator
         * @throws ConcurrentModificationException if another iterator has modified the list or if the iterModCount does not match the list modCount
         */
        @Override
        public int nextIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return nextIndex;
        }

        /**
         * Returns the index of the element before the list iterator.
         * 
         * @return int index of the element before the list iterator
         * @throws ConcurrentModificationException if another iterator has modified the list or if the iterModCount does not match the list modCount
         */
        @Override
        public int previousIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return nextIndex - 1;
        }

        /**
         * Removes the last element returned by the list iterator from
         * previous() or next(). This method can only be called once
         * per call to previous() or next(), and only if add() was not
         * called after the last call to next() or previous().
         * 
         * @throws ConcurrentModificationException if another iterator has modified the list or if the iterModCount does not match the list modCount
         * @throws IllegalStateException if neither next() or previous() had been called, or add() has been called after the last call to next() or previous()
         */
        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (lastRet == null) {
                throw new IllegalStateException();
            }

            if (size() == 1) { // only 1 element
                head = tail = nextNode = prevNode = null;
                lastRet = null;
                nextIndex--;
                size--;
                iterModCount++;
                modCount++;
                return;
            }

            Node<T> removeNode;

            if (prevNode != null && nextNode != null) {
                // removeNode is whichever lastReturn is equal to
                removeNode = lastRet.equals(nextNode.getElement()) ? nextNode : prevNode;
            } else {
                // removeNode is the option that is not null
                removeNode = (nextNode != null) ? nextNode : prevNode;
            }

            if (removeNode == head) { // remove head
                head = removeNode.getNext();
                head.setPrevious(null);
                nextNode = head;
                prevNode = null;
            } else if (removeNode == tail) { // remove tail
                tail = removeNode.getPrevious();
                tail.setNext(null);
                prevNode = tail;
                nextNode = null;
            } else { // remove middle
                Node<T> removeNodeNext = removeNode.getNext();
                Node<T> removeNodePrevious = removeNode.getPrevious();
                removeNodeNext.setPrevious(removeNodePrevious);
                removeNodePrevious.setNext(removeNodeNext);
            }

            lastRet = null;
            nextIndex--;
            size--;
            iterModCount++;
            modCount++;
        }

        /**
         * Replace the element that was last returned by next() or 
         * previous() from this list iterator with the new specified 
         * element. This method can only be called once per call to 
         * previous() or next(), and only if add() was not called 
         * after the last call to next() or previous().
         * 
         * @param e T the new element to replace the last returned value
         * @throws ConcurrentModificationException if another iterator has modified the list or if the iterModCount does not match the list modCount
         * @throws IllegalStateException if neither next() or previous() had been called, or add() has been called after the last call to next() or previous()
         */
        @Override
        public void set(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (lastRet == null || size() == 0) {
                throw new IllegalStateException();
            }

            Node<T> setNode;

            if (prevNode != null && nextNode != null) {
                // setNode is whichever lastReturn is equal to
                setNode = lastRet.equals(nextNode.getElement()) ? nextNode : prevNode;
            } else {
                // setNode is the option that is not null
                setNode = (nextNode != null) ? nextNode : prevNode;
            }

            setNode.setElement(e);
            iterModCount++;
            modCount++;
        }

        /**
         * 
         * Adds the a new element into the list at the location of the
         * list iterator. The element will be added before the element 
         * returned by next() and after the element returned by 
         * previous(). If the list is empty, then the new element will
         * be the only element in the list.
         * 
         * @throws ConcurrentModificationException if another iterator has modified the list or if the iterModCount does not match the list modCount
         * @throws IllegalStateException if neither next() or previous() had been called, or add() has been called after the last call to next() or previous()
         */
        @Override
        public void add(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            Node<T> newNode = new Node<>(e);

            if (size() == 0) {
                head = newNode;
                tail = newNode;
            } else if (prevNode == null) { // iterator before first element
                newNode.setNext(head);
                head.setPrevious(newNode);
                head = newNode;
            } else if (prevNode == tail) { // iterator after last element
                newNode.setPrevious(tail);
                tail.setNext(newNode);
                tail = newNode;
            } else { // iterator at middle
                newNode.setNext(nextNode);
                newNode.setPrevious(prevNode);
                prevNode.setNext(newNode);
                nextNode.setPrevious(newNode);
            }

            lastRet = null;
            nextIndex++;
            size++;
            iterModCount++;
            modCount++;
        }
    }
}