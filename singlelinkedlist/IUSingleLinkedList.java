import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author Digno JR Teogalbo
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;
    private int modCount;
    private static int NOT_FOUND = -1;

    /** Creates an empty list */
    public IUSingleLinkedList() {
        head = null;
        tail = null;
        size = 0;
        modCount = 0;
    }

    public Node<T> giveHead() {
        return this.head;
    }

    @Override
    public void addToFront(T element) {
        Node<T> newNode = new Node<>(element, head);
        head = newNode;

        if (isEmpty()) {
            // first added element
            tail = newNode;
        }

        size++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        Node<T> newNode = new Node<>(element);

        if (isEmpty()) {
            // first added element
            head = newNode;
        } else {
            // if tail exists
            tail.setNext(newNode);
        }

        tail = newNode;
        size++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        int index = indexOf(target);

        if (index == NOT_FOUND) {
            throw new NoSuchElementException();
        }

        Node<T> newNode = new Node<>(element);
        Node<T> previous = null;
        Node<T> current = head;

        for (int i = 0; i <= index; i++) {
            previous = current;
            current = current.getNext();
        }

        newNode.setNext(current);

        if (current == head) {
            // target at head
            head = newNode;
        } else if (current == null) {
            // target at end of list
            previous.setNext(newNode);
            tail = newNode;
        } else {
            // target in the middle
            newNode.setNext(current);
            previous.setNext(newNode);
        }

        size++;
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> newNode = new Node<>(element);
        Node<T> previous = null;
        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            previous = current;
            current = current.getNext();
        }

        if (isEmpty()) {
            // first added element
            head = newNode;
            tail = newNode;
        } else if (current == head) {
            // index at head
            newNode.setNext(current);
            head = newNode;
        } else if (current == null) {
            // index after tail
            tail.setNext(newNode);
            tail = newNode;
        } else {
            // index in the middle
            newNode.setNext(current);
            previous.setNext(newNode);
        }

        size++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T value = head.getElement();

        if (size() == 1) {
            head = null;
            tail = null;
        } else {
            // more than 1 element
            head = head.getNext();
        }

        size--;
        modCount++;
        return value;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<T> previous = null;
        Node<T> current = head;

        while (current.getNext() != null) {
            previous = current;
            current = current.getNext();
        }

        if (size() == 1) {
            head = null;
            tail = null;
        } else {
            // more than 1 element
            tail = previous;
            tail.setNext(null);
        }

        size--;
        modCount++;
        return current.getElement();
    }

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        boolean found = false;
        Node<T> previous = null;
        Node<T> current = head;

        while (current != null && !found) {
            if (element.equals(current.getElement())) {
                found = true;
            } else {
                previous = current;
                current = current.getNext();
            }
        }

        if (!found) {
            throw new NoSuchElementException();
        }

        if (size() == 1) { // only node
            head = tail = null;
        } else if (current == head) { // first node
            head = current.getNext();
        } else if (current == tail) { // last node
            tail = previous;
            tail.setNext(null);
        } else { // somewhere in the middle
            previous.setNext(current.getNext());
        }

        size--;
        modCount++;
        return current.getElement();
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<T> previous = null;
        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            previous = current;
            current = current.getNext();
        }

        if (size() == 1) {
            // only 1 element
            head = null;
            tail = null;
        } else if (current == head) {
            // index at head
            head = current.getNext();
        } else if (current == tail) {
            // index at tail
            tail = previous;
            tail.setNext(null);
        } else {
            // index in the middle
            previous.setNext(current.getNext());
        }

        size--;
        modCount++;
        return current.getElement();
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        current.setElement(element);
        modCount++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> current = head;

        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        return current.getElement();
    }

    @Override
    public int indexOf(T element) {
        Node<T> current = head;
        int index = 0;

        while (current != null) {
            if (current.getElement().equals(element)) {
                return index;
            }

            index++;
            current = current.getNext();
        }

        return NOT_FOUND;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return head.getElement();
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) {
        boolean found = false;
        Node<T> current = head;

        while (current != null && !found) {
            if (current.getElement().equals(target)) {
                found = true;
            }

            current = current.getNext();
        }

        return found;
    }

    @Override
    public boolean isEmpty() {
        return (tail == null || head == null);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        Node<T> current = head;
        StringBuilder output = new StringBuilder("[");

        while (current != null) {
            T value = current.getElement();
            String comma = (current.getNext() == null) ? "" : ",";
            output.append("\"");
            output.append(value.toString());
            output.append("\"");
            output.append(comma);
            current = current.getNext();
        }

        output.append("]");
        return output.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new SLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException();
    }

    /** Iterator for IUSingleLinkedList */
    private class SLLIterator implements Iterator<T> {
        private Node<T> nextNode;
        private int iterModCount;
        private int currentIndex;
        private T lastRet;

        /** Creates a new iterator for the list */
        public SLLIterator() {
            nextNode = head;
            iterModCount = modCount;
            lastRet = null;
            currentIndex = -1; // start before first element
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return !(isEmpty() || nextNode == null);
        }

        @Override
        public T next() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            currentIndex++;
            lastRet = nextNode.getElement();
            nextNode = nextNode.getNext();

            return lastRet;
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (lastRet == null) {
                throw new IllegalStateException();
            }

            IUSingleLinkedList.this.remove(currentIndex);
            iterModCount++;
            currentIndex--;
            lastRet = null;
        }
    }
}
