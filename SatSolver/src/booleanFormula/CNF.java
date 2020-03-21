package booleanFormula;

import java.util.ArrayList;

public class CNF {
	private ArrayList<Clause> clauses = new ArrayList<Clause>();
	public Variables variables; //index is the number of the variable. Value : 0 if valuation to false, 1 to true, -1 if no valuation
	public Literal[] literals; //literal xi is stored in literals[i] and literal not(xi) is stored in literal[literals.length-i]
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
		
	
	public void setLiterals(Literal[] literals) {
		this.literals = literals;
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
	
	/**
	 * Check if the formula is valid with current variable assignment
	 * throws an exception if there are unassigned variables
	 * @return
	 * @throws CNFException 
	 */
	public boolean isValid() throws CNFException {
		for(Clause c : this.clauses) {
			if(!c.isValid()){
				return false;
			}
		}
		return true;
	}
	
	public void printStat() {
		System.out.println("Number of clauses = " + this.clauses.size());
		System.out.println("Number of variables = " + this.getVariables().getSize());
		int nb_lit = 0;
		for(int i = 0;i<literals.length;i++) {
			if(literals[i] != null) {nb_lit++;}
		}
		System.out.println("Number of literals = " +nb_lit);
	}
	
	//TODO : list of stats on literals and variables
}
