package booleanFormula;

public class PairVariableFormula {
	private int variable;
	private CNF formula;
	
	public PairVariableFormula(int variable, CNF formula) {
		this.variable = variable;
		this.formula = formula;
	}

	public int getVariable() {
		return variable;
	}

	public void setVariable(int variable) {
		this.variable = variable;
	}

	public CNF getFormula() {
		return formula;
	}

	public void setFormula(CNF formula) {
		this.formula = formula;
	}
	
	@Override
	public String toString() {
		String res = "Variable = " + this.variable;
		res+= "\n";
		res+= this.formula.toString();
		return res;
	}
	
}
