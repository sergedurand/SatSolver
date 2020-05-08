package latinSquare;

import java.util.ArrayList;
import java.util.HashMap;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import booleanFormula.Clause;
import booleanFormula.Variables;
import solver.SolverDPLL;
import solver.SolverTimeoutException;
import tools.Tools;

public class TestLatin {

	public static void main(String[] args) throws CNFException, SolverTimeoutException {
		// TODO Auto-generated method stub
		int size = 2;
		LatinSquare sq2 = new LatinSquare(size);
		System.out.println(sq2.getPhi().gridToString(size));
		CNF phi = sq2.getPhi();
		phi.printStat();
		SolverDPLL s = new SolverDPLL();
		System.out.println(s.solve(phi));
		sq2.solveSquare(s);
		System.out.println(sq2);
	}

}
