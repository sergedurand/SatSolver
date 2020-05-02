package solver;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Variables;

public class SolverNaive implements Solver {
	
	private CNF formula;
	private int[] interpretation;
	private boolean solved;
	
	
	public SolverNaive() {
		this.solved = false;
		// TODO Auto-generated constructor stub
	}
	
	public SolverNaive(CNF formula) {
		this.formula = formula;
		this.interpretation = formula.getVariables().getInterpretation();
	}
	
	/**
	 * Naive solving. Test all possible variables assignment
	 * Returns null if there is no valid assignment, else returns the first found valid assignment
	 * @throws CNFException 
	 */
	@Override
	public void solve(CNF formula) throws CNFException {
		this.updateSolver(formula);
		
		Deque<Integer> variablesLeft = new LinkedList<Integer>();
		for(int i = 0;i<this.formula.getVariables().getSize();i++) {
			variablesLeft.push(i);
		}
		
		Deque<Integer> variablesVisited = new LinkedList<Integer>();
		
		
		int state = this.formula.satSituation();
		int cur_var;
		while(true) {
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
	
	/**
	 * Return the binary string of i (itself a binary string)
	 * coded on max bits
	 * @param s
	 * @param max
	 * @return
	 */
	public String nextInt(String s,int max) {
		int i = Integer.parseInt(s,2);
		int res = i+1;
		if(res > Math.pow(2, max)-1){
			return null;
		}
		String res_s = Integer.toBinaryString(res);
		while(res_s.length() < max) {
			res_s = "0"+res_s;
		}
		return res_s;
		
	}
	
	private void updateInterpretation() {
		this.interpretation = this.formula.getVariables().getInterpretation();
	}
	
	
	public void updateSover() {
		this.formula = null;
		this.interpretation = null;
		this.solved = false;
	}
	
	public void updateSolver(CNF formula) {
		this.formula = formula;
		this.interpretation = formula.getVariables().getInterpretation();
		this.solved = false;
	}
	
	@Override
	public void printRes() {
		if(this.solved) {
			System.out.println("SAT");
		}else {
			System.out.println("UNSAT");
		}
	}
	

}
