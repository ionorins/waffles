package uk.ac.warwick.cs126.structures;

/**
 * static class that contains usefull algorithms
 */
public class Algorithms {
    /**
     * Sorts array in place using quicksort
     *
     * @param <T>  type of array
     * @param arr  array to be sorted
     * @param comp comparator function
     */
    public static <T> void sort(T[] arr, Lambda<T> comp) {
        sort(arr, comp, 0, arr.length - 1);
    }

    /**
     * Designates the element with index low as the pivot and moves all elements
     * smaller than it to its left
     *
     * @param <T>  type of array
     * @param arr  array to be sorted
     * @param comp comparator function
     * @param low  smallest index of the partition
     * @param high highest index of the partition
     * @return index of pivot
     */
    private static <T> int part(T[] arr, Lambda<T> comp, int low, int high) {
        int i = low, j = high, difi = 0, difj = -1;

        while (i < j) {
            if (comp.call(arr[i], arr[j]) > 0) {
                T aux = arr[i];
                arr[i] = arr[j];
                arr[j] = aux;

                if (difi == 1) {
                    difi = 0;
                    difj = -1;
                } else {
                    difi = 1;
                    difj = 0;
                }
            }

            i += difi;
            j += difj;
        }

        return i;
    }

    /**
     * Body of the quicksort method
     *
     * @param <T>  type of the array
     * @param arr  array to be sorted
     * @param comp comparator function
     * @param low  smallest index to be sorted
     * @param high biggest index to be sorted
     */
    private static <T> void sort(T[] arr, Lambda<T> comp, int low, int high) {
        if (low < high) {
            int p = part(arr, comp, low, high);
            sort(arr, comp, low, p - 1);
            sort(arr, comp, p + 1, high);
        }
    }
}