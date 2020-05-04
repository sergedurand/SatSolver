package solver;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Variables;
import tools.Tools;

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
	public boolean solve(CNF formula) throws CNFException {
		this.updateSolver(formula);
		
		LinkedList<Integer> variablesLeft = new LinkedList<Integer>();
		for(int i = 0;i<this.formula.getVariables().getSize();i++) {
			variablesLeft.add(i);
		}
		
		LinkedList<Integer> variablesVisited = new LinkedList<Integer>();
		
		
		int state = this.formula.satSituation();
		int cur_var;
		while(true) {
			try {
				cur_var = variablesLeft.pop();
			}catch(NoSuchElementException e) {
				this.solved = true;
				return true;
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
					return false;
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
	
	public boolean solveRecCall(CNF phi, int i,long start_time) throws CNFException, SolverTimeoutException {
		long elapsed_time = (System.nanoTime()-start_time)/1_000_000_000;
		if(elapsed_time > 2) {
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
		
		CNF phi1 = Tools.cleanClone(phi);
		CNF phi2 = Tools.cleanClone(phi);
		phi1.getVariables().setVal(i, 0);
		phi2.getVariables().setVal(i, 1);
		return solveRecCall(phi1,i+1,start_time) || solveRecCall(phi2,i+1,start_time);		
	}
	
	public boolean solveRec(CNF phi) throws SolverTimeoutException, CNFException {
		long start_time = System.nanoTime();
		return solveRecCall(phi,0,start_time);
	}
	
	

}
