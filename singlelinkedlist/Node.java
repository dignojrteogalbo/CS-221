public class Node<E> {
    private E element;
    private Node<E> next = null;

    public Node(E element) {
        this.element = element;
    }

    public Node(E element, Node<E> next) {
        this.element = element;
        this.next = next;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    public Node<E> getNext() {
        return (next != null) ? next : null;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

    public String toString() {
        return "Element: " + element.toString() + " Has next: " + (next != null);
    }
}
