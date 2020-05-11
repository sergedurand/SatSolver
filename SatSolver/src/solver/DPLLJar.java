package solver;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import tools.DimacsParser;

public class DPLLJar {

	public static String main(String[] args) throws CNFException, SolverTimeoutException {
		// TODO Auto-generated method stub
		String filename = args[0];
		SolverDPLL s = new SolverDPLL();
		CNF phi = DimacsParser.CNFfromDIMACS2(filename);
		boolean res = s.solve(phi, Integer.MAX_VALUE);
		if(res) {
			System.out.println("SAT");
		}else {
			System.out.println("UNSAT");
		}
		return "";
	}

}
