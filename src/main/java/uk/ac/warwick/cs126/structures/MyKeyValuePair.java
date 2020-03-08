package uk.ac.warwick.cs126.structures;

public class MyKeyValuePair<K, V> {
    private K key;
    private V value;

    public MyKeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String toString() {
        return "{" + this.key.toString() + ": " + this.value.toString() + "}";
    }

}