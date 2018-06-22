import java.util.ArrayDeque;

public class RedBlackTree<V extends Comparable<V>> {

    protected RBNode<V> root;

    public RedBlackTree() {
        root = null;
    }

    public Boolean delete(Comparable value) {
        RBNode deleted = find(root, value);
        if(deleted == null) return false;
        if(deleted == root && deleted.isLeaf()) { // only root
            root = null;
        }
        else if(deleted.isLeaf())
            deleteLeaf(deleted);
        else if(deleted.getLeft() != null && deleted.getRight() != null && !(deleted.getLeft().isLeaf())
                && !(deleted.getRight().isLeaf()) || !(deleted.getRight().isLeaf())) { // deleted have a not leaf childs
            RBNode min = minimum(deleted.getRight());
            RBNode minParent = min.getParent();
            RBNode minRight = min.getRight();

            min.getParent().setLeft(minRight);
            min.setRight(null);
            if(min.isLeaf())
                deleteLeaf(min);
            if(minRight != null) {
                setEqualsColor(min, minRight);
            }
            min.setParent(null);
            setEqualsColor(deleted, min);
            fullTransplant(deleted, min);

            if(minParent == deleted) {
                min.setRight(minRight);
            }
        }
        else if(deleted.getLeft() != null && deleted.getRight() != null && deleted.isBlack()
                && deleted.getLeft().isBlack() && deleted.getRight().isBlack()) { // two black leaf childs
            RBNode bro = null;
            if(deleted.getParent() != null)
                bro = (deleted.isLeft()) ? deleted.getParent().getRight() : deleted.getParent().getLeft(); // add parent null
            RBNode newNode = deleted.getLeft();
            fullTransplant(deleted, newNode);
            newNode.getRight().setRedColor();
            if(bro != null)
                bro.setRedColor();
        }
        else if(deleted.getLeft() != null && deleted.getRight() == null || deleted.getLeft() == null
                && deleted.getRight() != null){ // if deleted have only one child
            RBNode newNode = (deleted.getLeft() != null) ? deleted.getLeft() : deleted.getRight();
            if(newNode.isRed()) {
                setEqualsColor(deleted, newNode);
                fullTransplant(deleted, newNode);
            }
        }
        return true;
    }

    public Boolean insert(Comparable value) {
        RBNode newnode = new RBNode(value);
        RBNode prev = null;
        RBNode current = root;
        while(current != null) {
            prev = current;
            if(newnode.compareTo(current) >= 0) // if current < newnode
                current = current.getRight();
            else
                current = current.getLeft();
        }
        if(prev == null) {
            root = newnode;
            return true;
        }
        else if(newnode.compareTo(prev) >= 0) // if newnode > prev
            prev.setRight(newnode);
        else
            prev.setLeft(newnode);
        newnode.setRedColor();

        insertFixUp(newnode);

        return true;
    }

    public V find(Comparable value) {
        RBNode<V> node = find(root, value);
        if (node != null)
            return (V) find(root, value).getValue();
        else
            return null;
    }

    protected RBNode find(RBNode node, Comparable value) {
        if(node == null) return null;
        if(value.equals(node.getValue())) return node;
        if(value.compareTo((V)node.getValue()) > 0) // if value > node
            return find(node.getRight(), value);
        else
            return find(node.getLeft(), value);
    }

    private void fullTransplant(RBNode node, RBNode newNode) {
        if(newNode != null) {
            if(newNode.getParent() != null)
                if (newNode.isLeft())
                    newNode.getParent().setLeft(null);
                else
                    newNode.getParent().setRight(null);
            newNode.setParent(null);
        }
        if(node.getParent() == null)
            root = newNode;
        else if(node.isLeft())
            node.getParent().setLeft(newNode);
        else
            node.getParent().setRight(newNode);
        newNode.setLeft(node.getLeft());
        newNode.setRight(node.getRight());

    }

