package solver;

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.PriorityBlockingQueue;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Literal;
import booleanFormula.Variables;

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
	public void unitProb(int var_id) throws CNFException {
		Literal pos;
		Literal neg;
		int idx_neg;
		if(this.formula.getVariables().getVal(var_id)==1) {
			//if var_id = 1, i.e. the variable is x2, and I(x2) = 1, 
			//the literal x2 is positif and the literal neg(x2) is negative. If I(x2) = 0 it's the opposite
			//reminder: we store all the possible literals in a fixed array:
			//[x1,x2,...xn,neg(xn),neg(xn-1)...,neg(x1)]
			pos = this.formula.getLiterals()[var_id];
			idx_neg = this.formula.getLiterals().length-var_id-1;
			neg = this.formula.getLiterals()[idx_neg];
		}else {
			pos = this.formula.getLiterals()[this.formula.getLiterals().length-var_id-1];
			idx_neg = var_id;
			neg = this.formula.getLiterals()[var_id];
		}
		
		//first we remove the clauses containing the positive literal: unit propagation
		PriorityBlockingQueue<Integer> clauses = pos.getClauses();
		for(int clause_ID : clauses) {
			this.formula.removeClause(clause_ID);
		}
		
		//we remove the remaining literals: being negative they don't add value
		clauses = neg.getClauses();
		for(int clause_ID : clauses) {
			this.formula.getClauses().get(clause_ID).removeLiteral(idx_neg);
		}
		
		//if we have new assignement possible we can do a recursive call
		int new_var = -1;
		for(Iterator<HashMap.Entry<Integer,Clause>> e = this.formula.getClauses().entrySet().iterator();e.hasNext();) {
			Clause c = e.next().getValue();
			if(c.isUnit()){
				Literal unique_lit = this.formula.getLiterals()[c.getLiterals().get(0)];
				new_var = unique_lit.getId();
				if(unique_lit.isNeg()) {
					this.formula.getVariables().setVal(new_var, 0);
				}else {
					this.formula.getVariables().setVal(new_var, 1);
				}
				break;
			}
		}
		if(new_var != -1) {
			this.unitProb(new_var);
		}

	}
	
	
	@Override
	/**
	 * Implement a first version of a DPLL algorithm. It uses unit propagation and pure literal elimination.
	 * No optimization on the choice of variables.
	 */
	public void solve(CNF formula) throws CNFException {
		this.updateSolver(formula);
		
		Deque<Integer> variablesLeft = new LinkedList<Integer>();
		for(int i = 0;i<this.formula.getVariables().getSize();i++) {
			variablesLeft.push(i);
		}
		
		Deque<Integer> variablesVisited = new LinkedList<Integer>();
		
		
		int state = this.formula.satSituation();
		int cur_var;
		while(state != 1) {
			try {
				cur_var = variablesLeft.pop();
			}catch(NoSuchElementException e) {
				this.solved = true;
				return;
			}
			variablesVisited.push(cur_var);
			int val = 0;
			this.formula.getVariables().setVal(cur_var, val);
			this.updateInterpretation();
			state = this.formula.satSituation();
			if(state == -1 && val == 0) {
				val = 1;
				this.formula.getVariables().setVal(cur_var, val);
				this.updateInterpretation();
				state = this.formula.satSituation();
			}
			if(state == -1 && val == 1) {
				try {
				cur_var = variablesVisited.pop();
				}catch(NoSuchElementException e) {
					this.solved = false;
					return;
				}
			}
		}
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
