public class Search {
    public static void main(String[] args) {
        Character[] sorted = new Character[]{'A', 'B', 'C', 'D', 'E'};
        binarySearch(sorted, 'E');
    }

    public static <T extends Comparable<T>> boolean binarySearch(T[] list, T target) {
        boolean found = false;
        int left = 0;
        int right = list.length;
        int mid = (left + right) / 2;

        while (left <= right && !found) {
            mid = (left + right) / 2;

            if (target.compareTo(list[mid]) == 0) {
                found = true;
            } else if (target.compareTo(list[mid]) > 0) {
                left = mid;
            } else {
                right = mid;
            }
        }

        System.out.printf("Index: %d\n", mid);
        return found;
    }


}