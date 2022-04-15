public class DLL<T> {
    Node<T> head = null;
    Node<T> tail = null;

    public DLL() {
        
    }

    public DLL(Node<T> head) {
        this.head = head;
        Node<T> current = head;

        while (current.getNext() != null) {
            current = current.getNext();
            this.tail = current;
        }
    }

    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }

    public void setHead(Node<T> head) {
        this.head = head;
    }

    public void setTail(Node<T> tail) {
        this.tail = tail;
    }
}
