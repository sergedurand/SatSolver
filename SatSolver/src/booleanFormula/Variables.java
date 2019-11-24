package booleanFormula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class Variables {
	private int[] variables;
	private PriorityBlockingQueue<Integer>[] clauses;
	
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
		this.clauses = new PriorityBlockingQueue[this.variables.length];
		for(int i = 0;i<this.variables.length;i++) {
			this.clauses[i] = new PriorityBlockingQueue<Integer>();
		}
	}
	
	/**
	 * this constructor initialize the array of variables with no valuation
	 * and initialize all the queues for clauses ID
	 * @param size
	 */
	public Variables(int size) {
		this.variables = new int[size];
		for(int i = 0;i<size;i++) {
			variables[i] = -1;
		}
		this.clauses = new PriorityBlockingQueue[this.variables.length];
		for(int i = 0;i<this.variables.length;i++) {
			this.clauses[i] = new PriorityBlockingQueue<Integer>();
		}
	}

	public int[] getVariables() {
		return variables;
	}

	public void setVariables(int[] variables) {
		this.variables = variables;
	}
	
	public int getSize() {
		return this.variables.length;
	}
	
	/**
	 * return the valuation of variable xi
	 * v
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
			throw new CNFException("index out of bound, no such variable in the variables");
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
			throw new CNFException("index out of bound, no such variable in the variables");
		}
		if(this.clauses[i].contains(clause_ID)) { //to prevent duplicates from list of clauses
			return;
		}
		this.clauses[i].add(clause_ID);
	}
	
	
	
}
