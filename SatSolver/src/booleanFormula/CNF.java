package booleanFormula;

import java.util.ArrayList;

public class CNF {
	private ArrayList<Clause> clauses = new ArrayList<Clause>();
	public Variables variables; //index is the number of the variable. Value : 0 if valuation to false, 1 to true, -1 if no valuation
	
	public CNF() {
		super();
	}
	
	public CNF(int[] variables) {
		this.variables = new Variables(variables);
	}
	
	public CNF(ArrayList<Clause> clauses, int[] variables) {
		super();
		this.clauses = clauses;
		this.variables = new Variables(variables);
	}
	
	public CNF(Variables variables) {
		super();
		this.variables = variables;
	}

	public ArrayList<Clause> getClauses() {
		return clauses;
	}

	public void setClauses(ArrayList<Clause> clauses) {
		this.clauses = clauses;
	}
	
	public void addClause(Clause c) {
		this.clauses.add(c);
	}
		
	
	
	public Variables getVariables() {
		return variables;
	}

	public void setVariables(Variables variables) {
		this.variables = variables;
	}

	@Override
	public String toString() {
		String res = "[";
		for(int i = 0;i<this.clauses.size();i++) {
			res += clauses.get(i).toString();
			if(i!=this.clauses.size()-1) {
				res+= " ^ ";
			}
		}
		res+="]";
		
		return res;
	}
	
	//TODO : list of stats on literals and variables
}
