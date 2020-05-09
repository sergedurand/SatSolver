package constraintEncoding;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import solver.SolverTimeoutException;

/**
 * AMO (at most one) are used to add clauses satisfied iff at most one variable is true
 * @author serge
 *
 */

public interface AMO {

	public void addConstraint(int[] variables,CNF phi) throws AMOException, CNFException;
	public void test(int n) throws CNFException, AMOException, SolverTimeoutException;
	
}
