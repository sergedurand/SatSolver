package solver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import sun.java2d.pipe.DrawImage;
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
		int abd1 = 0;
		for(CNF phi : bench_formulas) {		
			cpt++;
			try {
				boolean res = s.solve2(phi,20);
				if(res) {
//					System.err.println(benchmark.get(cpt));
					System.err.println("SAT");
				}else {
					System.err.println("UNSAT");
				}
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				abd1++;
				System.out.println("failed at :" + benchmark.get(cpt));
			}
		}
		long elapsed_time1 = System.nanoTime()-start_time-clone_time;
		
		
		
		start_time = System.nanoTime();
		cpt = -1;
		int abd2 = 0;
		for(CNF phi : bench_formulas) {		
			cpt++;
			try {
				boolean res = s2.solve3(phi,20);
				if(res) {
					System.err.println("SAT");
				}else {
					System.err.println("UNSAT");
				}
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				abd2++;
				System.out.println("failed at :" + benchmark.get(cpt));
			}
		}
		long elapsed_time2 = System.nanoTime()-start_time-clone_time;
		System.out.println("temps total Twl = " + elapsed_time1/1000000 + " ms");
		System.out.println("Nombre de formule abandonn�e : " + abd1);
		System.out.println("temps total DPLL 2 = " + elapsed_time2/1000000 + " ms");
		System.out.println("Nombre de formule abandonn�e : " + abd2);
	}

}
