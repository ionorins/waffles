package uk.ac.warwick.cs126.structures;

public class MyHashtable<K, V> {
    private MyList<MyKeyValuePair<K, V>>[] array;
    private int size;
    private int arraySize;

    public MyHashtable() {
        this.size = 0;
        this.arraySize = 11;
        this.array = new MyList[this.arraySize];

        for (int i = 0; i < arraySize; i++)
            this.array[i] = new MyList<MyKeyValuePair<K, V>>();
    }

    private int getHash(K obj) {
        int code = obj.hashCode() % arraySize;
        return code < 0 ? -code : code;
    }

    public void add(K key, V value) {
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

        array[getHash(key)].add(new MyKeyValuePair<K, V>(key, value));
        size++;

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

    public V get(K key) {
        MyListNode<MyKeyValuePair<K, V>> node = array[getHash(key)].getHead();
        if (node == null)
            return null;

        // System.err.println(getHash(key));

        while (!key.equals(node.getValue().getKey())) {
            node = node.getNext();
            if (node == null)
                return null;
        }

        return node.getValue().getValue();
    }

    public Boolean contains(K key) {
        return this.get(key) != null;
    }

    public String toString() {
        String res = "";

        for (Integer i = 0; i < array.length; i++)
            res += i.toString() + ": " + array[i].toString() + "\n";

        return res;
    }
}