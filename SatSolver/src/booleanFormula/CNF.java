package booleanFormula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CNF {
	private HashMap<Integer,Clause> clauses = new HashMap<Integer,Clause>();
	public Variables variables; //index is the number of the variable. Value : 0 if valuation to false, 1 to true, -1 if no valuation
	public Literal[] literals; //literal xi is stored in literals[i] and literal not(xi) is stored in literal[literals.length-i]
	public CNF() {
		super();
	}
	
	public CNF(int[] variables) {
		this.variables = new Variables(variables);
	}
	
	public CNF(HashMap<Integer,Clause> clauses, int[] variables) {
		super();
		this.clauses = clauses;
		this.variables = new Variables(variables);
	}
	
	public CNF(Variables variables) {
		super();
		this.variables = variables;
	}

	public HashMap<Integer,Clause> getClauses() {
		return clauses;
	}

	public void setClauses(HashMap<Integer,Clause> clauses) {
		this.clauses = clauses;
	}
	
	public void addClause(Clause c) {
		this.clauses.put(c.getId(),c);
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
		for(HashMap.Entry<Integer,Clause> e : this.clauses.entrySet()) {
			res+= e.getValue().toString();
			res+= " ^ ";
		}
		res+="]";
		res = res.substring(0,res.lastIndexOf("^")-1) + res.substring(res.lastIndexOf("^")+2);
		
		return res;
	}
	
	/**
	 * Check if the formula is valid with current variable assignment
	 * throws an exception if there are unassigned variables
	 * @return
	 * @throws CNFException 
	 */
	
	public boolean isValid() throws CNFException {
		for(HashMap.Entry<Integer,Clause> e : this.clauses.entrySet()) {
			if(e.getValue().isValid()) {
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
		System.out.println("Number of unique literals = " +nb_lit);
		nb_lit = 0;
		for(HashMap.Entry<Integer,Clause> e : this.getClauses().entrySet()) {
			Clause c = e.getValue();
			nb_lit += c.getLiterals().size();
		}
		System.out.println("Number of total literals = " +nb_lit);

	}

	public Literal[] getLiterals() {
		return literals;
	}
	
	/**
	 * returns the index of the literal
	 * @param lit_id
	 * @return
	 */
	public int getSpecificLiteral(int lit_id) {
		for(int i = 0;i<this.literals.length;i++) {
			if(this.literals[i] == null) {
				continue;
			}
			if(this.literals[i].getId() == lit_id) {
				return i;
			}
		}
		return -1;
	}
	
	
	public void removeClause(int clause_ID) throws CNFException {
		this.clauses.remove(clause_ID);
		this.variables.removeClauseFromAll(clause_ID);
		for(Literal l : this.literals) {
			if(l != null) {
				l.removeClause(clause_ID);
			}
		}
	}
	
	/**
	 * return 1 if the formula is satisfied
	 * return 0 if it is unsatisfiable
	 * return -1 if it is potentially satisfiable
	 * @return
	 * @throws CNFException
	 */
	public int satSituation() throws CNFException {
		int res = 1;
		for(HashMap.Entry<Integer,Clause> e : this.clauses.entrySet()) {
			Clause c = e.getValue();
			if(c.isEmpty() || c.clauseSituation() == 0) { //one unsatisfiable clause found
				res=0;
				break;
			}
			if(c.clauseSituation() == -1) {
				res=-1; //one potentially satisfied clause
				break;
			}
		}
		return res; //res is 1 if and only if all clauses are sat
	}
	
	//TODO : list of stats on literals and variables
}
