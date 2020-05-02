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
import booleanFormula.Variables;
import tools.Tools;

public class SolverDPLL implements Solver {
	private boolean solved;
	private int[] interpretation;
	private CNF formula;
	
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
		}else {
			pos = phi.getLiterals()[phi.getLiterals().length-var_id-1];
			idx_neg = var_id;
			neg = phi.getLiterals()[var_id];
		}
		
		
		//first we remove the clauses containing the positive literal: unit propagation
		if(pos != null) {
			PriorityBlockingQueue<Integer> clauses = pos.getClauses();
			for(int clause_ID : clauses) {
				this.formula.removeClause(clause_ID);
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
		int new_var = -1;
		for(Iterator<HashMap.Entry<Integer,Clause>> e = phi.getClauses().entrySet().iterator();e.hasNext();) {
			Clause c = e.next().getValue();
			if(c.isUnit()){
				Literal unique_lit = phi.getLiterals()[c.getLiterals().get(0)];
				new_var = unique_lit.getId();
				if(unique_lit.isNeg()) {
					phi.getVariables().setVal(new_var, 0);
				}else {
					phi.getVariables().setVal(new_var, 1);
				}
				break;
			}
		}
		if(new_var != -1) {
			this.updateInterpretation();
			phi = this.unitProb(new_var, phi);
		}else {
			return phi;
		}
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
			variablesLeft.push(var);
		}
		
		Deque<PairVariableFormula> variablesVisited = new LinkedList<PairVariableFormula>();
		
		
		int cur_var;
		PairVariableFormula cur_pair;
		while(true) {
			try {
				cur_pair = variablesLeft.pop();
				cur_var = cur_pair.getVariable();
				this.formula = cur_pair.getFormula();
			}catch(NoSuchElementException e) {
				this.solved = true;
				return;
			}
			
			CNF cur_formula = Tools.cloneFormula(this.formula);
			int val = 0;
			this.formula.getVariables().setVal(cur_var, val);
			this.updateInterpretation();
			cur_pair.setFormula(this.formula);
			this.unitProb(cur_var);
			cur_pair.setFormula(this.formula);
			
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
				continue;
			}else {
				//we forget changes made by unit prob and try again with val = 1
				this.formula = cur_formula;
				cur_pair.setFormula(cur_formula);
				val = 1;
				this.formula.getVariables().setVal(cur_var, val);
				this.updateInterpretation();
				cur_pair.setFormula(this.formula);
				this.unitProb(cur_var);
				cur_pair.setFormula(this.formula);
				
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
					continue;
				}else {//this time we have to backtrack before the first assignment
					try {
						cur_pair = variablesVisited.pop();
					}catch(NoSuchElementException e) { //no more choice = unsatisfiable
						this.solved = false;
						return;
					}
				}
			}
			
		}
	}
	
	
	/**
	 * Recursive version of DPLL algorithm
	 * @throws CNFException 
	 */
	public boolean solveRec(CNF phi) throws CNFException {
		int state = phi.satSituation();
		if(state == 1) {
			return true;
		}
		if(state == 0) {
			return false;
		}
		
		for(int i = 0;i<phi.getVariables().getSize();i++) {
			phi = unitProb(i, phi);
		}
		phi.add
		
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
}
