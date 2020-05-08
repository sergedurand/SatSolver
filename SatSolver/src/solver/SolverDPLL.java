package solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.ComparatorVariable;
import booleanFormula.Literal;
import booleanFormula.PairVariableFormula;
import booleanFormula.Trace;
import booleanFormula.TracePrinter;
import booleanFormula.Variables;
import tools.Tools;

public class SolverDPLL implements Solver {
	private boolean solved;
	private int[] interpretation;
	private CNF formula;
	private Trace trace = null;
	
	public SolverDPLL() {
		this.solved = false;
		// TODO Auto-generated constructor stub
	}
	
	public SolverDPLL(CNF formula) {
		this.formula = formula;
		this.interpretation = this.formula.getVariables().getInterpretation();
	}
	
	/**
	 * Remove all clauses satisfied by the assignement of var_id
	 * Remove all negative literals caused by the assignment from the remaining clauses
	 * @throws CNFException 
	 */
	public void removeSatClauses(int var_id, CNF phi) throws CNFException {
		ArrayList<Integer> clauses_id = phi.getClauseID();
		int lit_pos;
		int lit_neg;
		//we get the index of the positive and negative literal according to the variable valuation
		if(phi.getInterpretation()[var_id] == 1) {
			lit_pos = var_id;
			lit_neg = phi.getLiterals().length-1-var_id;
		}else if(phi.getInterpretation()[var_id] == 0) {
			lit_pos = phi.getLiterals().length-1-var_id;
			lit_neg = var_id;
		}else {
			throw new CNFException("Trying to remove clauses with an unassigned variable : " + var_id); 
		}
		//every clauses containing literal[lit_pos] is satisfied, we can remove them
		for(int id : clauses_id) {
			if(phi.getClauses().get(id).hasLit(lit_pos)) {
				phi.removeClause(id);
			}
		}
		
		//we remove every negative literals
		clauses_id = phi.getClauseID();
		for(int id : clauses_id) {
			phi.getClauses().get(id).removeLiteral(lit_neg);
		}
	}
	
	
	/**
	 * Check if there are any unit clauses. 
	 * If there are returns variable ID and assignment
	 * If there aren't returns [-1];
	 * @param phi
	 * @return
	 */
	public int[] unitPropagation(CNF phi) {
		int[] res = {-1};
		ArrayList<Integer> clauses_id = phi.getClauseID();
		for(int id : clauses_id) {
			Clause c = phi.getClauses().get(id);
			if(c.isUnit()) {
				int lit_id = c.getLiterals().get(0);
				int var_id = phi.getLiterals()[lit_id].getId();
				int val = 1;
				if(phi.getLiterals()[lit_id].isNeg()) {
					val = 0;
				}
				res = new int[2];
				res[0] = var_id;
				res[1] = val;
				return res;
			}
		}
		return res;	
	}
	
