package solver;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import tools.DimacsParser;
import tools.SolverComparator;
import tools.Tools;

public class TestSolverDPLL {
	public static void main(String[] args) throws CNFException, SolverTimeoutException {
		SolverDPLLOptimized s1 = new SolverDPLLOptimized();
		String path = "./SAT"; 
		SolverDPLLOptimized2 s5 = new SolverDPLLOptimized2();

		SolverComparator.compareOneSolver(s1, path, 25);
		

	}

}
