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
		String instructions = "";
		if(args.length==0) {
			System.out.println("please put the path to the folder containing the dimacs files");
			//return;
		}
		String path = "C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS\\Logical aspect of AI\\SatSolver\\SatSolver\\data\\bf";
		List<String> benchmark = Tools.listFiles(path);
		ArrayList<CNF> bench_formulas = new ArrayList<CNF>();
		SolverDPLL s = new SolverDPLL();

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
				long start_clone = System.nanoTime();
				phi = Tools.cloneFormula(phi);
				long elapsed_clone = System.nanoTime()-start_clone;
				clone_time += elapsed_clone;
				boolean res = s.solveRec(phi);
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
		long elapsed_time = System.nanoTime()-clone_time;
		System.out.println("temps total = " + elapsed_time/1_000_000 + " ms");
		System.out.println("Nombre de formule abandonnée : " + cpt);

	}

}
