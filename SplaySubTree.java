
public class SplaySubTree<T extends Comparable<T>> {

	private T data;
	private SplaySubTree<T> left, right, parent;
	private long size; // number of nodes in tree

	/**
	 * @param node - If node==null then size will be 0
	 *               otherwise node will be in the tree and size will be 1
	 */
	public SplaySubTree(T node) {
		data = node;
		if (node == null) size = 0;
		else size = 1;
	}
	
	public String toString(){
		String lft = "";
		String rght = "";
		String myData = ""+data;
		if(left!=null){
			myData += " left="+ left.data;
			lft = left.toString();
		}else lft = "null";
		if(right!=null){
			myData += " right="+ right.data;
			rght = right.toString();
		}else rght = "null";
		
		return myData + "\n" + lft + rght;
	}
	
	public T getData() {return data;}

	/**
	 * @param index - of the node to search for.
	 * @return  - null if index<=0 or index>=size otherwise SubTree at index. 
	 */
	public SplaySubTree<T> get(long index) {
		if (index > size || index < 0) return null;
		long cS = 1;
		SplaySubTree<T> cT= this;
		if(cT.left!=null)cS +=cT.left.size;
		while(cS!=index){
			if(cS>index){
				cS--;
				cT = cT.left;
				if(cT!=null && cT.right!=null)cS-= cT.right.size;
			}else{
				cS++;
				cT = cT.right;
				if(cT!=null && cT.left!=null)cS += cT.left.size;
			}
		}
		return cT;
	}

	/**
	 * @return - the number of nodes in the tree.
	 */
	public long size() { return size;}

	/**
	 * @param node - to search for.
	 * @return - the index of node. All nodes are ordered according to the compareTo(T) method.
	 *         
	 */
	public long indexOf(T node) {
		if(node==null)return -1;
		long cI = 1;
		SplaySubTree<T> cT= this;
		if(cT.left!=null)cI +=cT.left.size;
		while(!cT.data.equals(node)){
			if(cT.data.compareTo(node)>0){
				cI--;
				cT = cT.left;
				if(cT!=null && cT.right!=null)cI-= cT.right.size;
			}else{
				cI++;
				cT = cT.right;
				if(cT!=null && cT.left!=null)cI += cT.left.size;
			}
			if(cT==null)return -1;
		}
		
		return cI;
	}

	/**
	 * @param node - is added to the tree.
	 *             If node is null tree is unchanged.
	 * @return - New root of the tree. 
	 */
	public SplaySubTree<T> add(T node) {
		if (node == null)
			return this;
		if (this.data == null)
			return new SplaySubTree<T>(node);
		SplaySubTree<T> current = this;
		SplaySubTree<T> child = null;

		if (this.data.compareTo(node) < 0)
			child = this.right;
		else
			child = this.left;
		while (child != null) {
			current = child;
			if (current.data.compareTo(node) < 0)
				child = current.right;
			else
				child = this.left;
		}

		SplaySubTree<T> newNode = new SplaySubTree<T>(node);
		if (current.data.compareTo(node) < 0){
			current.right = newNode;	
		}else{
			current.left = newNode;
		}
		newNode.parent = current;
		newNode.splay();
		return newNode;
	}

	/**
	 * @param node - is removed from the tree.
	 *             If node is null tree is unchanged.
	 * @return - New root of the tree.
	 */
	public SplaySubTree<T> remove(T node) {
		if(node==null)return this;
		SplaySubTree<T> x = find(node);
		if (x == null)
			return this;
		// To delete a node x:
		// if x has no children remove it.
		
		if (x.left == null && x.right == null) {
			if(x.parent!=null){
				if(x.parent.left == x){
					parent.left = null;
				}else parent.right = null;
			}else return new SplaySubTree(null);
			
		}
		// if x has one child remove x, and put the child in place of x
		if(x.left==null){
			if(x.parent!=null){
				if(x.parent.left == x){
					parent.left = x.right;
					x.right.parent = parent;
					x = x.right;
				}else{
					parent.right = x.right;
					x.right.parent = parent;
					x = x.right;
				}
			}else{
				x.right.parent = null;
				return x.right;
			}
		}else if(x.right == null){
			if(x.parent!=null){
				if(x.parent.left == x){
					parent.left = x.left;
					x.left.parent = parent;
					x = x.left;
				}else{
					parent.right = x.left;
					x.left.parent = parent;
					x = x.left;
				}
			}else{
				x.left.parent = null;
				return x.left;
			}
		}else{
			// if x has two children, swap its value with that of
			// the rightmost node of its left sub tree
			SplaySubTree<T> rmc = x.left;
			while(rmc.right!=null)rmc = rmc.right;
			x.data = rmc.data;
			// Then remove that node instead.
			rmc.left.parent = rmc.parent;
			if(rmc.parent == x){
				x.left = rmc.left;
			}else{
				rmc.parent.right = rmc.left;
			}
			x = rmc;
		}
		
		// After deletion, splay the parent of the removed node to the top of
		// the tree.
		x.splay();
		return x;
	}

