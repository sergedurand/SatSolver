package solver;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import tools.DimacsParser;
import tools.Tools;

public class TestDPLLOptimized {

	public static void main(String[] args) throws CNFException, SolverTimeoutException {
		SolverDPLLOptimized s = new SolverDPLLOptimized();
		CNF phi3 = DimacsParser.CNFfromDIMACS2("./benchmark/kcolor3.cnf");
		System.out.println(phi3);
		System.out.println(s.solve(phi3,5));
		
		String path = "./benchmark";
		List<String> benchmark = Tools.listFiles(path);
		ArrayList<CNF> bench_formulas = new ArrayList<CNF>();

		try {
			for(String filename : benchmark) {
				CNF phi = DimacsParser.CNFfromDIMACS2(filename);
				System.err.println(filename);
				bench_formulas.add(phi);
			}
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long start_time = System.nanoTime();
		long clone_time = 0;
		int cpt = 0;
		for(CNF phi : bench_formulas) {		

			try {
				boolean res = s.solve(phi,5);
				if(res) {
					System.out.println("SAT");
				}else {
					System.out.println("UNSAT");
				}
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				cpt ++;
			}
		}
		long elapsed_time = System.nanoTime()-start_time-clone_time;
		System.out.println("temps total = " + elapsed_time/1000000 + " ms");
		System.out.println("Nombre de formule abandonn�e : " + cpt);
	}

}
