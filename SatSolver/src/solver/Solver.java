package solver;

import booleanFormula.CNF;
import booleanFormula.Variables;

public interface Solver {
	public Variables solve(CNF formula);
	
}
