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
		System.out.println(s.solve(phi2,5));
		
		
		String path = "./SAT";
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

		long start_time = System.nanoTime();
		int cpt = -1;
		int abd1 = 0;
		for(CNF phi : bench_formulas) {		
			cpt++;
			try {
				boolean res = s2.solve3(phi,20);
//				if(res) {
//					System.err.println(benchmark.get(cpt));
//					System.err.println("SAT");
//				}else {
//					System.err.println("UNSAT");
//				}
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				abd1++;
				System.out.println("failed at :" + benchmark.get(cpt));
			}
		}
		long elapsed_time1 = System.nanoTime()-start_time;
		
		
		
		start_time = System.nanoTime();
		cpt = -1;
		int abd2 = 0;
		for(CNF phi : bench_formulas) {		
			cpt++;
			try {
				boolean res = s.solve(phi,20);
//				if(res) {
//					System.err.println("SAT");
//				}else {
//					System.err.println("UNSAT");
//				}
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				abd2++;
				System.out.println("failed at :" + benchmark.get(cpt));
			}
		}
		long elapsed_time2 = System.nanoTime()-start_time;
		System.out.println("temps total DPLL 2 = " + elapsed_time1/1000000 + " ms");
		System.out.println("Nombre de formule abandonn�e : " + abd1);
		System.out.println("temps total twl = " + elapsed_time2/1000000 + " ms");
		System.out.println("Nombre de formule abandonn�e : " + abd2);

	}

}
