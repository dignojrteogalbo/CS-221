import java.util.Comparator;

/**
 * Class for sorting lists that implement the IndexedUnsortedList interface,
 * using ordering defined by class of objects in list or a Comparator.
 * As written uses Mergesort algorithm.
 *
 * @author CS221, Digno JR Teogalbo
 * @version CS 221-002 Spring 2022
 */
public class Sort
{
	/**
	 * Returns a new list that implements the IndexedUnsortedList interface. 
	 * As configured, uses WrappedDLL. Must be changed if using 
	 * your own IUDoubleLinkedList class. 
	 * 
	 * @return a new list that implements the IndexedUnsortedList interface
	 */
	private static <T> IndexedUnsortedList<T> newList() 
	{
		return new IUDoubleLinkedList<>(); //TODO: replace with your IUDoubleLinkedList for extra-credit
	}
	
	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @see IndexedUnsortedList 
	 */
	public static <T extends Comparable<T>> void sort(IndexedUnsortedList<T> list) 
	{
		mergesort(list);
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using given Comparator.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 * @see IndexedUnsortedList 
	 */
	public static <T> void sort(IndexedUnsortedList <T> list, Comparator<T> c) 
	{
		mergesort(list, c);
	}
	
	/**
	 * Mergesort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface, 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 */
	private static <T extends Comparable<T>> void mergesort(IndexedUnsortedList<T> list)
	{
		if (list.size() <= 1) 
			return;

		IndexedUnsortedList<T> list1 = newList();
		IndexedUnsortedList<T> list2 = newList();

		while (!list.isEmpty()) {
			T firstElement = list.removeFirst();
			list1.add(firstElement);

			if (!list.isEmpty()) {
				T lastElement = list.removeLast();
				list2.add(lastElement);
			}
		}

		mergesort(list1);
		mergesort(list2);
		merge(list, list1, list2);
	}

	/**
	 * Merge function to sort and add two lists to the original list.
	 * compareTo() method is used to compare the values of elements.
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be merged, implements IndexedUnsortedList interface 
	 * @param list1
	 *            The first half of list, implements IndexedUnsortedList interface 
	 * @param list2
	 *            The second half of list, implements IndexedUnsortedList interface 
	 */
	private static <T extends Comparable<T>> void merge(IndexedUnsortedList<T> list, IndexedUnsortedList<T> list1, IndexedUnsortedList<T> list2)
	{
		while (!list1.isEmpty() || !list2.isEmpty()) {
			if (list1.isEmpty()) {
				list.add(list2.removeFirst());
			} else if (list2.isEmpty()) {
				list.add(list1.removeFirst());
			} else {
				T element = (list1.first().compareTo(list2.first()) <= 0) ? list1.removeFirst() : list2.removeFirst();
				list.add(element);
			}
		}
	}

	/**
	 * Mergesort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface,
	 * using the given Comparator.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 */
	private static <T> void mergesort(IndexedUnsortedList<T> list, Comparator<T> c)
	{
		if (list.size() <= 1)
			return;

		IndexedUnsortedList<T> list1 = newList();
		IndexedUnsortedList<T> list2 = newList();

		while (list.size() != 0) {
			T firstElement = list.removeFirst();
			list1.add(firstElement);

			if (list.size() != 0) {
				T lastElement = list.removeLast();
				list2.add(lastElement);
			}
		}

		mergesort(list1, c);
		mergesort(list2, c);
		merge(list, list1, list2, c);
	}

	/**
	 * Merge function to sort and add two lists to the original list.
	 * Comparator is used to compare the values of elements.
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be merged, implements IndexedUnsortedList interface 
	 * @param list1
	 *            The first half of list, implements IndexedUnsortedList interface 
	 * @param list2
	 *            The second half of list, implements IndexedUnsortedList interface 
	 */
	private static <T> void merge(IndexedUnsortedList<T> list, IndexedUnsortedList<T> list1, IndexedUnsortedList<T> list2, Comparator<T> c) {
		while (list1.size() != 0 || list2.size() != 0) {
			if (list1.size() == 0) {
				list.add(list2.removeFirst());
			} else if (list2.size() == 0) {
				list.add(list1.removeFirst());
			} else {
				T element = (c.compare(list1.first(), list2.first()) <= 0) ? list1.removeFirst() : list2.removeFirst();
				list.add(element);
			}
		}
	}
}
