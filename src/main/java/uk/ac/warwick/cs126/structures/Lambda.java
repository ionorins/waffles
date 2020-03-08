package uk.ac.warwick.cs126.structures;

/**
 * Interface for comparator functions
 *
 * @param <E> type to be compared
 */
public interface Lambda<E> {
    /**
     * The body of the comparator function
     *
     * @param a first element to be compared
     * @param b second element to be compared
     * @return 0 if a == b, < 0 if a < b, > 0 if a > b
     */
    public int call(E a, E b);
}