	/**
	 * @param other
	 * @return
	 */
	public SplaySubTree<T> join(SplaySubTree<T> other) {
		return null;

	}

	/**
	 * @param node
	 * @return
	 */
	public SplaySubTree<T> split(T node) {
		return null;
	}

	/**
	 * @param node
	 * @return
	 */
	public SplaySubTree<T> find(T node) {
		SplaySubTree<T> current = this;
		if(this.data==null)return null;
		while (current != null) {
			if (node.equals(current.data))
				return current;
			if (node.compareTo(current.data) < 0)
				current = current.left;
			else
				current = current.right;
		}
		return current;
	}


	/**
	 * Assuming this node is an interior or leaf node of a larger tree
	 * this method moves this node up to the root balancing the tree in the process
	 */
	public void splay() {
		if (Math.random() < 0.5)
			return;
		while (this.parent != null) {
			SplaySubTree<T> p = this.parent;
			SplaySubTree<T> g = p.parent;
			if (g == null && this == p.left) {
				zig();
			} else if (g == null && this == p.right) {
				zag();
			} else if (p.left == this && g.left == p) {
				zigzig();
			} else if (p.right == this && g.right == p) {
				zagzag();
			} else if (p.right == this && g.left == p) {
				zigzag();
			} else {
				zagzig();
			}
		}
	}

	/**
	 * This is a helper method used in the splay() operation
	 */
	private void zig() {
		SplaySubTree<T> b = this.right;
		SplaySubTree<T> p = this.parent;
		
		
		this.right = p;
		p.parent = this;
		p.left = b;
		if(b!=null)b.parent = p;
		
		p.size = 1;
		if(p.right!=null)p.size+=p.right.size;
		if(b!=null)p.size+= b.size;
		this.size = p.size +1;
		if(this.left!=null)this.size+=this.left.size;
	}

	/**
	 * This is a helper method used in the splay() operation
	 */
	private void zag() {
		SplaySubTree<T> b = this.left;
		SplaySubTree<T> p = this.parent;
		
		this.left = p;
		p.parent = this;
		p.right = b;
		if(b!=null)b.parent = p;
		
		if(b!=null)p.size = p.left.size + b.size+1;
		else p.size = p.left.size +1;
		this.size = p.size + this.right.size+1;
	}

	/**
	 * This is a helper method used by zigzig, zagzag, zigzag, zagzig
	 * This "fixes" the great grandparent
	 */
	private void fixGG(SplaySubTree<T> g) {
		SplaySubTree<T> gg = g.parent;
		if (gg != null) {
			if (g == gg.left)
				gg.left = this;
			if (g == gg.right)
				gg.right = this;
		}
		this.parent = gg;
	}

	/**
	 * This is a helper method used in the splay() operation
	 */
	private void zigzig() {
		SplaySubTree<T> g = parent.parent;
		SplaySubTree<T> b = this.right;
		SplaySubTree<T> p = this.parent;
		SplaySubTree<T> c = p.right;
		fixGG(g);

		b.parent = p;
		p.left = b;
		c.parent = g;
		g.left = c;
		g.parent = p;
		p.right = g;
		p.parent = this;
		this.right = p;
		
		g.size = c.size+g.right.size+1;
		p.size = g.size+b.size+1;
		this.size = p.size + this.left.size+1;
	}

	/**
	 * This is a helper method used in the splay() operation
	 */
	private void zagzag() {
		SplaySubTree<T> g = parent.parent;
		SplaySubTree<T> b = this.left;
		SplaySubTree<T> p = this.parent;
		SplaySubTree<T> c = p.left;

		fixGG(g);
		b.parent = p;
		p.right = b;
		c.parent = g;
		g.right = c;
		g.parent = p;
		p.left = g;
		p.parent = this;
		this.left = p;
		g.size = c.size+g.left.size+1;
		p.size = g.size+b.size+1;
		this.size = p.size + this.right.size+1;
	}

	/**
	 * This is a helper method used in the splay() operation
	 */
	private void zigzag() {
		SplaySubTree<T> g = parent.parent;
		SplaySubTree<T> b = this.left;
		SplaySubTree<T> p = this.parent;
		SplaySubTree<T> c = this.right;

		fixGG(g);
		b.parent = p;
		p.right = b;
		c.parent = g;
		g.left = c;
		p.parent = this;
		this.left = p;
		g.parent = this;
		this.right = g;
		
		g.size = g.right.size + c.size+1;
		p.size = p.left.size + b.size+1;
		this.size = g.size+p.size+1;
	}

	/**
	 * This is a helper method used in the splay() operation
	 */
	private void zagzig() {
		SplaySubTree<T> g = parent.parent;
		SplaySubTree<T> b = this.right;
		SplaySubTree<T> p = this.parent;
		SplaySubTree<T> c = this.left;
		fixGG(g);
		b.parent = p;
		p.left = b;
		c.parent = g;
		g.right = c;
		p.parent = this;
		this.right = p;
		g.parent = this;
		this.left = g;
		g.size = g.left.size + c.size+1;
		p.size = p.right.size + b.size+1;
		this.size = g.size+p.size+1;
	}


}
