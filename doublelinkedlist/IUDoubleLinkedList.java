import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
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

    public static void main(String[] args) {
        IUDoubleLinkedList<Integer> IU = new IUDoubleLinkedList<>();
        IU.add(1);
        IU.add(2);
        IU.add(2, 3);
        IU.add(3, -1);
    }

    /* Empty List Constructor */
    public IUDoubleLinkedList() {
        head = null;
        tail = null;
        size = 0;
        modCount = 0;
    }

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

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int index = indexOf(target);

        if (index == NOT_FOUND) {
            throw new NoSuchElementException();
        }

        Node<T> newNode = new Node<>(element);
        Node<T> current = head;
        int count = 0;

        while (index != count) {
            current = current.getNext();
            count++;
        }

        newNode.setNext(current.getNext());
        newNode.setPrevious(current);
        current.setNext(newNode);

        if (current == tail) {
            tail = newNode;
        }

        size++;
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> newNode = new Node<>(element);
        Node<T> current = head;
        int count = 0;

        while (index != count) {
            current = current.getNext();
            count++;
        }

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        } else if (current == null) {
            newNode.setPrevious(tail);
            tail.setNext(newNode);
            tail = newNode;
        } else {
            newNode.setNext(current);
            newNode.setPrevious(current.getPrevious());

            if (current == head) {
                head = newNode;
            } else {
                current.getPrevious().setNext(newNode);
            }

            current.setPrevious(newNode);
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

    @Override
    public T remove(T element) {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        boolean found = false;
        Node<T> current = head;

        while (current != null && !found) {
            if (element.equals(current.getElement())) {
                found = true;
            } else {
                current = current.getNext();
            }
        }

        if (!found) {
            throw new NoSuchElementException();
        }

        if (size() == 1) {
            head = null;
            tail = null;
        } else if (current == head) {
            head = current.getNext();
            head.setPrevious(null);
        } else if (current == tail) {
            tail = current.getPrevious();
            tail.setNext(null);
        } else {
            current.getNext().setPrevious(current.getPrevious());
            current.getPrevious().setNext(current.getNext());
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

        Node<T> current = head;
        int count = 0;

        while (index != count) {
            current = current.getNext();
            count++;
        }

        if (size() == 1) {
            head = null;
            tail = null;
        } else if (current == head) {
            head = current.getNext();
            head.setPrevious(null);
        } else if (current == tail) {
            tail = current.getPrevious();
            tail.setNext(null);
        } else {
            current.getNext().setPrevious(current.getPrevious());
            current.getPrevious().setNext(current.getNext());
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
        int i = 0;

        while (index != i) {
            current = current.getNext();
            i++;
        }

        current.setElement(element);
        modCount++;
    }

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

        Node<T> current = head;
        int count = 0;

        while (index != count) {
            current = current.getNext();
            count++;
        }

        return current.getElement();
    }

    @Override
    public int indexOf(T element) {
        if (size() == 0) {
            return NOT_FOUND;
        }

        Node<T> current = head;
        int index = 0;

        while (current != null) {
            if (element.equals(current.getElement())) {
                return index;
            }

            current = current.getNext();
            index++;
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
        if (isEmpty()) {
            return false;
        }

        Node<T> current = head;

        while (current != null) {
            if (target.equals(current.getElement())) {
                return true;
            }

            current = current.getNext();
        }

        return false;
    }

    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }

    @Override
    public int size() {
        return size;
    }

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

    @Override
    public Iterator<T> iterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DLListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLListIterator(startingIndex);
    }

    private class DLLIterator implements Iterator<T> {
        private Node<T> nextNode;
        private int iterModCount;
        private T lastRet;
        private int index;

        public DLLIterator() {
            nextNode = head;
            iterModCount = modCount;
            lastRet = null;
            index = -1;
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

            index++;
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

            IUDoubleLinkedList.this.remove(index);
            index--;
            iterModCount++;
            lastRet = null;
        }
    }

    private class DLListIterator implements ListIterator<T> {
        private Node<T> nextNode;
        private Node<T> prevNode;
        private T lastRet;
        private int index;
        private int iterModCount;

        public DLListIterator() {
            nextNode = head;
            prevNode = null;
            lastRet = null;
            index = -1;
            iterModCount = modCount;
        }

        public DLListIterator(int startingIndex) {
            nextNode = head;
            prevNode = null;
            index = -1;

            while (index != startingIndex) {
                prevNode = nextNode;
                nextNode = nextNode.getNext();
                index++;
            }

            lastRet = null;
            iterModCount = modCount;
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

            lastRet = nextNode.getElement();
            prevNode = nextNode;
            nextNode = nextNode.getNext();
            index++;
            return lastRet;
        }

        @Override
        public boolean hasPrevious() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return !(isEmpty() || prevNode == null);
        }

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
            index--;
            return null;
        }

        @Override
        public int nextIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return (index + 1);
        }

        @Override
        public int previousIndex() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return (index - 1);
        }

        @Override
        public void remove() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (lastRet == null) {
                throw new IllegalStateException();
            }

            if (size() == 1) {
                head = null;
                tail = null;
            } else if (lastRet.equals(prevNode.getElement())) {

            } else if (lastRet.equals(nextNode.getElement())) {

            }

            index--;
            size--;
            iterModCount++;
            modCount++;
        }

        @Override
        public void set(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (lastRet != null) {
                throw new IllegalStateException();
            }

            prevNode.setElement(e);
            iterModCount++;
            modCount++;
        }

        @Override
        public void add(T e) {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            Node<T> newNode = new Node<>(e);

            if (isEmpty()) {
                head = newNode;
                tail = newNode;
            } else if (prevNode == null) {
                newNode.setNext(head);
                head.setPrevious(newNode);
                head = newNode;
            } else if (nextNode == null) {
                newNode.setPrevious(tail);
                tail.setNext(newNode);
                tail = newNode;
            } else {
                prevNode.setNext(newNode);
                nextNode.setPrevious(newNode);
                prevNode = newNode;
            }

            size++;
            index++;
            iterModCount++;
            modCount++;
        }
    }
}