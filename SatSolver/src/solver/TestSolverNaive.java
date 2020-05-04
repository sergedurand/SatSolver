package solver;

import java.util.ArrayList;
import java.util.List;

import booleanFormula.CNF;
import booleanFormula.CNFException;
import sudoku.SudokuException;
import sudoku.SudokuTools;
import tools.Tools;

public class TestSolverNaive {
	public static void main(String[] args) throws CNFException, SolverTimeoutException {

		Solver s = new SolverNaive();
		String foldername = "C:\\Users\\Serge\\Documents\\Maths\\M1 JH ENS\\Logical aspect of AI\\"
				+ "SatSolver\\SatSolver\\data\\benchmark\\uf20";
		
		List<String> filenames = Tools.listFiles(foldername);
		ArrayList<CNF> benchmark = new ArrayList<CNF>();
		for(String filename : filenames) {
			CNF phi = SudokuTools.CNFfromDIMACS(filename);
			benchmark.add(phi);
		}
		
		long start_time = System.nanoTime();
		int cpt = 0;
		for(CNF phi : benchmark.subList(0, 2)) {
			try {
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
				System.out.println(e.toString());
			}
		}
		long elapsed_time = System.nanoTime()-start_time;
		System.out.println("temps total = " + elapsed_time/1000000000 + " s");
		System.out.println("Nombre de formule abandonnée : " + cpt);


	
	}
}
