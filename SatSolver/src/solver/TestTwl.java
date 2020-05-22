package solver;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import tools.DimacsParser;
import tools.Tools;

public class TestTwl {

	public static void main(String[] args) throws CNFException, SolverTimeoutException {
		// TODO Auto-generated method stub
		SolverTwlHeuristics s = new SolverTwlHeuristics();
		CNF phi = DimacsParser.CNFfromDIMACS2("./HARD/bf1355-075.cnf");
		long start = System.nanoTime();
		System.out.println(s.solve2(phi, 4500,900));
		long elapsed = (System.nanoTime()-start)/1000000;
		System.out.println(elapsed + " ms");
		
//		for(int i = 15; i<26;i += 1) {
//			CNF phi = DimacsParser.CNFfromDIMACS2("./UNSAT/dubois20.cnf");
//			long start = System.nanoTime();
//			s.solve2(phi, 20, phi.getClauses().size()/i);
//			long elapsed = (System.nanoTime()-start)/1000000;
//			System.out.println("i = " + i +" time =  " + elapsed +" ms");
//			System.out.println("threshold = " + (phi.getClauses().size()/i));
//		}
//		
//		CNF phi2 = DimacsParser.CNFfromDIMACS2("./UNSAT/dubois20.cnf");
//		SolverTwl s2 = new SolverTwl();
//		long start = System.nanoTime();
//		s2.solve2(phi2, 20);
//		long elapsed = (System.nanoTime()-start)/1000000;
//		System.out.println("Twl sans heuristique : " + elapsed + " ms");
	}

}
