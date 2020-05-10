package booleanFormula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 
 * @author Serge
 *
 */
public class Clause {
	
	private Set<Integer> literals = new HashSet<Integer>();
	private Set<Integer> activeLiterals = new HashSet<Integer>();
	private Set<Integer> unactiveLiterals = new HashSet<Integer>();
	
	private static int count = 0;
	private int id;
	private CNF formula;
	
	public Clause() {
		super();
		this.id = count++;
	}
	public Clause(Set<Integer> literals) {
		super();
		this.literals = literals;
		for(int lit : literals) {
			this.activeLiterals.add(lit);
		}
		this.id = count++;
	}
	public Set<Integer> getLiterals() {
		return literals;
	}
	public void setLiterals(LinkedHashSet<Integer> literals) {
		this.literals = literals;
		for(int lit : literals) {
			this.activeLiterals.add(lit);
		}
	}
	public int getId() {
		return id;
	}
	
	public void addLiteral(int lit_id) throws CNFException {
		this.literals.add(lit_id);
		this.activeLiterals.add(lit_id);
		this.formula.getLiterals()[lit_id].addClause(this.id);
		this.formula.getVariables().addClause(this.id, this.formula.getLiterals()[lit_id].getId());
	}
	
	public void removeLiteral(int lit_id) {
		//first we remove the current clause from the literal list of clauses.
		this.formula.getLiterals()[lit_id].removeClause(this.getId());
		this.literals.removeAll(Arrays.asList(Integer.valueOf(lit_id)));
		this.activeLiterals.remove(lit_id);
	}
	
	public void deactivateLiteral(int lit_id) {
		this.activeLiterals.remove(lit_id);
		this.unactiveLiterals.add(lit_id);
	}
	
	public void reactivateLiteral(int lit_id) {
		this.unactiveLiterals.remove(lit_id);
		this.activeLiterals.add(lit_id);
	}

	public static void resetCounter() {
		count = 0;
	}
	
	public boolean isValid() throws CNFException {
		for(Integer i : this.literals) {
			int val = this.formula.getLiterals()[i].getVal();
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
			if(this.formula.getLiterals()[i] == null) {
				continue;
			}
			int val = this.formula.getLiterals()[i].getVal();
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
		Iterator<Integer> it = this.activeLiterals.iterator();
		while(it.hasNext()) {
			int l_id = it.next();
			res += formula.getLiterals()[l_id].toString();
			if(it.hasNext()) {
				res += " "+ "v"+" ";
			}
		}
		res += ")";
		return res;
	}
	
	/**
	 * print the clause in the style associated with grid-like problem (e.g. sudoku or latin square)
	 * @param size
	 * @return
	 */
	public String gridToString(int size) {
		String res = "(";
		Iterator<Integer> it = this.literals.iterator();
		while(it.hasNext()){
			int l_id = it.next();
			res += formula.getLiterals()[l_id].gridToString(size);
			if(it.hasNext()) {
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
		return (this.activeLiterals.size()==1);
	}
	
	public boolean isEmpty() {
		return (this.activeLiterals.size()==0);
	}
	
	public Clause clone() {
		LinkedHashSet<Integer> new_lit = new LinkedHashSet<Integer>(this.literals.size());
		for(int l_id : this.literals) {
			new_lit.add(l_id);
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
		return this.literals.contains(lit_id);
	}
}
