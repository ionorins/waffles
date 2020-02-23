// package uk.ac.warwick.cs126.structures;

public class MyList<E> {
    private MyListNode<E> head;

    public MyList() {
        head = null;
    }

    public void add(E value) {
        MyListNode<E> node = new MyListNode<E>(value);
        node.setNext(head);
        head = node;
    }

    public MyListNode<E> getHead() {
        return this.head;
    }

    public String toString() {
        if (head == null)
            return "[]";

        String res = "[";

        MyListNode<E> aux = head;

        while (aux.getNext() != null) {
            res += aux.toString() + ", ";
            aux = aux.getNext();
        }

        res += aux.toString() + "]";

        return res;
    }
}