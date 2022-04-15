public class QuickSort<T> {
    DLL<T> list;

    public QuickSort(DLL<T> list) {
        this.list = list;
    }

    public static void main(String[] args) {

    }

    public DLL<T> Sort(DLL<T> list) {
        Node<T> pivot = list.getHead();
        DLL<T> sorted = new DLL<>();
        DLL<T> before = new DLL<>();
        DLL<T> after = new DLL<>();

        if (list.getHead() == list.getTail()) {
            return list;
        }

        Node<T> current = pivot.getNext();

        while (current != null) {
            if (pivot.getElement().compareTo(current.getElement())) {

            }
        }

        sorted.setHead(before.getHead());
        Node<T> beforeTail = sorted.getTail();
        beforeTail.setNext(pivot);
        pivot.setPrevious(beforeTail);
        pivot.setNext(after.getHead());
        after.getHead().setPrevious(pivot);
        sorted.setTail(after.getTail());
        return sorted;
    }
}