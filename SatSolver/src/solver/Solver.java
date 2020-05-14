package solver;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Variables;

public interface Solver {
	public boolean solve(CNF formula,int timeout) throws CNFException, SolverTimeoutException;
	public void printRes();
	int[] getInterpretation();
	
}
