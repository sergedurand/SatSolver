package solver;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import tools.DimacsParser;

public class TestHeuristics {

	public static void main(String[] args) throws CNFException {
		// TODO Auto-generated method stub
		String filename = args[0];
		
		SolverTwlHeuristics s = new SolverTwlHeuristics();

		for(int i = 15; i<26;i += 1) {
			CNF phi = DimacsParser.CNFfromDIMACS2(filename);
			try {			
				long start = System.nanoTime();
				s.solve2(phi, 20, phi.getClauses().size()/i);
				long elapsed = (System.nanoTime()-start)/1000000;
				System.out.println("i = " + i +" time =  " + elapsed +" ms");
				System.out.println("threshold = " + (phi.getClauses().size()/i));
			} catch (SolverTimeoutException e) {
				// TODO Auto-generated catch block
				System.out.println(" i = " + i + ", trop long abandon");
				e.printStackTrace();
			}
			
		}
	}

}
