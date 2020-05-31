package grecoLatin;

import booleanFormula.CNFException;
import constraintEncoding.AMOException;
import constraintEncoding.BinaryAMO;
import constraintEncoding.NaiveAMO;
import constraintEncoding.SequentialAMO;
import solver.SolverDPLL;
import solver.SolverTimeoutException;
import solver.SolverTwl;

public class TestFGrecoLatin {

	public static void main(String[] args) throws CNFException, AMOException, SolverTimeoutException {
		// TODO Auto-generated method stub
		NaiveAMO a = new NaiveAMO();
		grecoLatin sq = new grecoLatin(a,4);
		SolverTwl s = new SolverTwl();
		sq.printStat();
		sq.solveSquare(s);
		System.out.println(sq);
	}

}