	@Override
	/**
	 * Implement an iterative version of a DPLL algorithm.
	 * No optimization on the choice of variables.
	 */
	public boolean solve(CNF phi) throws CNFException, SolverTimeoutException {
		long start_time = System.nanoTime();
		LinkedList<Integer> VariablesLeft = phi.getUnassigned();
		LinkedList<PairVariableFormula> VariablesExplored = new LinkedList<PairVariableFormula>();
		int var = -1;
		boolean backtracking = false;
		long elapsed_time;
		while(true) {
			if(phi.satSituation() == 1) {
				this.solved = true;
				return true;
			}
			elapsed_time = (System.nanoTime()-start_time)/1_000_000_000;
			if(elapsed_time > 5) {
				throw new SolverTimeoutException("Over 5 seconds");
			}
			int val;
			int[] assigned_var = {0};
			boolean empty = false;
			if(!backtracking) {
				//case of a fresh variable: start from val = 0
				try {
					var = VariablesLeft.pop();
				}catch(NoSuchElementException e) {
					//we have no more variables left and haven't found a conflict, it's SAT
					//in the weird case the formula is empty with no variable:
					if(phi.hasEmptyClause()) {
						this.solved = false;
						return false;
					}
					this.solved = true;
					this.interpretation = phi.getInterpretation();
					return true;
				}
				//we have a variable to check.
				//get a fresh clone
				CNF phi1 = Tools.cleanClone(phi);
				val = 0;
				phi.getVariables().setVal(var, val);
				phi1.getVariables().setVal(var,1);
				//if it fails we'll look for the other branch: the assignment to 1
				VariablesExplored.push(new PairVariableFormula(var,phi1)); 
				//we run unit propagation until impossible;
				assigned_var = new int[1];
				assigned_var[0] = -2;
				empty = false;
				while(true) {
					elapsed_time = (System.nanoTime()-start_time)/1_000_000_000;
					if(elapsed_time > 5) {
						throw new SolverTimeoutException("Over 5 seconds");
					}
					removeSatClauses(var, phi);
					empty = phi.hasEmptyClause();
					if(empty) {
						break;
					}
					assigned_var = unitPropagation(phi);
					if(assigned_var[0] == -1) {//we have found no new assignment due to pure literal propagation
						break;
					}

					phi.getVariables().setVal(assigned_var[0], assigned_var[1]);
					removeSatClauses(assigned_var[0], phi);

					empty = phi.hasEmptyClause();
					if(empty) {
						break;
					}
				}
				if(!empty) {
					//we found no empty clause. We can keep looking with the next variable.
					VariablesLeft = phi.getUnassigned();
					backtracking = false;
					continue;
				}else {
					//conflict found, we try the other assignment
					PairVariableFormula cur_pair = VariablesExplored.pop();
					var = cur_pair.getVariable();
					phi = cur_pair.getFormula();
					assigned_var[0] = 0;
					while(true) {
						removeSatClauses(var, phi);
						empty = phi.hasEmptyClause();
						if(empty) {
							break;
						}
						assigned_var = unitPropagation(phi);
						if(assigned_var[0] == -1) {
							break;
						}
						phi.getVariables().setVal(assigned_var[0], assigned_var[1]);
						removeSatClauses(assigned_var[0], phi);
						empty = phi.hasEmptyClause();
						if(empty) {
							break;
						}
					}if(!empty) {
						//we can continue, no backtracking
						VariablesLeft = phi.getUnassigned();
						backtracking = false;
						continue;
					}else {
						//we have an empty clause on second assignement: need to backtrack
						backtracking = true;
						continue;
					}
				}
				
			}else { //we are backtracking
				PairVariableFormula cur_pair;
				try {
					 cur_pair = VariablesExplored.pop();
				}catch(NoSuchElementException e) {
					//in this case we fail: nowhere to backtrack to
					this.solved = false;
					return false;
				}
				
				var = cur_pair.getVariable();
				phi = cur_pair.getFormula();
				//no need to get a specific val, when backtracking we know we have only assignment 1 left.
				//no need to add to explored variables: we won't backtrack again to here
				assigned_var[0] = 0;
				while(true) {
					removeSatClauses(var, phi);
					empty = phi.hasEmptyClause();
					if(empty) {
						break;
					}
					assigned_var = unitPropagation(phi);
					if(assigned_var[0] == -1) {
						break;
					}
					phi.getVariables().setVal(assigned_var[0], assigned_var[1]);
					removeSatClauses(assigned_var[0], phi);
					empty = phi.hasEmptyClause();
					if(empty) {
						break;
					}
				}if(!empty) {
					//we can continue 
					VariablesLeft = phi.getUnassigned();
					backtracking = false;
					continue;
				}else {
					//we have an empty clause on second assignement we have to try to backtrack further
					backtracking = true;
					continue;
				}
			}
		}			
	}
	
	
	
