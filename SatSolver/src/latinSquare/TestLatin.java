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
import constraintEncoding.SequentialAMO;
import solver.SolverDPLL;
import solver.SolverDPLLOptimized;
import solver.SolverDPLLOptimized2;
import solver.SolverTimeoutException;
import solver.SolverTwl;
import solver.SolverTwlHeuristics;
import tools.ModelParser;
import tools.Tools;

public class TestLatin {

	public static void main(String[] args) throws CNFException, SolverTimeoutException, AMOException {
		// TODO Auto-generated method stub
		SolverDPLLOptimized s = new SolverDPLLOptimized();
		SolverDPLLOptimized2 s2 = new SolverDPLLOptimized2();
		SolverTwl s3 = new SolverTwl();
		SolverTwlHeuristics s4 = new SolverTwlHeuristics();
		int size = 12;
		LatinSquare sq = new LatinSquare(size);
		CNF phi = sq.getPhi();
		Tools.DimacsFromCNF(phi, "latin_sq_12", "");
		long start = System.nanoTime();
		sq.solveSquare(s4);
		long elapsed = (System.nanoTime()-start)/1000000;
		System.out.println(elapsed + " ms");
		
	}

}
