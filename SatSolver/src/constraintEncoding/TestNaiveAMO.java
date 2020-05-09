package constraintEncoding;

import booleanFormula.CNFException;
import solver.SolverTimeoutException;

public class TestNaiveAMO {

	public static void main(String[] args) throws CNFException, AMOException, SolverTimeoutException {
		// TODO Auto-generated method stub
		AMO a1 = new NaiveAMO();
		a1.test(4);

	}

}
