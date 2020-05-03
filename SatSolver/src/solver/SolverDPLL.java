package solver;

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
	 * Performs unit propagation and pure literal elimination
	 * @throws CNFException 
	 */
	public CNF unitProb(int var_id, CNF phi) throws CNFException {
		Literal pos;
		Literal neg;
		int idx_neg;
		if(phi.getVariables().getVal(var_id)==1) {
			//if var_id = 1, i.e. the variable is x2, and I(x2) = 1, 
			//the literal x2 is positif and the literal neg(x2) is negative. If I(x2) = 0 it's the opposite
			//reminder: we store all the possible literals in a fixed array:
			//[x1,x2,...xn,neg(xn),neg(xn-1)...,neg(x1)]
			pos = phi.getLiterals()[var_id];
			idx_neg = phi.getLiterals().length-var_id-1;
			neg = phi.getLiterals()[idx_neg];
		}else if(phi.getVariables().getVal(var_id)==0) {
			pos = phi.getLiterals()[phi.getLiterals().length-var_id-1];
			idx_neg = var_id;
			neg = phi.getLiterals()[var_id];
		}else {//we are in the case of an unassigned variable
			return phi;
		}
		
		
		//first we remove the clauses containing the positive literal: unit propagation
		if(pos != null) {
			PriorityBlockingQueue<Integer> clauses = pos.getClauses();
			for(int clause_ID : clauses) {
				phi.removeClause(clause_ID);
			}
		}
		
		//we remove the remaining literals: being negative they don't add value
		if( neg != null) {
			PriorityBlockingQueue<Integer> clauses = neg.getClauses();
			for(int clause_ID : clauses) {
				if(!phi.getClauses().get(clause_ID).isEmpty()) {
					phi.getClauses().get(clause_ID).removeLiteral(idx_neg);
				}
			}
		}
		phi.getLiterals()[idx_neg] = null; //the literal is not active in the formula anymore
		
		//if we have new assignement possible we can do a recursive call
//		int new_var = -1;
//		for(Iterator<HashMap.Entry<Integer,Clause>> e = phi.getClauses().entrySet().iterator();e.hasNext();) {
//			Clause c = e.next().getValue();
//			if(c.isUnit()){
//				Literal unique_lit = phi.getLiterals()[c.getLiterals().get(0)];
//				new_var = unique_lit.getId();
//				if(unique_lit.isNeg()) {
//					phi.getVariables().setVal(new_var, 0);
//				}else {
//					phi.getVariables().setVal(new_var, 1);
//				}
//				break;
//			}
//		}
//		if(new_var != -1) {
//			phi = this.unitProb(new_var, phi);
//		}else {
//			return phi;
//		}
		return phi;

	}
	
	
	@Override
	/**
	 * Implement a first version of a DPLL algorithm. It uses unit propagation and pure literal elimination.
	 * No optimization on the choice of variables.
	 */
	public void solve(CNF formula) throws CNFException {
		this.updateSolver(formula);
		Deque<PairVariableFormula> variablesLeft = new LinkedList<PairVariableFormula>();
		
		for(int i = 0;i<this.formula.getVariables().getSize();i++) {
			PairVariableFormula var = new PairVariableFormula(i,this.formula);
			System.err.println(i);
			variablesLeft.add(var);
		}
		
		Deque<PairVariableFormula> variablesVisited = new LinkedList<PairVariableFormula>();
		
		Trace cur_trace = new Trace(null);
		int cur_var;
		PairVariableFormula cur_pair;
		TracePrinter p = new TracePrinter(); 
		int i = 0;
		while(true) {
			try {
				cur_pair = variablesLeft.pop();
				cur_var = cur_pair.getVariable();
				this.formula = cur_pair.getFormula();
				cur_trace.setRoot(cur_pair);
				i += 1;
			}catch(NoSuchElementException e) {
				this.solved = true;
				while(cur_trace.getParent() != null) {
					cur_trace = cur_trace.getParent();
				}
				this.trace = trace;
				return;
			}
			
			CNF cur_formula = Tools.cloneFormula(this.formula);
			int val = 0;
			this.formula.getVariables().setVal(cur_var, val);
			this.updateInterpretation();
			cur_pair.setFormula(this.formula);
			this.formula = this.unitProb(cur_var,this.formula);
			cur_pair.setFormula(this.formula);
			cur_trace.setLeft(new Trace(cur_pair));
			//checking empty clause
			boolean hasEmptyClause = false;
			for(HashMap.Entry<Integer,Clause> e : this.formula.getClauses().entrySet()) {
				if(e.getValue().isEmpty()) {
					hasEmptyClause = true;
					break;
				}
			}
			if(!hasEmptyClause) {
				variablesVisited.push(cur_pair);
				cur_trace = cur_trace.getLeft();
				continue;
			}else {
				//we forget changes made by unit prob and try again with val = 1
				this.formula = cur_formula;
				cur_pair.setFormula(cur_formula);
				val = 1;
				this.formula.getVariables().setVal(cur_var, val);
				this.updateInterpretation();
				cur_pair.setFormula(this.formula);
				this.formula = this.unitProb(cur_var,this.formula);
				cur_pair.setFormula(this.formula);
				cur_trace.setRight(new Trace(cur_pair));
				//checking empty clause
				boolean hasEmptyClause_bis = false;
				for(HashMap.Entry<Integer,Clause> e : this.formula.getClauses().entrySet()) {
					if(e.getValue().isEmpty()) {
						hasEmptyClause_bis = true;
						break;
					}
				}
				if(!hasEmptyClause_bis) {//no empty clause, we can keep searching
					this.formula=cur_pair.getFormula();
					this.updateInterpretation();
					variablesVisited.push(cur_pair);
					cur_trace = cur_trace.getRight();

					continue;
				}else {//this time we have to backtrack before the first assignment
					try {
						cur_pair = variablesVisited.pop();
						cur_trace = cur_trace.getParent();
					}catch(NoSuchElementException e) { //no more choice = unsatisfiable
						this.solved = false;
						//go back to the root
						while(cur_trace.getParent() != null) {
							cur_trace = cur_trace.getParent();
						}
						this.trace = cur_trace;
						return;
					}
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
