package AVL;

/**
 * @author Andrés
 *
 * @param <E>
 * 
 */

public class AVLTree <E extends Comparable<E>>{
    private static final int ALLOWED_IMBALANCE = 1;

    private Node root;

    public AVLTree(){
        this.root = null;
    }
    public void insert(E elem){
        if(this.root == null)
            this.root = new Node(elem);
        else
            this.root = this.insert(elem, this.root);
    }
    /**
     * Insert en un BST.
     * Al final manda llamar el metodod balance
     * 
     */
    private Node insert(E elem, Node node){
        if(node == null)
            return new Node(elem);
        int r = elem.compareTo(node.elem);
        if(r < 0){
            node.left = this.insert(elem, node.left);
        }
        else if(r > 0){
            node.right = this.insert(elem, node.right);
        }
        return this.balance(node);
    }
    /**
     * Asegura que el arbol este balanceado.
     * Llama rotaciones cuando es necesario.
     * 
     */
    private Node balance(Node n){
        if(n == null) return null;
        if(height(n.left) - height(n.right) > ALLOWED_IMBALANCE){
            if(height(n.left.left) >= height(n.left.right))
                n = rotateWithLeftChild(n);
            else
                n = doubleWithLeftChild(n);
        }
        if(height(n.right) - height(n.left) > ALLOWED_IMBALANCE){
            if(height(n.right.right) >= height(n.right.left))
                n = rotateWithRightChild(n);
            else
                n = doubleWithRightChild(n);
        }
        n.height = Math.max(height(n.left), height(n.right)) + 1;
        return n;
    }
    /**
     * 
     * 
     * 
     */
    private Node rotateWithLeftChild(Node n){
        Node l = n.left;
        n.left = l.right;
        l.right = n;
        n.height = Math.max(height(n.left), height(n.right)) + 1;
        l.height = Math.max(height(l.left), height(l.right)) + 1;
        return l;
    }
    /**
     * 
     * 
     * 
     */
    private Node rotateWithRightChild(Node n){
        Node r = n.right;
        n.right = r.left;
        r.left = n;
        n.height = Math.max(height(n.left), height(n.right)) + 1;
        r.height = Math.max(height(r.left), height(r.right)) + 1;
        return r;
    }
    /**
     * 
     * 
     * 
     */
    public Node doubleWithLeftChild(Node n){
        n.left = rotateWithRightChild(n.left);
        return rotateWithLeftChild(n);
    }
    /**
     * 
     * 
     * 
     */
    public Node doubleWithRightChild(Node n){
        n.right = rotateWithLeftChild(n.right);
        return rotateWithRightChild(n);
    }
    /**
     * Regresa la altura de un nodo.
     * 
     * 
     */
    private int height(Node n){
        if(n == null) return 0;
        return n.height;
    }
    /**
     * Regresa un String en inorder.
     * 
     * 
     */
    public String toString() {
        if(this.root == null) return "";
        return this.root.inOrder();
    }

    private class Node{
        public E elem;
        public Node left,
                    right;
        public int height;

        /*public Node(Node left, E elem, Node right){
            this.left = left;
            this.right = right;
            this.elem = elem;
            this.height = Math.max(
                this.left == null ? 0 : this.left.height,
                this.right == null ? 0 : this.right.height
            ) + 1;
        }*/

        public Node(E elem){
            this.elem = elem;
            this.left = null;
            this.right = null;
            this.height = 1;
        }

        public String inOrder(){
            return ((this.left != null) ? this.left.inOrder() : "") + this.toString() + ((this.right != null) ? this.right.inOrder() : "");
        }

        public String toString(){
            return "[" + this.elem + "-" + this.height + "]";
        }
    }
    public static void main(String[] args){
        AVLTree<Integer> t = new AVLTree<>();
        t.insert(84);
        t.insert(10);
        t.insert(8);
        t.insert(92);
        t.insert(66);
        t.insert(88);
        t.insert(29);
        t.insert(27);
        t.insert(75);
        t.insert(72);
        t.insert(68);
        t.insert(62);
        t.insert(18);
        t.insert(80);
        t.insert(36);
        t.insert(1);
        t.insert(40);
        System.out.println(t);
    }
}
