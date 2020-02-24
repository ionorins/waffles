package uk.ac.warwick.cs126.structures;

public class Test {
    public static void main(String[] args) {
        MyTree<Integer, String> tree = new MyTree<Integer, String>();
        try {
            // tree.add(5, "5");
            // tree.add(6, "6");
            // tree.add(1, "1");
            // tree.add(4, "4");
            // tree.add(2, "2");
            // tree.add(3, "3");
            // tree.add(0, "0");
            // tree.add(-1, "-1");
            Integer[] a = {1, 3, 5, 234, 3425, 134124, 54, 1234, 1345534, 12341, 124654,  74545,63, 634, 3245, 546 , 23123476, 345};
            for (Integer i : a) {
                tree.add(i, i.toString());
            }

            // tree.remove(1);
        } catch (IllegalArgumentException e) {
            System.out.println(e.toString());
        }
        System.out.println(tree.toString());
        // System.out.println(tree.search(-1));
        // System.out.println(tree.search(0));
        // System.out.println(tree.search(1));
        // System.out.println(tree.search(2));
        // System.out.println(tree.search(3));
        // System.out.println(tree.search(4));
        // System.out.println(tree.search(5));
        // System.out.println(tree.search(6));
        // // tree.insert(2, "c");
        // System.out.println(tree.toString());

        // MyHashtable<String, Integer> table = new MyHashtable<String, Integer>();
        // for (Integer i = 0; i < 100; i++) {
        // table.add(i.toString(), i);
        // }

        // System.out.println(table.toString());

        // for (Integer i = 0; i < 100; i++) {
        // System.out.println(table.get(i.toString()));

        // Integer[] arr = { 2, 3, 5, 6, 2, 1, 5 };
        // Lambda<Integer> comp = new Lambda<Integer>() {
        // @Override
        // public int call(Integer a, Integer b) {
        // return b - a;
        // }
        // };

        // Algorithms.sort(arr, comp);

        // for (Integer i : arr)
        // System.out.println(i.toString());
        // System.out.println(arr.toString());
    }
}