	/**
	 * Implement a first version of a DPLL algorithm. It uses unit propagation but no pure literal elimination.
	 * No optimization on the choice of variables.
	 */
	public boolean solveNoLitElim(CNF phi) throws CNFException, SolverTimeoutException {
		long start_time = System.nanoTime();
		LinkedList<Integer> VariablesLeft = phi.getUnassigned();

		LinkedList<PairVariableFormula> VariablesExplored = new LinkedList<PairVariableFormula>();
		int var = -1;
		boolean backtracking = false;
		long elapsed_time;
		while(true) {
			elapsed_time = (System.nanoTime()-start_time)/1_000_000_000;
			if(elapsed_time > 10) {
				throw new SolverTimeoutException("Over 10 seconds");
			}
			int val;
			boolean empty = false;
			if(!backtracking) {
				//case of a fresh variable: start from val = 0
				try {
					var = VariablesLeft.pop();
				}catch(NoSuchElementException e) {
					//we have no more variables left and haven't found a conflict, it's SAT
					//in the weird case the formula is empty with no variable:
					if(phi.hasEmptyClause()) {
						this.solved = false;
						return false;
					}
					this.solved = true;
					this.interpretation = phi.getInterpretation();
					return true;
				}
				//we have a variable to check.
				//get a fresh clone
				CNF phi1 = Tools.cleanClone(phi);
				val = 0;
				phi.getVariables().setVal(var, val);
				phi1.getVariables().setVal(var,1);
				//if it fails we'll look for the other branch: the assignment to 1
				VariablesExplored.push(new PairVariableFormula(var,phi1)); 
				//we run unit propagation and pure literal elimination until impossible;
				empty = false;
				removeSatClauses(var, phi);
				empty = phi.hasEmptyClause();

				if(!empty) {
					//we found no empty clause. We can keep looking with the next variable.
					VariablesLeft = phi.getUnassigned();
					backtracking = false;
					continue;
				}else {
					//conflict found, we try the other assignment
					PairVariableFormula cur_pair = VariablesExplored.pop();
					var = cur_pair.getVariable();
					phi = cur_pair.getFormula();
					removeSatClauses(var, phi);
					empty = phi.hasEmptyClause();

					if(!empty) {
						//we can continue, no backtracking
						VariablesLeft = phi.getUnassigned();
						backtracking = false;
						continue;
					}else {
						//we have an empty clause on second assignement: need to backtrack
						backtracking = true;
						continue;
					}
				}
				
			}else { //we are backtracking
				PairVariableFormula cur_pair;
				try {
					 cur_pair = VariablesExplored.pop();
				}catch(NoSuchElementException e) {
					//in this case we fail: nowhere to backtrack to
					this.solved = false;
					return false;
				}
				
				var = cur_pair.getVariable();
				phi = cur_pair.getFormula();
				//no need to get a specific val, when backtracking we know we have only assignment 1 left.
				//no need to add to explored variables: we won't backtrack again to here
				removeSatClauses(var, phi);
				empty = phi.hasEmptyClause();
				if(!empty) {
					//we can continue 
					VariablesLeft = phi.getUnassigned();
					backtracking = false;
					continue;
				}else {
					//we have an empty clause on second assignement we have to try to backtrack further
					backtracking = true;
					continue;
				}
			}
		}			
	}
	
	/**
	 * Recursive version of DPLL algorithm
	 * Only handle the recursive calls
	 * @throws CNFException 
	 */
	public boolean solveAppelRec(CNF phi, int i,Trace trace,long start_time) throws CNFException, SolverTimeoutException {
		long elapsed_time = (System.nanoTime()-start_time)/1_000_000_000;
		if(elapsed_time > 10) {
			throw new SolverTimeoutException("Over 10 seconds");
		}
		int state = phi.satSituation();
		//base cases
		if(state == 1) {
			this.solved = true;
			return true;
		}

		if(i >= phi.getVariables().getSize()) { //all variables have been tested
			this.solved = false;
			return false;
		}
		
		//we run unit prob on all variables
		
		CNF phi1 = Tools.cleanClone(phi);
		CNF phi2 = Tools.cleanClone(phi);
		phi1.getVariables().setVal(i, 0);
		removeSatClauses(i, phi1);
		phi2.getVariables().setVal(i, 1);
		removeSatClauses(i, phi2);
		return solveAppelRec(phi1,i+1,trace,start_time) || solveAppelRec(phi2,i+1,trace,start_time);		
	}
	
	/**
	 * Recursive version of DPLL. Not optimized.
	 * @param phi
	 * @return
	 * @throws CNFException
	 * @throws SolverTimeoutException 
	 */
	public boolean solveRec(CNF phi) throws CNFException, SolverTimeoutException {
		Trace trace = new Trace(null);
		trace.setRoot(new PairVariableFormula(0, phi));
		long cur_time = System.nanoTime();
		return solveAppelRec(phi,0,trace,cur_time);
	}
	
	
	@Override
	public void printRes() {
		if(this.solved) {
			System.out.println("SAT");
		}else {
			System.out.println("UNSAT");
		}
	}

	public void updateSolver(CNF formula) {
		this.formula = formula;
		this.interpretation = formula.getVariables().getInterpretation();
		this.solved = false;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	@Override
	public int[] getInterpretation() {
		return interpretation;
	}

	public void setInterpretation(int[] interpretation) {
		this.interpretation = interpretation;
	}

	public CNF getFormula() {
		return formula;
	}

	public void setFormula(CNF formula) {
		this.formula = formula;
	}
	
	public Trace getTrace() {
		return trace;
	}

}
