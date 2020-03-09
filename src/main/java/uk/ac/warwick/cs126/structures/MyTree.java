package uk.ac.warwick.cs126.structures;

/**
 * Implementation of AVL tree
 *
 * @param <K> type of key
 * @param <V> type of value
 */
public class MyTree<K extends Comparable<K>, V> {
    private MyNode<K, V> root;

    public MyTree() {
        this.root = null;
    }

    /**
     * Search tree for key
     *
     * @param key  key to be searched for
     * @param node root of the tree to be searched
     * @return the value of the node with the search key if exists, otherwise null
     */
    private V search(K key, MyNode<K, V> node) {
        if (node == null)
            return null;
        if (key.equals(node.getKey()))
            return node.getValue();
        if (key.compareTo(node.getKey()) < 0)
            return search(key, node.getLeft());
        return search(key, node.getRight());
    }

    /**
     * Rotates tree to the right
     *
     * @param y root of the tree to be rotated
     * @return new root of the rotated tree
     */
    private MyNode<K, V> rotateRight(MyNode<K, V> y) {
        MyNode<K, V> x = y.getLeft();
        MyNode<K, V> t2 = x.getRight();

        x.setRight(y);
        y.setLeft(t2);

        y.updateHeight();
        x.updateHeight();

        return x;
    }

    /**
     * Rotates tree to the left
     *
     * @param x root of the tree to be rotated
     * @return new root of the rotated tree
     */
    private MyNode<K, V> rotateLeft(MyNode<K, V> x) {
        MyNode<K, V> y = x.getRight();
        MyNode<K, V> t2 = y.getLeft();

        y.setLeft(x);
        x.setRight(t2);

        y.updateHeight();
        x.updateHeight();

        return y;
    }

    /**
     * Returns inorder traversal containing values of the tree
     *
     * @param arr  array which will contain the traversal
     * @param node root of the tree to be traversed
     */
    private void toArrayList(MyArrayList<V> arr, MyNode<K, V> node) {
        if (node == null)
            return;
        toArrayList(arr, node.getLeft());
        arr.add(node.getValue());
        toArrayList(arr, node.getRight());
    }

    /**
     * Returns inorder traversal containing keys of the tree
     *
     * @param arr  array which will contain the traversal
     * @param node root of the tree to be traversed
     */
    private void toArrayListOfKeys(MyArrayList<K> arr, MyNode<K, V> node) {
        if (node == null)
            return;
        toArrayListOfKeys(arr, node.getLeft());
        arr.add(node.getKey());
        toArrayListOfKeys(arr, node.getRight());
    }

    /**
     * Returns value of node with specified key
     *
     * @param key key of the node to be searched of
     * @return value key of the node to be searched of
     */
    public V search(K key) {
        return search(key, root);
    }

    /**
     * Checks if tree contains key
     *
     * @param key key to be searched for
     * @return true if key is found, false otherwise
     */
    public boolean contains(K key) {
        return search(key) != null;
    }

    /**
     * Inserts new node into tree
     *
     * @param key key of node to be inserted
     * @param value value of node to be inserted
     * @param node root of tree in which new node will be inserted
     * @return root of new tree
     * @throws IllegalArgumentException if key is already in tree
     */
    private MyNode<K, V> insert(K key, V value, MyNode<K, V> node) throws IllegalArgumentException {
        // insert node if at bottom of the tree
        if (node == null)
            return new MyNode<K, V>(key, value);

        // throw error if key already exists
        if (key.compareTo(node.getKey()) == 0)
            throw new IllegalArgumentException("Key '" + node.getKey() + "' already inserted");

        // search place to insert node
        if (key.compareTo(node.getKey()) < 0)
            node.setLeft(insert(key, value, node.getLeft()));
        else
            node.setRight(insert(key, value, node.getRight()));

        // balance tree
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

    /**
     * Adds node to the tree
     *
     * @param key   key of the new node
     * @param value value of the new node
     * @throws IllegalArgumentException if key is already in tree
     */
    public void add(K key, V value) throws IllegalArgumentException {
        root = this.insert(key, value, root);
    }

    /**
     * Removes node from tree
     *
     * @param key key of node to be removed
     * @param node root of tree from wich node will be removed
     * @return root of tree with removed node
     */
    private MyNode<K, V> remove(K key, MyNode<K, V> node) {
        // node not found
        if (node == null)
            return node;

        // search node to remove
        if (key.compareTo(node.getKey()) < 0)
            node.setLeft(remove(key, node.getLeft()));

        else if (key.compareTo(node.getKey()) > 0)
            node.setRight(remove(key, node.getRight()));

        // node found
        else {
            // trivial case when at least one of the children is a leaf
            if (node.getLeft() == null || node.getRight() == null) {
                if (node.getLeft() == null && node.getRight() == null)
                    node = null;
                else if (node.getLeft() != null)
                    node = node.getLeft();
                else
                    node = node.getRight();
            } else {
                // find inorder successor of removed node
                MyNode<K, V> aux = node.getRight();

                while (aux.getLeft() != null)
                    aux = aux.getLeft();

                // replace node with successor
                node.setKey(aux.getKey());
                node.setValue(aux.getValue());

                // remove successor
                node.setRight(remove(aux.getKey(), node.getRight()));
            }
        }

        if (node == null)
            return node;

        // balance tree
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

    /**
     * Removes node from tree
     *
     * @param key key of the node to be removed
     */
    public void remove(K key) {
        root = remove(key, root);
    }

    /**
     * Returns inorder traversal containing values of the tree
     *
     * @return inorder traversal containing values of the tree
     */
    public MyArrayList<V> toArrayList() {
        MyArrayList<V> arr = new MyArrayList<V>();
        toArrayList(arr, root);
        return arr;
    }

    /**
     * Returns inorder traversal containing keys of the tree
     *
     * @return inorder traversal containing keys of the tree
     */
    public MyArrayList<K> toArrayListofKeys() {
        MyArrayList<K> arr = new MyArrayList<K>();
        toArrayListOfKeys(arr, root);
        return arr;
    }

    public String toString() {
        return this.toArrayList().toString();
    }
}