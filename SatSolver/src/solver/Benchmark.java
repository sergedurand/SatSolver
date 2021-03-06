package solver;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import sudoku.SudokuTools;
import tools.Tools;

public class Benchmark {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length==0) {
			System.out.println("please put the path to the folder containing the dimacs files");
			//return;
		}
		String path = args[0];
		Solver s = new SolverDPLL();
		if(args.length> 2) {
			System.out.println("Too many arguments");
			return;
		}
		if(args.length==2 && args[1].equals("NAIVE")) {
			s = new SolverNaive();
		}
		List<String> benchmark = Tools.listFiles(path);
		ArrayList<CNF> bench_formulas = new ArrayList<CNF>();

		try {
			for(String filename : benchmark) {
				CNF phi = SudokuTools.CNFfromDIMACS(filename);
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
				boolean res = s.solve(phi,10);
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
