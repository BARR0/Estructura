import java.util.NoSuchElementException;

public class AVLTree<E extends Comparable<E>> {

	Node root;
	private static final int ALLOWED_IMBALANCE = 1;

	public AVLTree(){
		this.root = null;
	}

    public E get(E key) {
        if(this.root == null) throw new NoSuchElementException();
        Node tmp = this.root;
        int r = 0;
        while(tmp != null){
            r = key.compareTo(tmp.element);
            if(r == 0){
                return tmp.element;
            }
            else if(r < 0){
                tmp = tmp.left;
            }
            else{
                tmp = tmp.right;
            }
        }
        throw new NoSuchElementException();
    }

    public boolean contains(E key) {
        try{
            this.get(key);
            return true;
        }
        catch(NoSuchElementException e){
            return false;
        }
    }

	public void insert(E element){
		if(this.root == null){
			this.root = new Node(element, null,null);
		}else{
			this.root = insert(element,this.root);
		}
	}

	private Node insert(E element, Node n){
		if(n == null) return new Node(element, null,null);
		int cmp = element.compareTo(n.element);
		if(cmp < 0){
			n.left = insert(element, n.left);
		}else if (cmp > 0){
			n.right = insert(element, n.right);
		}
		return balance(n);
	}

	public Node remove(E element){
		return element != null? remove(element, this.root) : null;
	}

	private Node remove(E element, Node node){
		if(node == null){
			return node; //item not found
		}

		int comparable = element.compareTo(node.element);

		if(comparable < 0){
			node.left = remove(element, node.left);
		}else if(comparable > 0){
			node.right = remove(element, node.right);
		}else if (node.left != null && node.right != null){
			node.element = findMin(node.right).element;
			node.right = remove(node.element, node.right);
		}else{
			node = (node.left != null) ? node.left : node.right;
		}
		return balance(node);
	}

	private Node findMin(Node node){
		if(node.left == null){
			return node;
		}
		return findMin(node.left);
	}

	private Node balance(Node n){
		if (n == null) return n;
		if(height(n.left) - height(n.right) > ALLOWED_IMBALANCE){
			if(height(n.left.left) >= height(n.left.right)){
				n = rotateWithLeftChild(n);
			}else{
				n = doubleWithLeftChild(n);
			}
		}else{
			if(height(n.right) - height(n.left) > ALLOWED_IMBALANCE){
				if(height(n.right.right) >= height(n.right.left)){
					n = rotateWithRightChild(n);
				}else{
					n = doubleWithRightChild(n);
				}
			}
		}
		n.height = Math.max(height(n.left), height(n.right))+1;
		return n;
	}

	private Node rotateWithLeftChild(Node a){
		Node b = a.left;
		a.left = b.right;
		b.right = a;
		a.height = Math.max(height(a.left), height(a.right))+1;
		b.height = Math.max(height(b.left), height(b.right))+1;
		return b;
	}

	private Node rotateWithRightChild(Node a){
		Node b = a.right;
		a.right = b.left;
		b.left = a;
		a.height = Math.max(height(a.left), height(a.right))+1;
		b.height = Math.max(height(b.left), height(b.right))+1;
		return b;
	}

	private Node doubleWithLeftChild(Node x){
		x.left = rotateWithRightChild(x.left);
		return rotateWithLeftChild(x);
	}

	private Node doubleWithRightChild(Node x){
		x.right = rotateWithLeftChild(x.right);
		return rotateWithRightChild(x);
	}

	private int height(Node n){
		return n == null?0:n.height;
	}

	public String toString(){
		if(this.root != null){
			return inOrder(this.root);
		}else{
			return "";
		}
	}

	private String inOrder(Node node){
		String string = "";
		if(node.left != null){
			string += inOrder(node.left);
		}
		string += node.toString();
		if(node.right != null){
			string += inOrder(node.right);
		}
		return string;
	}

	private class Node{
		E element;
		Node right, left;
		int height;

		public Node(E element, Node right, Node left){
			this.element = element;
			this.right = right;
			this.left = left;
			this.height = Math.max(
					right == null?1:right.height+1,
					left == null?1:left.height+1
			);
		}

		public String toString(){
			return "["+this.element.toString()+"]";
		}
	}
	public static void main(String[] args) {
		AVLTree<Integer> arbol = new AVLTree<Integer>();
		arbol.insert(84);
		arbol.insert(10);
		arbol.insert(8);
		arbol.insert(92);
		arbol.insert(66);
		arbol.insert(88);
		arbol.insert(29);
		arbol.insert(27);
		arbol.insert(75);
		arbol.insert(72);
		arbol.insert(68);
		arbol.insert(62);
		arbol.insert(18);
		arbol.insert(80);
		arbol.insert(36);
		arbol.insert(1);
		arbol.insert(40);
		System.out.println(arbol.toString());
		arbol.remove(72);
		System.out.println(arbol.toString());
	}
}
