package uk.ac.warwick.cs126.structures;

/**
 * Implementation of a hash table
 *
 * @param <K> type of key
 * @param <V> type of value
 */
public class MyHashtable<K, V> {
    private MyList<MyKeyValuePair<K, V>>[] array;
    private int size;
    private int arraySize;

    @SuppressWarnings("unchecked")
    public MyHashtable() {
        this.size = 0;
        this.arraySize = 11;
        this.array = new MyList[this.arraySize];

        for (int i = 0; i < arraySize; i++)
            this.array[i] = new MyList<MyKeyValuePair<K, V>>();
    }

    /**
     * Returns a hash for the object between 0 and arraySize
     *
     * @param obj object to be hashed
     * @return the hash of the object
     */
    private int getHash(K obj) {
        int code = obj.hashCode() % arraySize;
        return code < 0 ? -code : code;
    }

    /**
     * Add key-value pair to the hash table
     *
     * @param key   the key
     * @param value the value corresponding to the key
     */
    @SuppressWarnings("unchecked")
    public void add(K key, V value) {
        /**
         * remove key if already in table
         */
        MyListNode<MyKeyValuePair<K, V>> crt = array[getHash(key)].getHead();

        if (crt != null && crt.getValue().getKey().equals(key))
            array[getHash(key)].setHead(crt.getNext());
        while (crt != null && crt.getNext() != null) {
            if (crt.getNext().getValue().getKey().equals(key))
                crt.setNext(crt.getNext().getNext());
            crt = crt.getNext();
        }

        if (crt != null && crt.getNext() != null && crt.getNext().getValue().getKey().equals(key))
            crt.setNext(null);

        /**
         * Add key-value pair to the hash table
         */
        array[getHash(key)].add(new MyKeyValuePair<K, V>(key, value));
        size++;

        /**
         * If table is more than 50% full, duble its size
         */
        if ((float) size / arraySize > .5f) {
            size = 0;
            arraySize *= 2;
            MyList<MyKeyValuePair<K, V>>[] aux = array;
            array = new MyList[this.arraySize];

            for (int i = 0; i < arraySize; i++)
                this.array[i] = new MyList<MyKeyValuePair<K, V>>();

            for (MyList<MyKeyValuePair<K, V>> list : aux) {
                MyListNode<MyKeyValuePair<K, V>> node = list.getHead();
                while (node != null) {
                    this.add(node.getValue().getKey(), node.getValue().getValue());
                    node = node.getNext();
                }
            }
        }
    }

    /**
     * Returns the value corresponding to the key in the table
     *
     * @param key the key for which the value is retrieved
     * @return the value corresponding to the key in the table
     */
    public V get(K key) {
        MyListNode<MyKeyValuePair<K, V>> node = array[getHash(key)].getHead();
        if (node == null)
            return null;

        while (!key.equals(node.getValue().getKey())) {
            node = node.getNext();
            if (node == null)
                return null;
        }

        return node.getValue().getValue();
    }

    /**
     * Checks if key is present in the hash table
     *
     * @param key the key to be checked
     * @return true if the key is in the table, otherwise false
     */
    public Boolean contains(K key) {
        return this.get(key) != null;
    }

    /**
     * Returns a string representation of the table
     *
     * @return a string representation of the table
     */
    public String toString() {
        String res = "";

        for (Integer i = 0; i < array.length; i++)
            res += i.toString() + ": " + array[i].toString() + "\n";

        return res;
    }
}