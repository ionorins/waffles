// package uk.ac.warwick.cs126.structures;

public class Test {
    public static void main(String[] args) {
        MyTree<Integer, String> tree = new MyTree<Integer, String>();
        try {
        tree.insert(0, "0");
        tree.insert(-1, "-1");
        tree.insert(1, "2");
        tree.insert(2, "1");
        tree.remove(1);
        } catch (IllegalArgumentException e){
            System.out.println(e.toString());
        }
        // tree.insert(2, "c");
        System.out.println(tree.toString());
    }
}