    private void deleteLeaf(RBNode deleted) {
        if (deleted.isRed()) { // red leaf
            deleted.delete();
        } else { // black leaf (._. )
            if (deleted.getParent().isRed()) {
                if (deleted.getNeighbor().haveChilds()) {
                    deleted.getParent().setBlackColor();
                    if (deleted.isRight()) {
                        leftRotate(deleted.getNeighbor());
                        rightRotate(deleted.getParent());
                    } else {
                        rightRotate(deleted.getNeighbor());
                        leftRotate(deleted.getParent());
                    }
                    deleted.delete();
                } else {
                    deleted.getParent().setBlackColor();
                    deleted.getNeighbor().setRedColor();
                    deleted.delete();
                }
            } else {
                if (deleted.getNeighbor().isRed()) { // 2.1
                    if (deleted.isRight() && deleted.getNeighbor().getRight().haveChilds() || deleted.isLeft() && deleted.getNeighbor().getLeft().haveChilds()) { // 2.1.1
                        RBNode neighbor = deleted.getNeighbor();
                        if (deleted.isRight()) {
                            RBNode d = neighbor.getRight().getLeft();
                            neighbor.getRight().getLeft().setBlackColor();
                            leftRotate(neighbor);
                            rightRotate(deleted.getParent());
                            deleted.delete();
                            if (d == null) {
                                RBNode c = neighbor.getParent();
                                RBNode a = c.getNeighbor();
                                neighbor.setBlackColor();
                                rightRotate(c);
                                rightRotate(a);
                                leftRotate(c);
                            }
                        } else {
                            RBNode d = neighbor.getLeft().getRight();
                            neighbor.getLeft().getRight().setBlackColor();
                            rightRotate(neighbor);
                            leftRotate(deleted.getParent());
                            deleted.delete();
                            if (d == null) {
                                RBNode c = neighbor.getParent();
                                RBNode a = c.getNeighbor();
                                neighbor.setBlackColor();
                                leftRotate(c);
                                leftRotate(a);
                                rightRotate(c);
                            }
                        }
                    } else { // 2.1.2
                        if (deleted.isRight()) {
                            deleted.getNeighbor().getRight().setRedColor();
                            deleted.getNeighbor().setBlackColor();
                            rightRotate(deleted.getParent());
                        } else {
                            deleted.getNeighbor().getLeft().setRedColor();
                            deleted.getNeighbor().setBlackColor();
                            leftRotate(deleted.getParent());
                        }
                        deleted.delete();
                    }
                } else { // 2.2
                    if (deleted.getNeighbor().haveChilds()) { // 2.2.1
                        if (deleted.isRight()) {
                            if (deleted.getNeighbor().getRight() != null) {
                                deleted.getNeighbor().getRight().setBlackColor();
                                RBNode b = deleted.getNeighbor();
                                rightRotate(deleted.getParent());
                                rightRotate(deleted.getParent());
                                leftRotate(b);
                            } else {
                                deleted.getNeighbor().getLeft().setBlackColor();
                                rightRotate(deleted.getParent());
                                deleted.delete();
                            }
                        } else {
                            if (deleted.getNeighbor().getLeft() != null) {
                                deleted.getNeighbor().getLeft().setBlackColor();
                                RBNode b = deleted.getNeighbor();
                                leftRotate(deleted.getParent());
                                leftRotate(deleted.getParent());
                                rightRotate(b);
                            } else {
                                deleted.getNeighbor().getRight().setBlackColor();
                                leftRotate(deleted.getParent());
                                deleted.delete();
                            }
                        }
                    } else { // 2.2.2
                        deleted.getNeighbor().setRedColor();
                        deleted.delete();
                    }
                }
            }
        }

    }

    private void insertFixUp(RBNode node) {
        while (node.getParent() != null && node.getParent().isRed()) {
            if(node.getParent().isLeft()) { // if parent of node is left
                RBNode parentNeighbor = node.getParent().getNeighbor();
                if(parentNeighbor != null && parentNeighbor.isRed()) {
                    node.getParent().setBlackColor();
                    parentNeighbor.setBlackColor();
                    node.getParent().getParent().setRedColor();
                    node = node.getParent().getParent();
                }
                else {
                    if(node != null && node.isRight()) {
                        node = node.getParent();
                        leftRotate(node);
                    }
                    node.getParent().setBlackColor();
                    node.getParent().getParent().setRedColor();
                    rightRotate(node.getParent().getParent());
                }
            }
            else {
                RBNode parentNeighbor = node.getParent().getNeighbor();
                if(parentNeighbor != null && parentNeighbor.isRed()) {
                    node.getParent().setBlackColor();
                    parentNeighbor.setBlackColor();
                    node.getParent().getParent().setRedColor();
                    node = node.getParent().getParent();
                }
                else {
                    if(node != null && node.isLeft()) {
                        node = node.getParent();
                        rightRotate(node);
                    }
                    node.getParent().setBlackColor();
                    node.getParent().getParent().setRedColor();
                    leftRotate(node.getParent().getParent());
                }
            }
        }
        root.setBlackColor();
    }

