package solver;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import tools.DimacsParser;
import tools.SolverComparator;
import tools.Tools;

public class TestTwl2 {
	public static void main(String[] args) throws CNFException, SolverTimeoutException {
		SolverTwl s1 = new SolverTwl();
		SolverDPLLOptimized s2 = new SolverDPLLOptimized();
		CNF phi2 = DimacsParser.CNFfromDIMACS2("./benchmark/rand3_2.cnf");
		System.out.println(s2.solve(phi2,5));
		String path = "./benchmark";
		List<String> benchmark = Tools.listFiles(path);
		ArrayList<CNF> bench_formulas = new ArrayList<CNF>();

		try {
			for(String filename : benchmark) {
				CNF phi = DimacsParser.CNFfromDIMACS2(filename);
				bench_formulas.add(phi);
			}
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long start_time = System.nanoTime();
		long clone_time = 0;
		int cpt = -1;
		int abd = 0;
		for(CNF phi : bench_formulas) {		
			cpt++;
			try {
				boolean res = s2.solve(phi,20);
				if(res) {
					System.err.println(benchmark.get(cpt));
					System.err.println("SAT");
				}else {
					System.err.println(benchmark.get(cpt));
					System.err.println("UNSAT");
				}
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				abd++;
				System.out.println("failed at :" + benchmark.get(cpt));
			}
		}
		long elapsed_time = System.nanoTime()-start_time-clone_time;
		System.out.println("temps total = " + elapsed_time/1000000 + " ms");
		System.out.println("Nombre de formule abandonn�e : " + abd);
	}
}
