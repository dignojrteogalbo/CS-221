/**
 * Merge sort implementation for integer arrays created
 * from algorithm made in CS 221 
 * 
 * @author Digno JR Teogalbo
 */
public class MergeSort {
    public static void main(String[] args) {
        MergeSort sorter = new MergeSort();

        int[] myList = new int[]{1, 5, 3, 4, 2};
        int[] sortedList = mergeSort(myList);
    }

    public static int[] mergeSort(int[] list) {
        if (list.length == 1) {
            return list;
        }

        int firstLength = list.length / 2;
        int secondLength = (list.length % 2 == 0) ? firstLength : firstLength + 1;
        int[] list1 = new int[firstLength];
        int[] list2 = new int[secondLength];
        int j = 0;  // list1 index counter
        int k = 0;  // list2 index counter

        for (int i = 0; i < list.length; i++) {
            if (i % 2 == 0) {
                list2[j] = list[i];
                j++;
            } else {
                list1[k] = list[i];
                k++;
            }
        }

        return merge(mergeSort(list1), mergeSort(list2));
    }

    public static int[] merge(int[] list1, int[] list2) {
        int n = list1.length;   // size of list1
        int m = list2.length;   // size of list2
        int[] list = new int[n + m];
        int j = 0;  // list1 index counter
        int k = 0;  // list2 index counter

        for (int i = 0; i < list.length; i++) {
            if (j >= n) {           // when list2 is empty
                list[i] = list2[k];
                k++;
            } else if (k >= m) {    // when list2 is empty
                list[i] = list1[j];
                j++;
            } else {
                if (list1[j] <= list2[k]) {
                    list[i] = list1[j];
                    j++;
                } else {
                    list[i] = list2[k];
                    k++;
                }
            }
        }

        return list;
    }
}