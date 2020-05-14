package tools;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import solver.Solver;
import solver.SolverTimeoutException;

public class SolverComparator {
	public SolverComparator() {
		super();
	}
	
	public static void compare(Solver s1, Solver s2,String folder, int timeout) {
		List<String> benchmark = Tools.listFiles(folder);
		ArrayList<CNF> bench_formulas = new ArrayList<CNF>();
		ArrayList<CNF> bench_formulas2 = new ArrayList<CNF>();


		try {
			for(String filename : benchmark) {
				CNF phi = DimacsParser.CNFfromDIMACS2(filename);
				bench_formulas.add(phi);
				bench_formulas2.add(Tools.cleanClone(phi));
			}
		} catch (CNFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int cpt = -1;
		int abd = 0;
		long time1 = 0;
		for(CNF phi : bench_formulas) {		
			cpt++;
			try {
				long start_time = System.nanoTime();
				boolean res = s1.solve(phi,timeout);
				long elapsed_time = System.nanoTime()-start_time;
				time1 += elapsed_time;
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				abd++;
				System.out.println("failed at :" + benchmark.get(cpt));
			}
		}
		String res1 = "Solver s1 : temps total = " + time1/1000000 + " ms\n";
		res1 += "Nombre de formules abandonnees: " + abd + "\n";
		
		cpt = -1;
		abd = 0;
		long time2 = 0;
		for(CNF phi : bench_formulas2) {		
			cpt++;
			try {
				long start_time = System.nanoTime();
				boolean res = s2.solve(phi,timeout);
				long elapsed_time = System.nanoTime()-start_time;
				time2 += elapsed_time;
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				abd++;
				System.out.println("failed at :" + benchmark.get(cpt));
			}
		}
		
		String res2 = "Solver s2: temps total = " + time2/1000000 + " ms\n";
		res2 += "Nombre de formules abandonnees: " + abd + "\n";
		
		System.out.println(res1);
		System.out.println(res2);
			
	}
	
	public static void compareOneSolver(Solver s1,String folder, int timeout) {
		List<String> benchmark = Tools.listFiles(folder);
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

		int cpt = -1;
		int abd = 0;
		long time1 = 0;
		for(CNF phi : bench_formulas) {		
			cpt++;
			try {
				long start_time = System.nanoTime();
				boolean res = s1.solve(phi,timeout);
				long elapsed_time = System.nanoTime()-start_time;
				time1 += elapsed_time;
			}catch(CNFException e){
				e.printStackTrace();
			}catch(SolverTimeoutException e){
				abd++;
				System.out.println("failed at :" + benchmark.get(cpt));
			}
		}
		String res1 = "Solver s1 : temps total = " + time1/1000000 + " ms\n";
		res1 += "Nombre de formules abandonnees: " + abd + "\n";
		System.out.println(res1);
	}
}
