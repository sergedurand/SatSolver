package solver;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import tools.DimacsParser;
import tools.SolverComparator;

public class TestHeuristics {

	public static void main(String[] args) throws CNFException {
		// TODO Auto-generated method stub
		String filename = "./SAT/ais12.cnf";
		SolverTwl s1 = new SolverTwl();
		SolverComparator.compareOneSolver(s1, "./SAT/ais12.cnf", 25);
		
		
		SolverTwlHeuristics s = new SolverTwlHeuristics();

		for(int i = 1; i<5;i ++) {
			CNF phi = DimacsParser.CNFfromDIMACS2(filename);
			try {			
				long start = System.nanoTime();
				int t = (int) phi.getClauses().size()*i/2;
				s.solve2(phi, 15, t);
				long elapsed = (System.nanoTime()-start)/1000000;
				System.out.println("i = " + i +" time =  " + elapsed +" ms");
				System.out.println("threshold = " + t);
			} catch (SolverTimeoutException e) {
				// TODO Auto-generated catch block
				System.out.println(" i = " + i + ", trop long abandon");
				e.printStackTrace();
			}
			
		}
	}

}
