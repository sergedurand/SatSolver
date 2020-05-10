package solver;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Variables;

public interface Solver {
	public boolean solve(CNF formula,int timeout) throws CNFException, SolverTimeoutException;
	
	public void printRes();
	public void updateSolver(CNF Formula);

	public boolean solveRec(CNF phi) throws CNFException, SolverTimeoutException;

	int[] getInterpretation();
	
}
