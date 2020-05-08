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
		SolverDPLL s = new SolverDPLL();
		int size = 7;
		LatinSquare sq2 = new LatinSquare(size);
		CNF phi = Tools.cleanClone(sq2.getPhi());
		System.out.println(phi);
		sq2.solveSquare(s);
		System.out.println(sq2);
		
	}

}
