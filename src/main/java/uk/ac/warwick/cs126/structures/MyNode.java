package uk.ac.warwick.cs126.structures;

public class MyNode<K, V> {
    private MyNode<K, V> left, right;
    private K key;
    private V value;
    private int height;

    MyNode(K key, V value) {
        left = right = null;
        this.key = key;
        this.value = value;
        this.height = 1;
    }

    public MyNode<K, V> getLeft() {
        return this.left;
    }

    public MyNode<K, V> getRight() {
        return this.right;
    }

    public void setLeft(MyNode<K, V> node) {
        this.left = node;
    }

    public void setRight(MyNode<K, V> node) {
        this.right = node;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int updateHeight() {
        int l = this.getLeft() == null ? 0 : this.getLeft().getHeight();
        int r = this.getRight() == null ? 0 : this.getRight().getHeight();

        height = 1 + (l > r ? l : r);

        return l - r;
    }
}