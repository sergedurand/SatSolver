package solver;

public class Trace {
	private int node_var;
	private Trace left_var;
	private Trace right_var;
	private Trace parent;
	
	public Trace() {
		this.node_var = -1; 
		this.left_var = null;
		this.right_var = null;
		this.parent = null;
	}
	public Trace(int node_var) {
		super();
		this.node_var = node_var;
	}
	
	public void addChildren(int var) {
		this.left_var = new Trace(var);
		this.left_var.setParent(this);
		this.right_var = new Trace(var);
		this.right_var.equals(this);
	}

	public int getNode_var() {
		return node_var;
	}

	public void setNode_var(int node_var) {
		this.node_var = node_var;
	}

	public Trace getLeft_var() {
		return left_var;
	}

	public void setLeft_var(Trace left_var) {
		this.left_var = left_var;
	}

	public Trace getRight_var() {
		return right_var;
	}

	public void setRight_var(Trace right_var) {
		this.right_var = right_var;
	}

	public Trace getParent() {
		return parent;
	}

	public void setParent(Trace parent) {
		this.parent = parent;
	}

}
