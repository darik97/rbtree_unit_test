import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedBlackTreeTest {
    @Test
    public void checkEmptyTree() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        assertEquals(0, tree.getHeight(tree.root));
        assertTrue(tree.isEmpty());
    }

    @Test
    public void checkInsert() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        tree.insert(50);
        assertTrue(!tree.isEmpty());
        assertTrue(tree.getHeight(tree.root) > 0);
        assertEquals(1, tree.getSize());
        tree.insert(150);
        tree.insert(110);
        tree.insert(30);
        tree.insert(15);
        assertEquals(5, tree.getSize());
    }

    @Test
    public void checkStringRBTree() {
        RedBlackTree<String> tree = new RedBlackTree<>();
        String[] rbValues = { "a", "z", "b"};
        for (String value: rbValues) {
            tree.insert(value);
        }
        assertTrue(!tree.isEmpty());
        assertEquals(3, tree.getSize());
        assertEquals("a", tree.find("a"));
        assertEquals(null, tree.find("A"));
    }

    @Test
    public void checkClearTree() {
        RedBlackTree<Integer> tree = getIntegerTree();
        tree.clear();
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.getHeight(tree.root));
    }

    @Test
    public void checkFindNode() {
        RedBlackTree<Integer> tree = getIntegerTree();
        assertEquals(Integer.valueOf(50), tree.find(50));
    }

    @Test
    public void checkFindNone() {
        RedBlackTree<Integer> tree = getIntegerTree();
        assertEquals(null, tree.find(510));
    }

    @Test
    public void checkMinNode() {
        RedBlackTree<Integer> tree = getIntegerTree();
        assertEquals(Integer.valueOf(5), tree.minimum(tree.root).getValue());
    }

    @Test
    public void checkMaxNode() {
        RedBlackTree<Integer> tree = getIntegerTree();
        assertEquals(Integer.valueOf(150), tree.maximum(tree.root).getValue());
    }

    private RedBlackTree<Integer> getIntegerTree() {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        Integer[] rbValues = { 100, 50, 150, 40, 10, 5};
        for (Integer value: rbValues) {
            tree.insert(value);
        }
        return tree;
    }

}