package uk.ac.warwick.cs126.structures;

/**
 * MyListNode
 */
public class MyListNode<E> {
    private E value;
    private MyListNode<E> next;

    public MyListNode(E value) {
        this.value = value;
    }

    public E getValue() {
        return this.value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public MyListNode<E> getNext() {
        return this.next;
    }

    public void setNext(MyListNode<E> next) {
        this.next = next;
    }

    public String toString() {
        return this.getValue().toString();
    }
}