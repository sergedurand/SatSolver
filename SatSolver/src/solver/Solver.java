package solver;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Variables;

public interface Solver {
	public void solve(CNF formula) throws CNFException;
	
	public void printRes();
	public void updateSolver(CNF Formula);

	public boolean solveRec(CNF phi) throws CNFException, SolverTimeoutException;
	
}
