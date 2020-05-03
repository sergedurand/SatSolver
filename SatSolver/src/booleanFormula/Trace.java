package booleanFormula;

public class Trace {
	private PairVariableFormula root;
	private Trace left;
	private Trace right;
	private Trace parent;
	public Trace(PairVariableFormula root) {
		this.root = root;
		this.left = null;
		this.right = null;
		this.parent = null;
	}
	
	public Trace(PairVariableFormula root,PairVariableFormula left, PairVariableFormula right) {
		this(root);
		this.left = new Trace(left);
		this.left.setParent(this);
		this.right = new Trace(right);
		this.right.setParent(this);
	}

	public PairVariableFormula getRoot() {
		return root;
	}

	public void setRoot(PairVariableFormula root) {
		this.root = root;
	}

	public Trace getLeft() {
		return left;
	}

	public void setLeft(Trace left) {
		this.left = left;
		this.left.setParent(this);
	}

	public Trace getRight() {
		return right;
	}

	public void setRight(Trace right) {
		this.right = right;
		this.right.setParent(this);
	}

	public Trace getParent() {
		return parent;
	}

	public void setParent(Trace parent) {
		this.parent = parent;
	}

}
