// package uk.ac.warwick.cs126.structures;

public class Test {
    public static void main(String[] args) {
        // MyTree<Integer, String> tree = new MyTree<Integer, String>();
        // try {
        // tree.insert(0, "0");
        // tree.insert(-1, "-1");
        // tree.insert(1, "2");
        // tree.insert(2, "1");
        // tree.remove(1);
        // } catch (IllegalArgumentException e){
        // System.out.println(e.toString());
        // }
        // // tree.insert(2, "c");
        // System.out.println(tree.toString());

        // MyHashtable<String, Integer> table = new MyHashtable<String, Integer>();
        // for (Integer i = 0; i < 100; i++) {
        // table.add(i.toString(), i);
        // }

        // System.out.println(table.toString());

        // for (Integer i = 0; i < 100; i++) {
        // System.out.println(table.get(i.toString()));

        Integer[] arr = { 2, 3, 5, 6, 2, 1, 5 };
        Lambda<Integer> comp = new Lambda<Integer>() {
            @Override
            public int call(Integer a, Integer b) {
                return b - a;
            }
        };

        Algorithms.sort(arr, comp);

        for (Integer i : arr)
            System.out.println(i.toString());
        // System.out.println(arr.toString());
    }
}