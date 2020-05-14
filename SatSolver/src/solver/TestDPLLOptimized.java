package solver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import tools.DimacsParser;
import tools.Tools;

public class TestDPLLOptimized {

	public static void main(String[] args) throws CNFException, SolverTimeoutException {
		SolverDPLLOptimized s = new SolverDPLLOptimized();
//		CNF phi3 = DimacsParser.CNFfromDIMACS2("./UNSAT/aim-50-1_6-no-1.cnf");
//		System.err.println(phi3);
//		for(int i = 0;i<21;i++) {
//			phi3.getVariables().setVal(i, 0);
//			s.deactivateSatClauses(i, phi3);
//			s.keepPropagating(phi3);
//		}
//		System.err.println(phi3);
//		phi3.getVariables().setVal(21, 1);
//		s.deactivateSatClauses(21, phi3);
//		System.err.println(phi3);
//		s.keepPropagating(phi3);
//		System.err.println(phi3);
//		phi3.getVariables().setVal(22, 1);
//		s.deactivateSatClauses(22, phi3);
//		System.err.println(phi3);
//		s.keepPropagating(phi3);
//		System.err.println(phi3);
		
//		phi3.getVariables().setVal(11, 0);
//		s.deactivateSatClauses(11, phi3);
//		System.err.println(phi3);
//		phi3.getVariables().setVal(10, 0);
//		s.deactivateSatClauses(10, phi3);
//		phi3.getVariables().setVal(9, 1);
//		s.deactivateSatClauses(9, phi3);
//		phi3.getVariables().setVal(0, 0);
//		s.deactivateSatClauses(0, phi3);
//		phi3.getVariables().setVal(3, 0);
//		s.deactivateSatClauses(3, phi3);
//		phi3.getVariables().setVal(6, 0);
//		s.deactivateSatClauses(6, phi3);
//		phi3.getVariables().setVal(8, 0);
//		s.deactivateSatClauses(8, phi3);
//		System.err.println(phi3);
//		int[] res = s.unitPropagation(phi3);
//		System.out.println(res[0]);

//	   System.out.println(s.solve(phi3,15));
//	   Tools.printInterpration(s.getInterpretation());
		
		String path = "./UNSAT";
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
				boolean res = s.solve(phi,20);
//				if(res) {
//					System.err.println(benchmark.get(cpt));
//					System.err.println("SAT");
//				}else {
//					System.err.println("UNSAT");
//				}
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
