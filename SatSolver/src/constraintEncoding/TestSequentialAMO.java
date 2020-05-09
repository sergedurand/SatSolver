package constraintEncoding;

import booleanFormula.CNFException;
import solver.SolverTimeoutException;

public class TestSequentialAMO {

	public static void main(String[] args) throws SolverTimeoutException, CNFException, AMOException {
		SequentialAMO a = new SequentialAMO();
		a.test(8);
	}

}
