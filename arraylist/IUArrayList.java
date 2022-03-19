import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array-based implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author
 *
 * @param <T> type to store
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int NOT_FOUND = -1;

    private T[] array;
    private int rear;
    private int modCount;

    /** Creates an empty list with default initial capacity */
    public IUArrayList() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates an empty list with the given initial capacity
     * 
     * @param initialCapacity
     */
    @SuppressWarnings("unchecked")
    public IUArrayList(int initialCapacity) {
        array = (T[]) (new Object[initialCapacity]);
        rear = 0;
        modCount = 0;
    }

    /** Double the capacity of array */
    private void expandCapacity() {
        if (rear == array.length - 1) {
            array = Arrays.copyOf(array, array.length * 2);
        }
    }

    @Override
    public void addToFront(T element) {
        expandCapacity();

        for (int i = rear; i >= 0; i--) {
            array[i+1] = array[i];
        }

        array[0] = element;
        rear++;
        modCount++;
    }

    @Override
    public void addToRear(T element) {
        expandCapacity();

        array[rear] = element;
        rear++;
        modCount++;
    }

    @Override
    public void add(T element) {
        addToRear(element);
    }

    @Override
    public void addAfter(T element, T target) {
        if (!contains(target)) {
            throw new NoSuchElementException();
        }

        expandCapacity();
        int index = indexOf(target);
        index++;

        for (int i = rear; i > index; i--) {
            array[i] = array[i-1];
        }

        array[index] = element;
        rear++;
        modCount++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        } 

        expandCapacity();

        for (int i = rear; i > index; i--) {
            array[i] = array[i - 1];
        }

        array[index] = element;
        rear++;
        modCount++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        T value = array[0];

        for (int i = 0; i < rear; i++)
        {
            array[i] = array[i+1];
        }

        rear--;
        modCount++;
        return value;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        rear--;
        T value = array[rear];
        array[rear] = null;
        modCount++;
        
        return value;
    }

    @Override
    public T remove(T element) {
        int index = indexOf(element);
        if (index == NOT_FOUND) {
            throw new NoSuchElementException();
        }

        T retVal = array[index];

        rear--;
        // shift elements
        for (int i = index; i < rear; i++) {
            array[i] = array[i + 1];
        }
        array[rear] = null;
        modCount++;

        return retVal;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        T value = array[index];

        for (int i = index; i < rear; i++) {
            array[i] = array[i+1];
        }

        array[rear] = null;
        rear--;
        modCount++;

        return value;
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        array[index] = element;
        modCount++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }

        return array[index];
    }

    @Override
    public int indexOf(T element) {
        int index = NOT_FOUND;

        if (!isEmpty()) {
            int i = 0;
            while (index == NOT_FOUND && i < rear) {
                if (element.equals(array[i])) {
                    index = i;
                } else {
                    i++;
                }
            }
        }

        return index;
    }

    @Override
    public T first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return array[0];
    }

    @Override
    public T last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        return array[rear-1];
    }

    @Override
    public boolean contains(T target) {
        return (indexOf(target) != NOT_FOUND);
    }

    @Override
    public boolean isEmpty() {
        return (rear == 0);
    }

    @Override
    public int size() {
        return rear;
    }

    public String toString() {
        StringBuilder output = new StringBuilder("[");

        for (int i = 0; i < rear; i++) {
            T value = array[i];
            String comma = (i == rear - 1) ? "" : ",";
            output.append("\"");
            output.append(value);
            output.append("\"");
            output.append(comma);
        }

        output.append("]");
        return output.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new ALIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException();
    }

    /** Iterator for IUArrayList */
    private class ALIterator implements Iterator<T> {
        private int nextIndex;
        private int iterModCount;
        private int lastRet;

        public ALIterator() {
            nextIndex = 0;
            iterModCount = modCount;
            lastRet = -1;
        }

        @Override
        public boolean hasNext() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            return !(isEmpty() || nextIndex == rear);
        }

        @Override
        public T next() {
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            T value = get(nextIndex);
            lastRet = nextIndex;
            nextIndex++;

            return value;
        }

        @Override
        public void remove() {
            if (lastRet == -1) {
                throw new IllegalStateException();
            }

            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            IUArrayList.this.remove(lastRet);
            iterModCount++;
            nextIndex--;
            lastRet = -1;
        }
    }
}
