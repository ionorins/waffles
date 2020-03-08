package uk.ac.warwick.cs126.structures;

public class MyTree<K extends Comparable<K>, V> {
    private MyNode<K, V> root;

    public MyTree() {
        this.root = null;
    }

    public MyNode<K, V> getSmallest() {
        MyNode<K, V> ret = this.root;
        while (ret.getLeft() != null)
            ret = ret.getLeft();
        return ret;
    }

    public MyNode<K, V> getBiggest() {
        MyNode<K, V> ret = this.root;
        while (ret.getRight() != null)
            ret = ret.getRight();
        return ret;
    }

    private V search(K key, MyNode<K, V> node) {
        if (node == null)
            return null;
        if (key.equals(node.getKey()))
            return node.getValue();
        if (key.compareTo(node.getKey()) < 0)
            return search(key, node.getLeft());
        return search(key, node.getRight());
    }

    private MyNode<K, V> rotateRight(MyNode<K, V> y) {
        // System.err.println(y.getValue().toString());
        MyNode<K, V> x = y.getLeft();
        MyNode<K, V> t2 = x.getRight();

        x.setRight(y);
        y.setLeft(t2);

        y.updateHeight();
        x.updateHeight();

        return x;
    }

    private MyNode<K, V> rotateLeft(MyNode<K, V> x) {
        MyNode<K, V> y = x.getRight();
        MyNode<K, V> t2 = y.getLeft();

        y.setLeft(x);
        x.setRight(t2);

        y.updateHeight();
        x.updateHeight();

        return y;
    }

    private void toArrayList(MyArrayList<V> arr, MyNode<K, V> node) {
        if (node == null)
            return;
        toArrayList(arr, node.getLeft());
        arr.add(node.getValue());
        toArrayList(arr, node.getRight());
    }

    private void toArrayListOfKeys(MyArrayList<K> arr, MyNode<K, V> node) {
        if (node == null)
            return;
        toArrayListOfKeys(arr, node.getLeft());
        arr.add(node.getKey());
        toArrayListOfKeys(arr, node.getRight());
    }

    public V search(K key) {
        return search(key, root);
    }

    public boolean contains(K key) {
        return search(key) != null;
    }

    private MyNode<K, V> insert(K key, V value, MyNode<K, V> node) throws IllegalArgumentException {
        if (node == null)
            return new MyNode<K, V>(key, value);

        if (key.compareTo(node.getKey()) == 0)
            throw new IllegalArgumentException("Key '" + node.getKey() + "' already inserted");

        if (key.compareTo(node.getKey()) < 0)
            node.setLeft(insert(key, value, node.getLeft()));
        else
            node.setRight(insert(key, value, node.getRight()));

        int dif = node.updateHeight();

        if (dif > 1 && node.getLeft().updateHeight() >= 0)
            return rotateRight(node);

        if (dif < -1 && node.getRight().updateHeight() <= 0)
            return rotateLeft(node);

        if (dif > 1 && node.getLeft().updateHeight() < 0) {
            node.setLeft(rotateLeft(node.getLeft()));
            return rotateRight(node);
        }

        if (dif < -1 && node.getRight().updateHeight() > 0) {
            node.setRight(rotateRight(node.getRight()));
            return rotateLeft(node);
        }

        return node;
    }

    public void add(K key, V value) throws IllegalArgumentException {
        root = this.insert(key, value, root);
    }

    private MyNode<K, V> remove(K key, MyNode<K, V> node) {
        // STEP 1: PERFORM STANDARD BST DELETE
        if (node == null)
            return node;

        // If the key to be deleted is smaller than
        // the root's key, then it lies in left subtree
        if (key.compareTo(node.getKey()) < 0)
            node.setLeft(remove(key, node.getLeft()));

        // If the key to be deleted is greater than the
        // root's key, then it lies in right subtree
        else if (key.compareTo(node.getKey()) > 0)
            node.setRight(remove(key, node.getRight()));

        // if key is same as root's key, then this is the node
        // to be deleted
        else {

            // node with only one child or no child
            if (node.getLeft() == null || node.getRight() == null) {
                if (node.getLeft() == null && node.getRight() == null)
                    node = null;
                else if (node.getLeft() != null)
                    node = node.getLeft();
                else
                    node = node.getRight();
            } else {

                // node with two children: Get the inorder
                // successor (smallest in the right subtree)
                MyNode<K, V> aux = node.getRight();

                while (aux.getLeft() != null)
                    aux = aux.getLeft();

                // Copy the inorder successor's data to this node
                node.setKey(aux.getKey());
                node.setValue(aux.getValue());

                // Delete the inorder successor
                node.setRight(remove(aux.getKey(), node.getRight()));
            }
        }

        // If the tree had only one node then return
        if (node == null)
            return node;

        int dif = node.updateHeight();

        if (dif > 1 && node.getLeft().updateHeight() >= 0)
            return rotateRight(node);

        if (dif < -1 && node.getRight().updateHeight() <= 0)
            return rotateLeft(node);

        if (dif > 1 && node.getLeft().updateHeight() < 0) {
            node.setLeft(rotateLeft(node.getLeft()));
            return rotateRight(node);
        }

        if (dif < -1 && node.getRight().updateHeight() > 0) {
            node.setRight(rotateRight(node.getRight()));
            return rotateLeft(node);
        }

        return node;
    }

    public void remove(K key) {
        root = remove(key, root);
    }

    public MyArrayList<V> toArrayList() {
        MyArrayList<V> arr = new MyArrayList<V>();
        toArrayList(arr, root);
        return arr;
    }

    public MyArrayList<K> toArrayListofKeys() {
        MyArrayList<K> arr = new MyArrayList<K>();
        toArrayListOfKeys(arr, root);
        return arr;
    }

    public String toString() {
        return this.toArrayList().toString();
    }
}