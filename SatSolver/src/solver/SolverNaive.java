package solver;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Variables;

public class SolverNaive implements Solver {

	public SolverNaive() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Naive solving. Test all possible variables assignment
	 * Returns null if there is no valid assignment, else returns the first found valid assignment
	 * @throws CNFException 
	 */
	@Override
	public Variables solve(CNF formula) throws CNFException {
		int max = formula.getVariables().getSize();
		String s = ""; //first valuation
		for(int i = 0;i<max;i++) {
			s = s+"0";
		}
		
		formula.getVariables().setVariablesFromString(s);
		while(!formula.isValid()) {
			s = nextInt(s,max);
			if(s == null) {
				System.out.println("UNSAT");
				return null;
			}
			formula.getVariables().setVariablesFromString(s);
		}
		System.out.println("SAT");
		return formula.getVariables();
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
	

}
