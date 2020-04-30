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
	public int[] solve(CNF formula) throws CNFException {
		this.formula = formula;
		int max = this.formula.getVariables().getSize();
		String s = ""; //first valuation
		for(int i = 0;i<max;i++) {
			s = s+"0";
		}
		
		this.formula.getVariables().setVariablesFromString(s);
		this.updateInterpretation();
		while(!this.formula.isValid()) {
			s = nextInt(s,max);
			if(s == null) {
				System.out.println("UNSAT");
				return null;
			}
			this.formula.getVariables().setVariablesFromString(s);
			this.updateInterpretation();
		}
		System.out.println("SAT");
		return this.interpretation;
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
	
	public int[] naiveBacktrack() throws CNFException {
		
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
				System.out.println("SAT");
				return this.interpretation;
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
					System.out.println("UNSAT");
					return null;
				}
			}
		}
		System.out.println("SAT");
		return this.interpretation;
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
	

}
