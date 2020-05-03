package booleanFormula;

public class Trace {
	private PairVariableFormula root;
	private Trace left;
	private Trace right;
	
	public Trace(PairVariableFormula root) {
		this.root = root;
		this.left = null;
		this.right = null;
	}
	
	public Trace(PairVariableFormula root,Trace left, Trace right) {
		this(root);
		this.left = left;
		this.right = right;
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
	}

	public Trace getRight() {
		return right;
	}

	public void setRight(Trace right) {
		this.right = right;
	}

}
