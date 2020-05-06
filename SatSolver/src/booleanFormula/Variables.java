package booleanFormula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class Variables {
	private int[] variables;
	private LinkedList<Integer>[] clauses;
	
	/**
	 * this constructor initialize variables from input
	 * and initialize n empty priority queues of integer:
	 * each being a list of clauses ID for each variables
	 * @param variables
	 */
	@SuppressWarnings("unchecked")
	public Variables(int[] variables) {
		super();
		this.variables = variables;
		this.clauses = new LinkedList[this.variables.length];
		for(int i = 0;i<this.variables.length;i++) {
			this.clauses[i] = new LinkedList<Integer>();
		}
	}
	
	
	/**
	 * this constructor initialize the array of variables with no valuation
	 * and initialize all the queues for clauses ID
	 * @param size
	 */
	@SuppressWarnings("unchecked")
	public Variables(int size) {
		this.variables = new int[size];
		for(int i = 0;i<size;i++) {
			variables[i] = -1;
		}
		this.clauses = new LinkedList[this.variables.length];
		for(int i = 0;i<this.variables.length;i++) {
			this.clauses[i] = new LinkedList<Integer>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setVariablesFromString(String s) throws CNFException {
		if(s.length() != this.variables.length) {
			throw new CNFException("the binary string s : "+ s +"is not suitable for the variables : too long or too short");
		}
		
		for(int i = 0;i<s.length();i++) {
			char c = s.charAt(i);
			if(c!='0' && c!='1') {
				throw new CNFException("valuation :"+c+" is not suitable. \n Convention: -1 if no valuation, 0 if false, 1 if true");
			}
			this.variables[i] = Integer.parseInt(String.valueOf(c));
		}

	}

	public int[] getInterpretation() {
		return this.variables;
	}

	public void setVariables(int[] variables) {
		this.variables = variables;
	}
	
	public int getSize() {
		return this.variables.length;
	}
	
	/**
	 * return the valuation of variable i
	 * 1 for true, 0 for false, -1 for unassigned
	 * @param i
	 * @return
	 */
	public int getVal(int i) throws CNFException{
		if(i>this.getSize()-1) {
			throw new CNFException("index out of bound, no such variable in the variables");
		}
		return variables[i];
	}
	
	public void setVal(int i, int val)throws CNFException {
		if(i>this.getSize()-1) {
			throw new CNFException("index : " + i + " out of bound, no such variable in the variables");
		}
		if(val!=0 && val!=-1 && val!=1) {
			throw new CNFException("valuation :"+val+" is not suitable. \n Convention: -1 if no valuation, 0 if false, 1 if true");
		}
		variables[i] = val;
	}
	
	/**
	 * add a clause_ID in the list of clauses of variable xi
	 * if it's not already in it (to prevent duplicates)
	 * @param clause_ID
	 * @param i
	 * @throws CNFException
	 */
	public void addClause(int clause_ID, int i) throws CNFException {
		if(i>this.getSize()-1) {
			throw new CNFException("index out of bound, no such variable in the variables. i = " + i + ", max_size = "+(this.getSize()-1));
		}
		if(this.clauses[i].contains(clause_ID)) { //to prevent duplicates from list of clauses
			return;
		}
		this.clauses[i].add(clause_ID);
	}
	
	/**
	 * Remove a clause (clause_ID) from the list of clauses using variable i. 
	 * @param clause_ID
	 * @param i
	 * @throws CNFException
	 */
	public void removeClause(int clause_ID,int i) throws CNFException {
		if(i>this.getSize()-1) {
			throw new CNFException("index " + i + " out of bound, no such variable in the variables");
		}
		if(this.clauses[i].contains(clause_ID)) {
			this.clauses[i].remove(new Integer(clause_ID));
		}
		
	}
	
	public void removeClauseFromAll(int clause_ID) throws CNFException {
		for(int i = 0;i<this.getSize();i++) {
			this.removeClause(clause_ID, i);
		}
	}
	
	
	public boolean isUnassigned(int i)  throws CNFException{
		if(i>this.getSize()-1) {
			throw new CNFException("index out of bound, no such variable in the variables");
		}
		return this.variables[i] == -1;
	}

	public LinkedList<Integer>[] getClauses() {
		return clauses;
	}

	public void setClauses(LinkedList<Integer>[] clauses) {
		this.clauses = clauses;
	}

	public int[] getVariables() {
		return variables;
	}
	
	public Variables clone() {
		int[] interpretation = this.variables.clone();
		return new Variables(interpretation);
	}
	
	public LinkedList<Integer> getUnassigned() throws CNFException{
		LinkedList<Integer> res = new LinkedList<Integer>();
		for(int i = 0;i<this.variables.length;i++) {
			if(this.getVal(i)==-1) {
				res.push(i);
			}
		}
		return res;
	}
	
	public void printClauses(int var_id) {
	}
	
	
	
}
