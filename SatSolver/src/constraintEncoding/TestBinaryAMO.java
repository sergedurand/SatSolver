package constraintEncoding;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Variables;
import solver.SolverDPLL;
import solver.SolverTimeoutException;
import tools.Tools;

public class TestBinaryAMO {

	public static void main(String[] args) throws SolverTimeoutException, CNFException, AMOException {
		BinaryAMO a = new BinaryAMO();
		CNF phi = new CNF(new Variables(3));
		SolverDPLL s = new SolverDPLL();
		int[] variables = new int[3];
		for(int i = 0;i<3;i++) {
			variables[i] = i;
		}
		a.addConstraint(variables, phi);
		System.out.println(phi);
		phi.getVariables().setVal(0, 1);
		Tools.printInterpration(phi.getVariables().getInterpretation());
		s.solve(phi,10);
		s.printRes();
		Tools.printInterpration(s.getInterpretation());
		phi = s.removeSatClauses(0, phi);
		System.out.println(phi);
		System.out.println(phi.satSituation());
	}

}
