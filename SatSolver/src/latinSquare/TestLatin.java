package latinSquare;

import java.util.ArrayList;
import java.util.HashMap;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Variables;
import constraintEncoding.AMOException;
import constraintEncoding.BinaryAMO;
import constraintEncoding.NaiveAMO;
import solver.SolverDPLL;
import solver.SolverTimeoutException;
import tools.Tools;

public class TestLatin {

	public static void main(String[] args) throws CNFException, SolverTimeoutException, AMOException {
		// TODO Auto-generated method stub
		SolverDPLL s = new SolverDPLL();
		int size = 2;
		NaiveAMO a = new NaiveAMO();
		LatinSquare sq2 = new LatinSquare(a,size);
		System.out.println(sq2.getPhi().gridToString(size));
		CNF phi = Tools.cleanClone(sq2.getPhi());
		sq2.solveSquare(s);
		Tools.printInterpration(sq2.getPhi().getInterpretation());
		sq2.printStat();
		System.out.println(phi.gridToString(size));
		s.solve(phi);
		s.printRes();
		Tools.printInterpration(s.getInterpretation());
		System.out.println(sq2);
		
	}

}
