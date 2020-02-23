// package uk.ac.warwick.cs126.structures;

/**
 * Algorithms
 */
public class Algorithms {
    public static <T> void sort(T[] arr, Lambda<T> comp) {
        sort(arr, comp, 0, arr.length - 1);
    }

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

    private static <T> void sort(T[] arr, Lambda<T> comp, int low, int high) {
        if (low < high) {
           int p = part(arr, comp, low, high);
           sort(arr, comp, low, p - 1);
           sort(arr, comp, p + 1, high);
        }
    }
}