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
		SolverDPLL s2 = new SolverDPLL();
		SolverTwl s3 = new SolverTwl();
		SolverTwlHeuristics s4 = new SolverTwlHeuristics();
		SolverDPLLOptimized2 s5 = new SolverDPLLOptimized2();

		SolverComparator.compareOneSolver(s3, path, 25);
		

	}

}
