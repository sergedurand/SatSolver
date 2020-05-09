package constraintEncoding;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import solver.SolverTimeoutException;

public class CommanderAMO implements AMO {

	@Override
	public void addConstraint(int[] variables, CNF phi) throws AMOException, CNFException {
		NaiveAMO a = new NaiveAMO();
		int m = (int) Math.ceil(variables.length/2);
		
	}

	@Override
	public void test(int n) throws CNFException, AMOException, SolverTimeoutException {
		// TODO Auto-generated method stub

	}

}
