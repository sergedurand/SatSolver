package solver;
import booleanFormula.CNF;
import booleanFormula.CNFException;
import solver.SolverTimeoutException;
import solver.SolverTwl;
import solver.SolverTwlHeuristics;
import tools.DimacsParser;

public class TestThreshold {
	public static void main(String[] args) throws CNFException {
		
		String filename = args[2];
		int threshold = Integer.parseInt(args[0]);
		int timeout = Integer.parseInt(args[1]);
		
		SolverTwlHeuristics s = new SolverTwlHeuristics();
		SolverTwl s2 = new SolverTwl();
		CNF phi = DimacsParser.CNFfromDIMACS2(filename);
		try {		
			long start = System.nanoTime();
			s.solve2(phi, timeout, threshold);
			long elapsed = (System.nanoTime()-start)/1000000;
			System.out.println("time =  " + elapsed +" ms");
			System.out.println("threshold = " + threshold);
		} catch (SolverTimeoutException e) {
			// TODO Auto-generated catch block
			System.err.println("over the timeout");
		}
		
		phi = DimacsParser.CNFfromDIMACS2(filename);

		try {		
			long start = System.nanoTime();
			s2.solve(phi, timeout);
			long elapsed = (System.nanoTime()-start)/1000000;
			System.out.println("time without heursistics =  " + elapsed +" ms");
			System.out.println("threshold = " + threshold);
		} catch (SolverTimeoutException e) {
			// TODO Auto-generated catch block
			System.err.println("over the timeout");
		}
	}

}
