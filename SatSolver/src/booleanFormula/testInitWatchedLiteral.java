package booleanFormula;

import solver.SolverDPLLOptimized;
import tools.DimacsParser;

public class testInitWatchedLiteral {

	public static void main(String[] args) throws CNFException {
		// TODO Auto-generated method stub
		CNF phi = DimacsParser.CNFfromDIMACS2("./benchmark/ordering3.cnf");
		System.out.println(phi);
		SolverDPLLOptimized s = new SolverDPLLOptimized();
		s.keepPropagating(phi);
		System.out.println(phi);
		phi.initWatchedLiterals();
		System.out.println(phi.printWatchedLiterals());
	}

}
