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
		SolverTwl s = new SolverTwl();
		SolverDPLLOptimized s2 = new SolverDPLLOptimized();
		CNF phi2 = DimacsParser.CNFfromDIMACS2("./benchmark/pigeon5.cnf");
		System.out.println(phi2);
		System.out.println(s.solve2(phi2,5));
		
		
		String path = "./uuf50-218";
		List<String> benchmark = Tools.listFiles(path);
		ArrayList<CNF> bench_formulas = new ArrayList<CNF>();

		try {
			int cpt = 0;
			for(String filename : benchmark) {
				CNF phi = DimacsParser.CNFfromDIMACS2(filename);
				Tools.DimacsFromCNF(phi, cpt +"", "");
				cpt++;
				bench_formulas.add(phi);
			}
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
