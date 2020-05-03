package booleanFormula;

import java.util.ArrayList;
import java.util.Random;

/**
 * 
 * @author Serge
 *
 */
public class Clause {
	
	private ArrayList<Integer> literals = new ArrayList<Integer>();
	private static int count = 0;
	private int id;
	private CNF formula;
	
	public Clause() {
		super();
		this.id = count++;
	}
	public Clause(ArrayList<Integer> literals) {
		super();
		this.literals = literals;
		this.id = count++;
	}
	public ArrayList<Integer> getLiterals() {
		return literals;
	}
	public void setLiterals(ArrayList<Integer> literals) {
		this.literals = literals;
	}
	public int getId() {
		return id;
	}
	
	public void addLiteral(int lit_id) {
		this.literals.add(lit_id);
	}
	
	public void removeLiteral(int lit_id) {
		//first we remove the current clause from the literal list of clauses.
		this.formula.getLiterals()[lit_id].removeClause(this.getId());
		this.literals.remove(Integer.valueOf(lit_id));
		
	}

	public static void resetCounter() {
		count = 0;
	}
	
	public boolean isValid() throws CNFException {
		for(Integer i : this.literals) {
			int val = this.formula.literals[i].getVal();
			if(val == -1) {
				throw new CNFException("la variable %d n'a pas de valuation" + Math.abs(i));
			}
			if(val == 1) {
				return true;
			}
		}
		return false; 	
	}
	
	/**
	 * return 1 if the clause is satisfied (i.e. one literal satisfies it)
	 * return 0 if the clause is unsatisfiable (i.e. no literal satisfied it and no literal
	 * is unassigned)
	 * return -1 if the clause is potentially satisfiable (no literal satisfied it
	 * but there are literals unassigned);
	 * @return
	 * @throws CNFException
	 */
	public int clauseSituation() throws CNFException {
		int unassigned = 0;
		for(Integer i : this.literals) {
			if(this.formula.literals[i] == null) {
				continue;
			}
			int val = this.formula.literals[i].getVal();
			if(val == 1) {
				return 1;
			}
			if(val == -1) {
				unassigned += 1;
			}
		}
		if(unassigned == 0) {
			return 0;
		}
		return -1;
	}
	
	
	@Override
	public String toString() {
		String res = "(";
		for(int i = 0;i<literals.size();i++) {
			int l_id = literals.get(i);
			res += formula.literals[l_id].toString();
			if(i!=literals.size()-1) {
				res += " "+ "v"+" ";
			}
		}
		res += ")";
		return res;
	}
	
	public String sudToString() {
		String res = "(";
		for(int i = 0;i<literals.size();i++) {
			int l_id = literals.get(i);
			res += formula.literals[l_id].sudToString();
			if(i!=literals.size()-1) {
				res += " "+ "v"+" ";
			}
		}
		res += ")";
		return res;
	}
	
	
	/**
	 * Return true if the clause has only 1 literal
	 * false otherwise
	 * @return
	 */
	public boolean isUnit() {
		return (this.getLiterals().size()==1);
	}
	
	public boolean isEmpty() {
		return (this.getLiterals().size()==0);
	}
	
	public Clause clone() {
		ArrayList<Integer> new_lit = new ArrayList<Integer>(this.literals.size());
		for(int i = 0;i<this.literals.size();i++) {
			new_lit.add(this.literals.get(i));
		}
		
		return new Clause(new_lit);
	}
	public CNF getFormula() {
		return formula;
	}
	public void setFormula(CNF formula) {
		this.formula = formula;
	}
	
	/**
	 * check if the variable var_id is in this clause
	 * @param var_id
	 * @return
	 */
	public boolean hasVar(int var_id) {
		for(int idx : this.literals) {
			if(this.formula.getLiterals()[idx].getId()==var_id) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasLit(int lit_id) {
		for(int idx : this.literals) {
			if(idx == lit_id) {
				return true;
			}
		}
		return false;
	}

	
}