    private void leftRotate(RBNode x) {
        /*       |                |
                 x                y
                / \              / \
               a   y     -->    x   z
                  / \          / \
                 b   z        a   b
        */
        RBNode y = x.getRight();
        x.setRight(y.getLeft());
        if(y.getLeft() != null)
            y.getLeft().setParent(x);
        y.setParent(x.getParent());
        if(x.getParent() == null)
            root = y;
        else if(x.isLeft())
            x.getParent().setLeft(y);
        else
            x.getParent().setRight(y);
        y.setLeft(x);
        x.setParent(y);
    }

    private void rightRotate(RBNode x) {
        /*       |                |
                 x                y
                / \              / \
               y   z     -->    a   x
              / \                  / \
             a   b                b   z
        */

        RBNode y = x.getLeft();
        x.setLeft(y.getRight());
        if(y.getRight() != null)
            y.getRight().setParent(x);
        y.setParent(x.getParent());
        if(x.getParent() == null)
            root = y;
        else if(x.isLeft())
            x.getParent().setLeft(y);
        else
            x.getParent().setRight(y);
        y.setRight(x);
        x.setParent(y);

    }

    private void setEqualsColor(RBNode first, RBNode second) {
        if(first.isBlack())
            second.setBlackColor();
        else
            second.setRedColor();
    }

    public void clear() {
        root = null;
    }

    public boolean isEmpty() { return root == null; }

    protected RBNode maximum(RBNode node) {
        RBNode current = node;
        RBNode prev = null;
        while (current != null) {
            prev = current;
            current = current.getRight();
        }
        return prev;
    }

    protected RBNode minimum(RBNode node) {
        RBNode current = node;
        RBNode prev = null;
        while (current != null) {
            prev = current;
            current = current.getLeft();
        }
        return prev;
    }

    protected int getHeight(RBNode begin) {
        if(begin == null) return 0;
        ArrayDeque<RBNode> deque = new ArrayDeque<RBNode>();
        deque.add(begin);
        int height = 1;
        RBNode last = null;
        while(!deque.isEmpty()) {
            last = deque.pop();
            if(last.getLeft() != null)
                deque.add(last.getLeft());
            if(last.getRight() != null)
                deque.add(last.getRight());
        }

        while(last != begin) {
            height++;
            if(last == null) break;
            last = last.getParent();
        }
        return height;
    }

    int getSize() {

        ArrayDeque<RBNode> deque = new ArrayDeque<RBNode>();
        ArrayDeque<RBNode> list = new ArrayDeque<RBNode>();
        deque.add(root);
        list.add(root);
        while(!deque.isEmpty()) {
            RBNode x = deque.pop();
            if (x.getLeft() != null) {
                deque.add(x.getLeft());
                list.add(x.getLeft());
            }
            if (x.getRight() != null) {
                deque.add(x.getRight());
                list.add(x.getRight());
            }
        }
        return list.size();
    }

    @Override
    public String toString() {
        if(root == null) return "";
        String output = "";
        int height = getHeight(root);
        int cols = (int) (Math.pow(2, height) - 1);
        RBNode nullNode = new RBNode(0);
        int count = 0;
        int level = 1;

        ArrayDeque<RBNode> deque = new ArrayDeque<RBNode>();
        ArrayDeque<RBNode> list = new ArrayDeque<RBNode>();
        deque.add(root);
        list.add(root);
        while(!deque.isEmpty()) {
            count++;
            RBNode x = deque.pop();
            if(x == nullNode)
                output += "(  )";
            else
                output += x + " ";
            if (x.getLeft() != null) {
                deque.add(x.getLeft());
                list.add(x.getLeft());
            }
            else
                deque.add(nullNode);
            if (x.getRight() != null) {
                deque.add(x.getRight());
                list.add(x.getRight());
            }
            else {
                deque.add(nullNode);
                list.add(nullNode);
            }
            if(count == Math.pow(2, level) - 1) {
                output += "\n";
                cols /= 2;
                level++;
            }
            if(height < level)
                break;
        }
        for (RBNode item: list) {
            output += item.getValue() + " ";
        }
        return output;
    }
}
