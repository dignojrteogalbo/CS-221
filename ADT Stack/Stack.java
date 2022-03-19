public class Stack<T> {
    private Node<T> head = null;
    private Node<T> tail = null;
    private int size = 0;

    public static void main(String[] args) {
        Node<Integer> three = new Node<>(3, null);
        Node<Integer> two = new Node<>(2, three);
        Node<Integer> one = new Node<>(1, two);

        Stack<Integer> stack = new Stack<>(one);
        System.out.println(stack.toString());
    }

    public Stack() {
        head = null;
        tail = null;
        size = 0;
    }

    public Stack(Node<T> head) {
        size = 1;
        this.head = head;

        Node<T> current = this.head;
        while (current.getNext() != null) {
            size++;
            current = current.getNext();
        }

        tail = current;
    }

    public void push(T value) {
        Node<T> newNode = new Node<T>(value);
        newNode.setNext(head);
        head = newNode;
        size++;

        if (tail == null) {
            tail = newNode;
        }
    }

    public T pop() {
        T value = head.getValue();
        head = head.getNext();
        size--;

        return value;
    }

    public T peek() {
        return (head == null) ? null : head.getValue();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (tail == null);
    }

    public String toString() {
        String output = "[";
        Node<T> current = head;

        while (current != null) {
            T value = current.getValue();
            output += String.format("\"%s\"%s", value.toString(), current.getNext() == null ? "" : ",");
            current = current.getNext();
        }

        output += "]";
        return output;
    }
}
