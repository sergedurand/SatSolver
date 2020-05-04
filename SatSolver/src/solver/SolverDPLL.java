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
	public CNF removeSatClauses(int var_id, CNF phi) throws CNFException {
		CNF res = Tools.cleanClone(phi);
		ArrayList<Integer> clauses_id = res.getClauseID();
		int lit_pos;
		int lit_neg;
		//we get the index of the positive and negative literal according to the variable valuation
		if(res.getInterpretation()[var_id] == 1) {
			lit_pos = var_id;
			lit_neg = res.getLiterals().length-1-var_id;
		}else if(res.getInterpretation()[var_id] == 0) {
			lit_pos = res.getLiterals().length-1-var_id;
			lit_neg = var_id;
		}else {
			throw new CNFException("Trying to remove clauses with an unassigned variable : " + var_id); 
		}
		//every clauses containing literal[lit_pos] is satisfied, we can remove them
		for(int id : clauses_id) {
			if(res.getClauses().get(id).hasLit(lit_pos)) {
				res.removeClause(id);
			}
		}
		
		//we remove every negative literals
		clauses_id = res.getClauseID();
		for(int id : clauses_id) {
			res.getClauses().get(id).removeLiteral(lit_neg);
		}
		return res;
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
	 * Implement a first version of a DPLL algorithm. It uses unit propagation and pure literal elimination.
	 * No optimization on the choice of variables.
	 */
	public void solve(CNF formula) throws CNFException {
		
		LinkedList<Integer> VariablesLeft = new LinkedList<Integer>();
	}
	
	
	/**
	 * Recursive version of DPLL algorithm
	 * Only handle the recursive calls
	 * @throws CNFException 
	 */
	public boolean solveAppelRec(CNF phi, int i,Trace trace,long start_time) throws CNFException, SolverTimeoutException {
		long elapsed_time = (System.nanoTime()-start_time)/1_000_000_000;
		if(elapsed_time > 10) {
			throw new SolverTimeoutException("Over 5 seconds");
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
		phi1 = unitProb(i, phi1);
		phi2.getVariables().setVal(i, 1);
		phi2 = unitProb(i, phi2);
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
	
	private void updateInterpretation() {
		this.interpretation = this.formula.getVariables().getInterpretation();
	}

	public Trace getTrace() {
		return trace;
	}

}
