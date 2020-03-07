package uk.ac.warwick.cs126.structures;

import uk.ac.warwick.cs126.util.StringFormatter;

public class Test {
    public static void main(String[] args) {
        MyTree<Integer, String> tree = new MyTree<Integer, String>();
        for (Integer i = 0; i < 100; i++)
            tree.add(i, i.toString());

        System.out.println(tree.root.getHeight());
    }
}