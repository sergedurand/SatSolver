package solver;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Variables;

public interface Solver {
	public int[] solve(CNF formula) throws CNFException;
	
}
