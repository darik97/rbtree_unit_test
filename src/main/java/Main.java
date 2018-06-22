public class Main {

    public static void main(String[] args) {

        RedBlackTree<Integer> tree = new RedBlackTree<Integer>();
        Integer[] rbValues = { 100, 50, 150, 30, 60, 20, 40, 10, 5};
        for (Integer value: rbValues) {
            tree.insert(value);
        }

        System.out.print(tree);
    }
}
